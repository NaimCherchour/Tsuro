package main.java.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class BotTsuro extends Joueur {
    private List<Tuile> tuilesDisponibles;

    public BotTsuro(int ligne, int colonne, int PointEntree, String prenom) {
        super(ligne, colonne, PointEntree, prenom, true);
        this.tuilesDisponibles = new ArrayList<>();
    }

    public Mouvement choisirEtAppliquerMouvement(Game game) {
        if (tuilesDisponibles.isEmpty()) {
            tuilesDisponibles = TuilesGenerator.genererToutesLesTuiles().subList(0, 3);
        }

        PlateauTuiles.Direction direction = this.getDirectionEntree();
        int cibleX = this.getLigne() + direction.di();
        System.out.println("x: "+cibleX);
        int cibleY = this.getColonne() + direction.dj();
        System.out.println("y: "+cibleY);

        Mouvement mouvementOptimal = null;
        int scoreMax = Integer.MIN_VALUE;

        for (Tuile tuile : tuilesDisponibles) {
            for (int rotation = 0; rotation < 4; rotation++) {
                Tuile copie = new Tuile(tuile.getId()); // Créer une copie de la tuile pour tester les rotations
                copie.setTableauChemins(Arrays.copyOf(tuile.getTableauChemins(), tuile.getTableauChemins().length));

                copie.setRotation(rotation);
                if (game.getPlateau().peutPlacerTuile(cibleX, cibleY, tuile)) {
                    // Création d'un objet Mouvement pour l'évaluation
                    Mouvement mouvementTemporaire = new Mouvement(tuile, 0, cibleX, cibleY, 0); // Le score est mis à 0 ici car il sera calculé par evaluerMouvement
                    int score = evaluerMouvement(mouvementTemporaire, game); // Utilisation de evaluerMouvement
                    System.out.println("Score: "+score);
                    if (score > scoreMax) {
                        scoreMax = score;
                        System.out.println("scoreMax après la valuation = "+scoreMax);
                        mouvementOptimal = mouvementTemporaire;
                        mouvementOptimal.score = score; // Mettre à jour le score du mouvement optimal
                        System.out.println("score du Mouvement optimal = "+scoreMax);


                    }
                }
            }
        }

        if (mouvementOptimal != null) {
            appliquerMouvement(mouvementOptimal, game);
        } else {
            System.out.println("Aucun mouvement valide trouvé.");
        }

        return mouvementOptimal;
    }


    private void appliquerMouvement(Mouvement mouvement, Game game) {
        game.getPlateau().placerTuile( mouvement.getTuile(), this);
        game.getPlateau().actualiserPosJ(this);
    }

    public void setTuilesDisponibles(List<Tuile> tuilesDisponibles) {
        this.tuilesDisponibles = tuilesDisponibles;
    }


    private List<Tuile> genererTuilesAleatoires() {
        List<Tuile> toutesLesTuiles = TuilesGenerator.genererToutesLesTuiles();
        List<Tuile> tuilesAleatoires = new ArrayList<>();
        Random rand = new Random();

        while (tuilesAleatoires.size() < 3) {
            int indexAleatoire = rand.nextInt(toutesLesTuiles.size());
            Tuile tuileSelectionnee = toutesLesTuiles.get(indexAleatoire);
            if (!tuilesAleatoires.contains(tuileSelectionnee)) {
                tuilesAleatoires.add(tuileSelectionnee);
            }
        }

        return tuilesAleatoires;
    }


    private int evaluerMouvement(Mouvement mouvement, Game game) {
        int score = 0;

        // Privilégier les coups gagnants / qui font perdre les adversaires
        if (estCoupGagnant(mouvement, game)) {
            score += 1000;
        }

        // Éviter de perdre
        if (estCoupPerdant(mouvement, game)) {
            score -= 1000;
        }

        // Essayer de rester dans les cases centrales
        int centre = game.getPlateau().getTaille() / 2;
        if (Math.abs(mouvement.getX() - centre) <= 1 && Math.abs(mouvement.getY() - centre) <= 1) {
            score += 500;
        }

        return score;
    }

    private boolean estCoupGagnant(Mouvement mouvement, Game game) {
        for (Joueur adversaire : game.getJoueurs()) {
            if (adversaire != this && estCoupPerdantPourAdversaire(mouvement, game, adversaire)) {
                return true;
            }
        }
        return false;
    }

    private boolean estCoupPerdantPourAdversaire(Mouvement mouvement, Game game, Joueur adversaire) {
        // Ici, on réutilise la logique de estCoupPerdant mais en appliquant la vérification à l'adversaire
        PlateauTuiles plateau = game.getPlateau();
        Tuile tuile = mouvement.getTuile();
        int x = mouvement.getX();
        int y = mouvement.getY();
        tuile.setRotation(mouvement.getRotation());

        // Calculer la sortie basée sur le point d'entrée de l'adversaire et la rotation de la tuile
        int pointEntreeAdversaire = adversaire.getEntree(); // Remarque: Doit être calculé pour l'adversaire
        int pointSortie = tuile.getPointSortieAvecRot(tuile.getRotation());

        // Calculer la direction de sortie pour l'adversaire
        PlateauTuiles.Direction directionSortieAdversaire = PlateauTuiles.Direction.getDirectionFromPoint(pointSortie);

        // Calculer la nouvelle position pour l'adversaire
        assert directionSortieAdversaire != null;
        int nouvelleXAdversaire = x + directionSortieAdversaire.di();
        int nouvelleYAdversaire = y + directionSortieAdversaire.dj();

        // Vérifier si la nouvelle position de l'adversaire est en dehors des limites du plateau
        if (!plateau.coordonneesValides(nouvelleXAdversaire, nouvelleYAdversaire)) {
            return true; // Sortir du plateau est un coup perdant pour l'adversaire
        }

        // Vérifier si le chemin de sortie est emprunté pour l'adversaire (collision avec un chemin déjà occupé)
        Tuile tuileSuivanteAdversaire = plateau.getTuile(nouvelleXAdversaire, nouvelleYAdversaire);
        if (tuileSuivanteAdversaire != null) {
            int[] pointsConnexionAdversaire = tuileSuivanteAdversaire.getPointsDeConnexion(directionSortieAdversaire.oppose());
            Tuile.Chemin cheminSuivantAdversaire = tuileSuivanteAdversaire.getTableauChemins()[pointsConnexionAdversaire[0]];
            return cheminSuivantAdversaire.estEmprunte(); // Entrer dans un chemin emprunté est un coup perdant pour l'adversaire
        }

        return false; // Si aucun des cas ci-dessus, le coup n'est pas perdant pour l'adversaire
    }


    private boolean estCoupPerdant(Mouvement mouvement, Game game) {
        PlateauTuiles plateau = game.getPlateau();
        int x = mouvement.getX();
        int y = mouvement.getY();
        Tuile tuile = mouvement.getTuile();
        tuile.setRotation(mouvement.getRotation());

        // Calculer la sortie basée sur le point d'entrée et la rotation de la tuile
        int pointEntree = this.getEntree(); // Suppose que getEntree() renvoie le point d'entrée actuel du joueur
        int pointSortie = tuile.getPointSortieAvecRot(tuile.getRotation());

        // Calculer la direction de sortie
        PlateauTuiles.Direction directionSortie = PlateauTuiles.Direction.getDirectionFromPoint(pointSortie);

        // Calculer la nouvelle position
        assert directionSortie != null;
        int nouvelleX = x + directionSortie.di();
        int nouvelleY = y + directionSortie.dj();

        // Vérifier si la nouvelle position est en dehors des limites du plateau
        if (!plateau.coordonneesValides(nouvelleX, nouvelleY)) {
            return true; // Sortir du plateau est un coup perdant
        }

        // Vérifier si le chemin de sortie est emprunté (collision avec un chemin déjà occupé)
        Tuile tuileSuivante = plateau.getTuile(nouvelleX, nouvelleY);
        if (tuileSuivante != null) {
            int[] pointsConnexion = tuileSuivante.getPointsDeConnexion(directionSortie.oppose());
            Tuile.Chemin cheminSuivant = tuileSuivante.getTableauChemins()[pointsConnexion[0]];
            return cheminSuivant.estEmprunte(); // Entrer dans un chemin emprunté est un coup perdant
        }

        return false; // Si aucun des cas ci-dessus, le coup n'est pas perdant
    }

    public static void main(String[] args) {
        testGenererTuilesAleatoires();
        System.out.println("--------------------------------");
        testChoisirEtAppliquerMouvement();
    }

    public static void testGenererTuilesAleatoires() {
        BotTsuro bot = new BotTsuro(0, 0, 0, "TestBot");
        List<Tuile> tuiles = bot.genererTuilesAleatoires();

        System.out.println("Test de genererTuilesAleatoires:");

        if (tuiles.size() == 3) {
            System.out.println("Succès: 3 tuiles ont été générées.");
        } else {
            System.out.println("Échec: le nombre de tuiles générées est incorrect.");
        }

        long uniqueCount = tuiles.stream().distinct().count();
        if (uniqueCount == 3) {
            System.out.println("Succès: toutes les tuiles sont uniques.");
        } else {
            System.out.println("Échec: certaines tuiles générées ne sont pas uniques.");
        }
    }

    public static void testChoisirEtAppliquerMouvement() {
        Game game = new Game(6, 2); // Ajustez selon votre implémentation
        BotTsuro bot = new BotTsuro(3, 3, 0, "TestBot");
        // Création de la tuile
        Tuile tuile = new Tuile(2, new int[]{1, 0, 4, 7, 2, 6, 5, 3});

        // Création de la liste contenant trois fois la même tuile
        List<Tuile> tuilesDisponibles = new ArrayList<>();
        tuilesDisponibles.add(tuile);
        tuilesDisponibles.add(tuile);
        tuilesDisponibles.add(tuile);

        // Attribution de la liste de tuiles disponibles au bot
        bot.setTuilesDisponibles(tuilesDisponibles);


        System.out.println("Premier test de choisirEtAppliquerMouvement:");

        Mouvement premierMouvement = bot.choisirEtAppliquerMouvement(game);
        if (premierMouvement != null && game.getPlateau().getTuile(premierMouvement.getX(), premierMouvement.getY()) == premierMouvement.getTuile()) {
            System.out.println("Succès: Un premier mouvement valide a été choisi et appliqué.");
        } else {
            System.out.println("Échec: Aucun mouvement valide n'a été trouvé ou appliqué lors du premier essai.");
        }

        // Réappliquer choisirEtAppliquerMouvement pour voir si le bot peut choisir un autre mouvement valide
        System.out.println("Deuxième test de choisirEtAppliquerMouvement:");

        Mouvement deuxiemeMouvement = bot.choisirEtAppliquerMouvement(game);
        if (deuxiemeMouvement != null && game.getPlateau().getTuile(deuxiemeMouvement.getX(), deuxiemeMouvement.getY()) == deuxiemeMouvement.getTuile()) {
            System.out.println("Succès: Un deuxième mouvement valide a été choisi et appliqué.");
        } else {
            System.out.println("Échec: Aucun mouvement valide n'a été trouvé ou appliqué lors du deuxième essai.");
        }
    }



    public static class Mouvement {
        private final Tuile tuile;
        private final int rotation;
        private final int x, y;
        private int score; // Utilisé pour l'évaluation dans MinMax

        public Mouvement(Tuile tuile, int rotation, int x, int y, int score) {
            this.tuile = tuile;
            this.rotation = rotation;
            this.x = x;
            this.y = y;
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public int getX() {
            return x;
        }

        public Tuile getTuile() {
            return tuile;
        }

        public int getY() {
            return y;
        }

        public int getRotation() {
            return rotation;
        }
    }
}
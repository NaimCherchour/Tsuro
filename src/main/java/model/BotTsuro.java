package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class BotTsuro extends Joueur {

    /**
     * Constructeur du Bot est le meme que le joueur , rien de plus
     * @param prenom
     * @param joueurs
     */
    public BotTsuro(String prenom, List<Joueur> joueurs) {
        super(prenom, joueurs);
    }

    /**
     * MÉTHODE PRINCIPALE du choix du mouvement ( la tuile ) parmi 12 choix en utilisant une approche de score MinMax
     * @param game ; pour pouvoir tester les tuiles disponibles , cela se fait sur des copies de Joueurs , Plateau et Tuiles
     *              pour ne pas modifier le jeu actuel jusqu'à poser la bonne Tuile
     */
    public void choisirEtAppliquerMouvement(Game game) {

        //TODO : Revoir les Copies -> améliorer l'efficacité
        // COPIE DES TUILES DU DECK : OBLIGATOIRE
        // COPIE DU PLATEAU ACTUEL ; pour tracker le mouvement du BoT également avec des tuiles voisines
        // COPIE DES JOUEURS ; pour ne pas modiifer leurs coordonnées X et Y et points d'entrée ( qui est le point actuel )
        Tuile[] tuilesDisponibles = game.getDeckTuiles().copy(); // copie des Tuiles du Deck ; une copie pas un clone car on a besoin de copier que un seul attribut ( sideTuiles )
        PlateauTuiles plateauCopie = game.getPlateau().clone(); //copie du Plateau

        Mouvement mouvementOptimal = null; // le mouvement à choisir à la fin
        int scoreMax = Integer.MIN_VALUE;


        // Évaluation de toutes les tuiles et rotations possibles
        for (int i = 0; i < tuilesDisponibles.length; i++) {

            Tuile tuileTest = tuilesDisponibles[i]; //Il s'agit d'une copie d'une tuile du deck

            for (int rotation = 0; rotation < 4; rotation++) { // 3 * 4 itérations totales
                tuileTest.setRotation(rotation); // 0 puis 1 puis 2 puis 3
                int score = 0;

                // copie des joueurs pour ne pas modifier leurs coordonnées lors des tests
                // On refait la copie à chaque itération ; pour reinitialiser les posX posY et PointEntrée des Joueurs
                // On les réinitialise comme dans le jeu avant de poser cette tuile test
                List<Joueur> copieJoueurs = new ArrayList<>();
                Joueur copieBOT = null;
                for (Joueur joueur : game.getJoueurs()) {
                    Joueur cop = joueur.clone() ;
                    copieJoueurs.add(cop);
                    if ( joueur instanceof BotTsuro) {
                        copieBOT = cop ; // copie du BOT sauvegardée dans la variable copieBOT
                    }
                }

                assert copieBOT != null;
                int initX = copieBOT.getLigne();
                int initY = copieBOT.getColonne();
                int point = copieBOT.getPointEntree();
                System.out.println("Position actuelle du bot (X= " + copieBOT.getLigne() + ", Y="  + copieBOT.getColonne() + ")");

                //TODO : à améliorer car il y'a des cas ou le Bot Perd ;
                // Tests pour le BOT ; si on arrive à placer une Tuile et on ne meurs pas donc c'est un bon coup
                if (plateauCopie.placerTuile(tuileTest, copieBOT, copieJoueurs)) {
                    System.out.println("Bot etat : " + copieBOT.isAlive());
                    if ( copieBOT.isAlive() ) {
                        score += 1000 ;
                        System.out.println("Mouvement gagnant détecté.");
                    }
                } else {
                    score -= 1000;
                    System.out.println("Mouvement perdant détecté.");
                }
                System.out.println("Résultat du mouvement : Tuile=" + tuileTest.getId() + ", Rotation=" + rotation + ", Score =" + score);

                // reset the board pour chaque essai en enlevant la dernière tuile
                System.out.println(plateauCopie.getTuile(initX,initY));
                plateauCopie.enleverTuile(initX,initY); // on enleve la tuile à posX et posY
                copieBOT.setAlive(true);
                // MinMax
                if (score > scoreMax) {
                    scoreMax = score;
                    mouvementOptimal = new Mouvement(tuileTest, rotation, this.getLigne(), this.getColonne(), score, i);
                    System.out.println("Nouveau mouvement optimal trouvé avec score " + score);
                }
            }
        }

        if ( mouvementOptimal != null ) {
            // récupérer la Tuile du Deck à placer
            Tuile placedTile = game.getDeckTuiles().getTuile(mouvementOptimal.getIndexTuile());
            placedTile.setRotation(mouvementOptimal.getRotation());
            System.out.println("La tuile à poser est " + placedTile.getId() + " rot " + placedTile.getRotation());
            game.getPlateau().placerTuile(placedTile,this, game.getJoueurs());
            System.out.println(this.isAlive());
        } else {
            System.out.println("Aucun mouvement valide trouvé pour le bot.");
        }
    }


    private int evaluerMouvement(Tuile tuile, PlateauTuiles plateau, List<Joueur> joueurs) {
        int score = 0;
        Joueur copie = this.clone();
        copie.setLigne(this.getLigne());
        copie.setColonne(this.getColonne());
        if (plateau.placerTuile(tuile, copie, joueurs) || !copie.isAlive()) {
            score += 1000;
            System.out.println("Mouvement gagnant détecté.");
        } else {
            score -= 1000;
            System.out.println("Mouvement perdant détecté.");
        }
        // Ajoute des bonus pour les positions stratégiques, etc.
        return score;
    }




    //TODO : Revoir les méthpdes estCoupGagnant et estCoupPerdant pour améliorer le BOT
    private boolean estCoupGagnant(Mouvement mouvement, PlateauTuiles plateau, List<Joueur> joueurs) {
        for (Joueur adversaire : joueurs) {
            if (adversaire != this && estCoupPerdantPourAdversaire(mouvement, plateau, adversaire, joueurs)) {
                return true;
            }
        }
        return false;
    }

    private boolean estCoupPerdantPourAdversaire(Mouvement mouvement, PlateauTuiles plateau, Joueur adversaire, List<Joueur> joueurs) {
        // Calculer la nouvelle position hypothétique de l'adversaire après le mouvement
        Tuile tuile = mouvement.getTuile();
        int pointSortie = tuile.getPointSortieAvecRot(adversaire.getPointEntree());
        PlateauTuiles.Direction directionSortie = PlateauTuiles.Direction.getDirectionFromPoint(pointSortie);
        int nouvelleX = adversaire.getLigne() + directionSortie.di();
        int nouvelleY = adversaire.getColonne() + directionSortie.dj();

        if (!plateau.coordonneesValides(nouvelleX, nouvelleY)) {
            return true; // Sortir du plateau est un coup perdant pour l'adversaire
        }

        // à vérifier: si le chemin de sortie est déjà occupé, ce qui causerait une collision
        if (plateau.getTuile(nouvelleX, nouvelleY) != null) {
            Tuile tuileSuivante = plateau.getTuile(nouvelleX, nouvelleY);
            int pointEntréeSuivant = PlateauTuiles.Direction.getPointFromDirection(directionSortie.oppose(), pointSortie);
            Tuile.Chemin cheminSuivant = tuileSuivante.getTableauChemins()[pointEntréeSuivant];
            if (cheminSuivant.estEmprunte()) {
                return true; // Entrer dans un chemin déjà occupé est un coup perdant
            }
        }

        return false; // Si aucun des cas ci-dessus, le coup n'est pas perdant pour l'adversaire
    }


    private boolean estCoupPerdant(Mouvement mouvement, PlateauTuiles plateau) {
        int nouvelleX = mouvement.getX() + PlateauTuiles.Direction.getDirectionFromPoint(mouvement.getTuile().getPointSortieAvecRot(mouvement.getTuile().getRotation())).di();
        int nouvelleY = mouvement.getY() + PlateauTuiles.Direction.getDirectionFromPoint(mouvement.getTuile().getPointSortieAvecRot(mouvement.getTuile().getRotation())).dj();
        if (!plateau.coordonneesValides(nouvelleX, nouvelleY)) {
            System.out.println("Sortie du plateau détectée.");
            return true;
        }
        return false;
    }

    public static class Mouvement {

        private final Tuile tuile; // Tuile à poser en appliquant ce mouvement
        private final int rotation; // la rotation de la tuile à poser
        private final int x, y; // les coordonées de la Tuile à poser
        private int score; // Utilisé pour l'évaluation dans MinMax
        private int indexTuile; // Indice de la tuile dans le deck

        /**
         * Constructeur pour Mouvement.
         *
         * @param tuile       La tuile à poser
         * @param rotation    La rotation appliquée à la tuile.
         * @param x           La coordonnée X du bot après le mouvement.
         * @param y           La coordonnée Y du bot après le mouvement.
         * @param score       Le score associé au mouvement.
         * @param indexTuile  L'index de la tuile dans le deck.
         */
        public Mouvement(Tuile tuile, int rotation, int x, int y, int score, int indexTuile) {
            this.tuile = tuile;
            this.rotation = rotation;
            this.x = x;
            this.y = y;
            this.score = score;
            this.indexTuile = indexTuile;
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

        public int getIndexTuile() {
            return indexTuile;
        }
    }
}


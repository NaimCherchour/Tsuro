package main.java.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BotTsuro extends Joueur {

    public BotTsuro(String prenom, List<Joueur> joueurs) {
        super(prenom, joueurs);
    }

    public Mouvement choisirEtAppliquerMouvement(Game game) {
        Tuile[] tuilesDisponibles = game.getDeckTuiles().getSideTuiles();
        System.out.println("Nombre de tuiles disponibles: " + tuilesDisponibles.length);

        PlateauTuiles.Direction directionActuelle = PlateauTuiles.Direction.getDirectionFromPoint(this.getPointEntree());
        int cibleX = this.getLigne() ;
        int cibleY = this.getColonne() ;
        System.out.println("Direction actuelle: " + directionActuelle + ", cibleX: " + cibleX + ", cibleY: " + cibleY);

        Mouvement mouvementOptimal = null;
        int scoreMax = Integer.MIN_VALUE;

        for (Tuile tuile : tuilesDisponibles) {
            for (int rotation = 0; rotation < 4; rotation++) {
                Tuile copie = new Tuile(tuile.getId());
                copie.setTableauChemins(Arrays.copyOf(tuile.getTableauChemins(), tuile.getTableauChemins().length));
                for (int r = 0; r <= rotation; r++) {
                    copie.tournerTuile();  // Tourner la tuile pour chaque rotation
                }
                System.out.println("Tuile ID: " + tuile.getId() + ", Rotation appliquée: " + rotation);
                if (game.getPlateau().peutPlacerTuile(cibleX,cibleY) ) {
                    Mouvement mouvementTemporaire = new Mouvement(copie, rotation, cibleX, cibleY, 0);
                    int score = evaluerMouvement(mouvementTemporaire, game);
                    System.out.println("Score du mouvement testé: " + score);
                    if (score > scoreMax) {
                        scoreMax = score;
                        mouvementOptimal = mouvementTemporaire;
                        mouvementOptimal.score = score;
                    }
                } else {
                    System.out.println("Position hors limites: (" + cibleX + ", " + cibleY + ")");
                }
            }
        }
        System.out.println("Score max après évaluation: " + scoreMax);
        if (mouvementOptimal != null) {
            appliquerMouvement(mouvementOptimal, game);
        } else {
            System.out.println("Aucun mouvement valide trouvé.");
        }

        return mouvementOptimal;
    }

    private void appliquerMouvement(Mouvement mouvement, Game game) {
        System.out.println("Appliquer mouvement: Tuile ID " + mouvement.getTuile().getId() + " à la position [" + mouvement.getX() + "," + mouvement.getY() + "]");
        game.getPlateau().placerTuile(mouvement.getTuile(), this, game.getJoueurs());
        game.getPlateau().actualiserPosJ(game.getJoueurs());
    }

    private int evaluerMouvement(Mouvement mouvement, Game game) {
        int score = 0;
        if (estCoupGagnant(mouvement, game)) {
            score += 1000;
            System.out.println("Mouvement gagnant détecté.");
        }
        if (estCoupPerdant(mouvement, game)) {
            score -= 1000;
            System.out.println("Mouvement perdant détecté.");
        }
        int centre = 3;  // Suppose un centre hypothétique pour des raisons de scoring
        if (Math.abs(mouvement.getX() - centre) <= 1 && Math.abs(mouvement.getY() - centre) <= 1) {
            score += 500;
            System.out.println("Mouvement proche du centre, score bonus ajouté.");
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
        tuile.tournerTuile(mouvement.getRotation());

        // Calculer la sortie basée sur le point d'entrée de l'adversaire et la rotation de la tuile
        int pointEntreeAdversaire = adversaire.getPointEntree(); // Remarque: Doit être calculé pour l'adversaire
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
        tuile.tournerTuile(mouvement.getRotation());

        // Calculer la sortie basée sur le point d'entrée et la rotation de la tuile
        int pointEntree = this.getPointEntree();
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

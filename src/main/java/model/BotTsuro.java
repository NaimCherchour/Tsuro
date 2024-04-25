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

        PlateauTuiles plateauCopie = game.getPlateau().copiePlateau();

        int cibleX = this.getLigne();
        int cibleY = this.getColonne();
        System.out.println("Position actuelle du bot (X=" + cibleX + ", Y=" + cibleY + ")");
        Mouvement mouvementOptimal = null;
        int scoreMax = Integer.MIN_VALUE;

        // Évaluation de toutes les tuiles et rotations possibles
        for (int i = 0; i < tuilesDisponibles.length; i++) {
            Tuile tuileOriginale = tuilesDisponibles[i];
            System.out.println("");
            for (int rotation = 0; rotation < 4; rotation++) {
                Tuile copie = tuileOriginale.copier();
                copie.tournerTuile(rotation);

                if (plateauCopie.peutPlacerTuile(cibleX, cibleY)) {
                    int score = evaluerMouvement(new Mouvement(copie, rotation, cibleX, cibleY, 0, i), plateauCopie, game.getJoueurs());
                    System.out.println("Évaluation du mouvement avec tuile ID " + copie.getId() + " à la rotation " + rotation + ": score=" + score);
                    System.out.println("Début du mouvement : Tuile=" + copie.getId() + ", Rotation=" + rotation + ", Score initial=" + score);
                    if (score > scoreMax) {
                        scoreMax = score;
                        mouvementOptimal = new Mouvement(copie, rotation, cibleX, cibleY, score, i);
                        System.out.println("Nouveau mouvement optimal trouvé avec score " + score);
                    }
                }
            }
        }

        if (mouvementOptimal != null) {
            game.getPlateau().placerTuile(mouvementOptimal.getTuile(), this, game.getJoueurs());
            game.getPlateau().actualiserPosJ(game.getJoueurs());
            System.out.println("Mouvement optimal appliqué avec succès. Tuile placée à X=" + mouvementOptimal.getX() + ", Y=" + mouvementOptimal.getY());
        } else {
            System.out.println("Aucun mouvement valide trouvé pour le bot.");
        }

        return mouvementOptimal;
    }



    private int evaluerMouvement(Mouvement mouvement, PlateauTuiles plateau, List<Joueur> joueurs) {
        int score = 0;
        if (estCoupGagnant(mouvement, plateau, joueurs)) {
            score += 1000;
            System.out.println("Mouvement gagnant détecté.");
        }
        if (estCoupPerdant(mouvement, plateau)) {
            score -= 1000;
            System.out.println("Mouvement perdant détecté.");
        }
        // Ajoute des bonus pour les positions stratégiques, etc.
        return score;
    }




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
        private final Tuile tuile;
        private final int rotation;
        private final int x, y;
        private int score; // Utilisé pour l'évaluation dans MinMax
        private int indexTuile; // Indice de la tuile dans le deck

        public Mouvement(Tuile tuile, int rotation, int x, int y, int score, int indexTuile) {
            this.tuile = tuile;
            this.rotation = rotation;
            this.x = x;
            this.y = y;
            this.score = score;
            this.indexTuile = indexTuile;
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

        public int getIndexTuile() {
            return indexTuile;
        }
    }
}


package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BotTsuro extends Joueur {
    private List<Tuile> tuilesDisponibles;

    public BotTsuro(int ligne, int colonne, int PointEntree, String prenom) {
        super(ligne, colonne, PointEntree, prenom, true);
        this.tuilesDisponibles = new ArrayList<>();
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
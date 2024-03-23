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
        game.getPlateau().placerTuile(mouvement.getX(), mouvement.getY(), mouvement.getTuile(), this);
        game.getPlateau().actualiserPosJ(this);
    }

    public void setTuilesDisponibles(List<Tuile> tuilesDisponibles) {
        this.tuilesDisponibles = tuilesDisponibles;
    }


    private List<Tuile> genererTuilesAleatoires(int nombreTuiles) {
        List<Tuile> toutesLesTuiles = TuilesGenerator.genererToutesLesTuiles();
        List<Tuile> tuilesAleatoires = new ArrayList<>();
        Random rand = new Random();

        while (tuilesAleatoires.size() < nombreTuiles) {
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

    private boolean estCoupPerdant(Mouvement mouvement, Game game) {
        return false;
    }

    private boolean estCoupGagnant(Mouvement mouvement, Game game) {
        return false;
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
package main.java.model;

import java.util.Scanner;

public class PlateauTuiles {
    private Tuiles[][] plateau;

    public PlateauTuiles(int taille) {
        plateau = new Tuiles[taille][taille];
    
        // Placer des tuiles sur les bords pour les entrés
        for (int i = 1; i < taille - 1; i++) {
            plateau[0][i] = new Tuiles(); // Bord supérieur
            plateau[taille - 1][i] = new Tuiles(); // Bord inférieur
            plateau[i][0] = new Tuiles(); // Bord gauche
            plateau[i][taille - 1] = new Tuiles(); // Bord droit
        }

    }

    public void placerTuile(int ligne, int colonne, Tuiles tuile) {
        if (ligne <= 0 || ligne >= plateau.length - 1 || colonne <= 0 || colonne >= plateau[0].length - 1) {
            System.out.println("Impossible de placer une tuile sur les bords du plateau.");
            return;
        }
        plateau[ligne][colonne] = tuile;
    }

    public void afficherPlateau() {
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j] == null) {
                    System.out.print("Case vide\t");
                } else {
                    System.out.print("Tuile " + "\t");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        PlateauTuiles plateau = new PlateauTuiles(9);
        plateau.afficherPlateau();

        // Demander une tuile à placer
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez les coordonnées de la tuile (ligne colonne) :");
        int ligne = scanner.nextInt();
        int colonne = scanner.nextInt();
        Tuiles tuile = new Tuiles(); 
        plateau.placerTuile(ligne, colonne, tuile);

        plateau.afficherPlateau();

        scanner.close();
    }
}

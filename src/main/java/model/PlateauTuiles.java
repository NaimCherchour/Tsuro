package main.java.model;

import java.util.Scanner;

public class PlateauTuiles {
    private Tuiles[][] plateau;

    public PlateauTuiles(int taille) {
        plateau = new Tuiles[taille][taille];
    }

    public void placerTuile(int ligne, int colonne, Tuiles tuile) {
        if (ligne <= 0 || ligne >= plateau.length - 1 || colonne <= 0 || colonne >= plateau[0].length - 1 || plateau[ligne][colonne]==null) {
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


    public boolean joueurPerdu(Joueur joueur) {
        int ligne = joueur.getLigne();
        int colonne = joueur.getColonne();
    
        return (ligne == 0 || ligne == plateau.length - 1 || colonne == 0 || colonne == plateau[0].length - 1);
    }

    public void reinitialiserPlateau() {
        // Remettre à zéro les tuiles
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                plateau[i][j] = null;
            }
        }
    
        // Les deux joueurs sont replacé a leur place initial : pour plus de joueur on pourra utiliser des enum si les places sont toujours les même ou alors utilisé une boucle 
        Joueur joueur1 = new Joueur(3, 4, 2);
        Joueur joueur2 = new Joueur(5, 6, 1);
    
        // Générer de nouvelles tuiles aléatoires (si nécessaire)
        //genererTuilesAleatoires();
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

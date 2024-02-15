
package main.java.model;
import java.util.Scanner;

public class PlateauTuiles {
    private Tuiles[][] plateau;
    public int [] interConnection = {5,4,7,6,1,0,3,2}; // pour chaque index, correspond au nouvelle index dans la tuile suivante

    public PlateauTuiles(int taille) {
        plateau = new Tuiles[taille][taille];
    }

    public void placerTuile(int ligne, int colonne, Tuiles tuile, Joueur j ) {
        System.out.println("Impossible de placer une tuile ici.");
        if (ligne < 0 || ligne >= plateau.length  || colonne < 0 || colonne >= plateau.length  || plateau[ligne][colonne]==null || Math.abs(ligne - j.getLigne()) != 1 || Math.abs(colonne - j.getColonne()) != 1) {
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
    public void ActualiserPosJ(Joueur j){ // Uniquement appellé apres le placement d'une tuile
        int entree = j.getEntree();
        if (entree < 2) {
            j.setLigne(j.getLigne() - 1);
        } else if (entree > 1 && entree < 4) {
            j.setColonne(j.getColonne() + 1);
        } else if (entree > 3 && entree < 6) {
            j.setLigne(j.getLigne() + 1);
        } else if (entree > 5 && entree < 8) {
            j.setColonne(j.getColonne() - 1);
        }
        if (joueurPerdu(j)){
            System.out.println("Le joueur a perdu.");
        }
        else { //recherche de la nouvelle entré
            Tuiles nvTuiles = plateau[j.getLigne()][j.getColonne()];
            j.setEntree(nvTuiles.GettabTui()[(nvTuiles.getRotation()*2+interConnection[j.getEntree()])%8]);// Verifie les rotations pour donner le nouveau point ou positionner la pion
            }
    }


    public boolean joueurPerdu(Joueur joueur) {
        int ligne = joueur.getLigne();
        int colonne = joueur.getColonne();
    
        return (ligne < 0 || ligne >= plateau.length  || colonne < 0 || colonne >= plateau.length );
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
        
        //plateau.placerTuile(ligne, colonne, tuile);

        plateau.afficherPlateau();

        scanner.close();
    }
}

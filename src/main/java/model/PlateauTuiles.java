package main.java.model;
import java.util.Scanner;

/**
 * La classe PlateauTuiles représente le plateau de jeu composé de tuiles.
 */
public class PlateauTuiles {
    private Tuiles[][] plateau; // Matrice représentant le plateau de tuiles.
    public int[] interConnection = {5,4,7,6,1,0,3,2}; // Pour chaque index, correspond à la nouvelle index dans la tuile suivante.

    /**
     * Constructeur de la classe PlateauTuiles.
     * 
     * @param taille La taille du plateau de jeu.
     */
    public PlateauTuiles(int taille) {
        plateau = new Tuiles[taille][taille];
    }

    /**
     * Place une tuile sur le plateau à la position spécifiée et vérifie si le joueur perd.
     * 
     * @param ligne La ligne où placer la tuile.
     * @param colonne La colonne où placer la tuile.
     * @param tuile La tuile à placer.
     * @param j Le joueur associé à la tuile.
     */
    public void placerTuile(int ligne, int colonne, Tuiles tuile, Joueur j) {
        System.out.println("Impossible de placer une tuile ici.");
        if (ligne < 0 || ligne >= plateau.length || colonne < 0 || colonne >= plateau.length || plateau[ligne][colonne] == null || Math.abs(ligne - j.getLigne()) != 1 || Math.abs(colonne - j.getColonne()) != 1) {
            return;
        }
        plateau[ligne][colonne] = tuile;
    }

    /**
     * Affiche le plateau de tuiles.
     */
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

    /**
     * Actualise la position du joueur après le placement d'une tuile.
     * 
     * @param j Le joueur dont la position doit être actualisée.
     */
    public void ActualiserPosJ(Joueur j) {
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
        } else {
            Tuiles nvTuiles = plateau[j.getLigne()][j.getColonne()];
            j.setEntree(nvTuiles.GettabTui()[(nvTuiles.getRotation()*2+interConnection[j.getEntree()])%8]);
        }
    }

    /**
     * Vérifie si le joueur a perdu en sortant du plateau.
     * 
     * @param joueur Le joueur à vérifier.
     * @return true si le joueur a perdu, sinon false.
     */
    public boolean joueurPerdu(Joueur joueur) {
        int ligne = joueur.getLigne();
        int colonne = joueur.getColonne();
    
        return (ligne < 0 || ligne >= plateau.length || colonne < 0 || colonne >= plateau.length);
    }

    /**
     * Réinitialise le plateau de tuiles en le remettant à zéro.
     */
    public void reinitialiserPlateau() {
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                plateau[i][j] = null;
            }
        }
        Joueur joueur1 = new Joueur(3, 4, 2);
        Joueur joueur2 = new Joueur(5, 6, 1);
    }

    
    public static void main(String[] args) {
        PlateauTuiles plateau = new PlateauTuiles(9);
        plateau.afficherPlateau();

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

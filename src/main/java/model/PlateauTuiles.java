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
        if (ligne < 0 || ligne >= plateau.length || colonne < 0 || colonne >= plateau.length ||  plateau[ligne][colonne] != null ){
            System.out.println("Impossible de placer une tuile ici.");
            return;
        }
        plateau[ligne][colonne] = tuile;
        ActualiserPosJ(j);
    }

    /**
     * Affiche le plateau de tuiles.
     */
    public void afficherPlateau() {
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j] == null) {
                    System.out.print("X\t");
                } else {
                    System.out.print("T " + "\t");
                }
            }
            System.out.println("\n");
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
            Chemin tmp=TrouveNVEntre(j.getEntree(),nvTuiles,j);
            if (!tmp.estEmprunte()){
                j.setEntree(tmp);
                tmp.setCouleur("Vert"); //exemple
                LiaisonCheminInvers(tmp,nvTuiles,"Vert");
                //si le chemin n'est pas emprunter || on défini la nouvelle entré , la couleur et on lie le chemin inverse du chemin tmp
            }
            else {
                System.out.println("Vous allez rentrer en collision avec un autre joueur !\nLe joueur a perdu.");
            }
        }
    }
    public Chemin TrouveNVEntre(int ancienPoint,Tuiles nvTuiles,Joueur j){
        return nvTuiles.GettabTui()[(nvTuiles.getRotation()*2+interConnection[j.getEntree()])%8]; //trouve le nouveau chemin a emprunter
    }
    public void LiaisonCheminInvers(Chemin x, Tuiles nvTuiles,String couleur){
        nvTuiles.GettabTui()[x.getPointSortie()].setCouleur(couleur); //défini la couleur du chemin inverse
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
    }

    
    public static void main(String[] args) {

        TuileAdapt tuiles = new TuileAdapt();
        Tuiles tuile1 = tuiles.getTuile(1);
        tuile1.afficherTuileNaive(); 

        PlateauTuiles plateau = new PlateauTuiles(7);
        plateau.afficherPlateau();

        System.out.println("Entrez les coordonnées de départ (ligne colonne) :");
        int ligneDepart = 0;
        int colonneDepart = -1;
        Joueur joueur1 = new Joueur(ligneDepart, colonneDepart, 2, "Lili");

        System.out.println("Coordonnées de départ du joueur 1 : " + joueur1.getLigne() + ", " + joueur1.getColonne());

        System.out.println("Entrez les coordonnées de la tuile à placer (ligne colonne) :");
        int ligneTuile = 0;
        int colonneTuile = 0;
        
        // Ici vous devez choisir quel joueur va placer la tuile, puis appeler la méthode placerTuile() en conséquence
        plateau.placerTuile(ligneTuile, colonneTuile, tuile1, joueur1); // Par exemple, placer la tuile pour le joueur 1

        plateau.afficherPlateau();

        System.out.println("Coordonnées du joueur 1 après placement de tuile : " + joueur1.getLigne() + ", " + joueur1.getColonne() + ". Sorti : "+joueur1.getEntree());

    }
}

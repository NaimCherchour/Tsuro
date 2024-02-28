package main.java.model;
import java.util.Scanner;

import main.java.model.Joueur;

import main.java.model.Tuile.Chemin;

/**
 * La classe PlateauTuiles représente le plateau de jeu composé de tuiles.
 */
public class PlateauTuiles {
    private Tuile[][] plateau; // Matrice représentant le plateau de tuiles.
    public int[] interConnection = {5,4,7,6,1,0,3,2}; // Pour chaque index, correspond à la nouvelle index dans la tuile suivante.

    /**
     * Constructeur de la classe PlateauTuiles.
     * 
     * @param taille La taille du plateau de jeu.
     */
    public PlateauTuiles(int taille) {
        plateau = new Tuile[taille][taille];
    }

    /**
     * Place une tuile sur le plateau à la position spécifiée et vérifie si le joueur perd.
     * 
     * @param ligne La ligne où placer la tuile.
     * @param colonne La colonne où placer la tuile.
     * @param tuile La tuile à placer.
     * @param j Le joueur associé à la tuile.
     */
    public void placerTuile(int ligne, int colonne, Tuile tuile, Joueur j) {
        if (ligne < 0 || ligne >= plateau.length || colonne < 0 || colonne >= plateau.length ||  plateau[ligne][colonne] != null ){
            System.out.println("Impossible de placer une tuile ici.");
            return;
        }
        plateau[ligne][colonne] = tuile;
    }
    public boolean isEmpty(int ligne, int colonne){
        return plateau[ligne][colonne]==null;
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

    private boolean coordonneesValides(int ligne, int colonne) {
        return (ligne >= 0 && ligne < plateau.length && colonne >= 0 && colonne < plateau[0].length);
    }

    /**
     * Actualise la position du joueur après le placement d'une tuile.
     * 
     * @param j Le joueur dont la position doit être actualisée.
     */

     public void ActualiserPosJ(Joueur j) {
        int entree = j.getEntree();
        int nouvelleLigne = j.getLigne();
        int nouvelleColonne = j.getColonne();
    
        // Déterminer les nouvelles coordonnées du joueur en fonction de son point d'entrée
        if (entree < 2) {
            nouvelleLigne--;
        } else if (entree > 1 && entree < 4) {
            nouvelleColonne++;
        } else if (entree > 3 && entree < 6) {
            nouvelleLigne++;
        } else if (entree > 5 && entree < 8) {
            nouvelleColonne--;
        }
    
        // Vérifier si les nouvelles coordonnées sont valides
        if (coordonneesValides(nouvelleLigne, nouvelleColonne)) {
            // Vérifier si une tuile est présente aux nouvelles coordonnées
            if (!isEmpty(nouvelleLigne, nouvelleColonne)) {
                j.setLigne(nouvelleLigne);
                j.setColonne(nouvelleColonne);
                Tuile nvTuiles = plateau[j.getLigne()][j.getColonne()];
                Chemin tmp=TrouveNVEntre(j.getEntree(),nvTuiles,j);
                System.out.println("sortie du chemin :"+(tmp.getPointSortie()+2*nvTuiles.getRotation())%8);
                if (tmp.estEmprunte()){
                    System.out.println("Vous allez rentrer en collision avec un autre joueur !\nLe joueur a perdu.");
                }
                else {
                    //j.setEntree(tmp);
                    j.setEntree(tmp,nvTuiles);
                    tmp.setCouleur(j.getCouleur());
                    LiaisonCheminInvers(tmp,nvTuiles,j.getCouleur());
                    ActualiserPosJ(j);
                }
            } else {
                System.out.println("Déplacement impossible : aucune tuile dans la direction spécifiée.");
            }
        } else {
            System.out.println("Déplacement impossible : le joueur est sorti du plateau.");
        }
    }

    public Chemin TrouveNVEntre(int ancienPoint,Tuile nvTuiles,Joueur j){
        return nvTuiles.getTableauChemin()[interConnection[(j.getEntree()-(2*nvTuiles.getRotation()))%8]]; //trouve le nouveau chemin a emprunter
    }
    public void LiaisonCheminInvers(Chemin x, Tuile nvTuiles,Joueur.Couleur couleur){
        nvTuiles.getTableauChemin()[x.getPointSortie()].setCouleur(couleur); //défini la couleur du chemin inverse
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
        Tuile tuile1 = tuiles.getTuile(1);
        tuile1.afficherTuileNaive();
        Tuile tuile2 = tuiles.getTuile(2);
        tuile2.afficherTuileNaive(); 
    

        PlateauTuiles plateau = new PlateauTuiles(7);
        plateau.afficherPlateau();

        System.out.println("Entrez les coordonnées de départ (ligne colonne) :");
        int ligneDepart = 0;
        int colonneDepart = -1;
        Joueur joueur1 = new Joueur(ligneDepart, colonneDepart, 2, "Lili");
        System.out.println(joueur1.getCouleur());

        System.out.println("Coordonnées de départ du joueur 1 : " + joueur1.getLigne() + ", " + joueur1.getColonne());

        System.out.println("Entrez les coordonnées de la tuile à placer (ligne colonne) :");
        int ligneTuile = 0;
        int colonneTuile = 0;
        
        // Ici vous devez choisir quel joueur va placer la tuile, puis appeler la méthode placerTuile() en conséquence
        plateau.placerTuile(ligneTuile, colonneTuile, tuile1, joueur1); // Par exemple, placer la tuile pour le joueur 1
        plateau.placerTuile(ligneTuile, colonneTuile+1, tuile2, joueur1); // Par exemple, placer la tuile pour le joueur 1
        plateau.ActualiserPosJ(joueur1);

        plateau.afficherPlateau();

        System.out.println("Coordonnées du joueur 1 après placement de tuile : " + joueur1.getLigne() + ", " + joueur1.getColonne() + ". Sorti : "+joueur1.getEntree());


        //les colisions entre joueur ne sont pas encore gerer, un joueuer perd uniquement lorsqu'il rentre dans une chemin qui a déja été occupé ou quand il sort du tableau
    }
}

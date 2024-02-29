package main.java.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.java.model.Joueur.Couleur;

public class Tuile{

    private static int dernierIdAttribue = 0;
    private int id;
    private static final int TAILLE_DU_TABLEAU = 8;
    private Chemin[] tableauChemins;
    private int rotation = 0;

    public Chemin[] getTableauChemin(){
        return tableauChemins;
    }

    public int getRotation(){
        return rotation;
    }
    public void setRotation(int r){
        rotation=r;
    }


    /**
     * Le Tableau de chemin contient à chaque indice le point de sortie ,la couleur et le joueur 
     */
    public Tuile(int id, int[] tableauEntreeSortie) {
        this.id = id;
        tableauChemins = new Chemin[TAILLE_DU_TABLEAU];
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            tableauChemins[i] = new Chemin(tableauEntreeSortie[i]); // Crée un nouvel objet Chemin pour chaque élément du tableau
        }
        this.rotation = 0; // rotation à 0 par défaut
    }
    /**
     * Affiche les connexions actuelles de la tuile.
     */
    public void afficherTuile() {
        System.out.println("Tuile ID: " + id);
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            Chemin chemin = tableauChemins[i];
            if (chemin.getPointSortie() != -1) {
                System.out.println("De " + i + " à " + chemin.getPointSortie() +" (Couleur: " + chemin.getCouleur() +
                        ".");
            }
        }
    }

    public void afficherTuileNaive() {
        System.out.println("Tuile ID: " + id);
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            Chemin chemin = tableauChemins[i];
            if (chemin.getPointSortie() != -1) {
                System.out.println("De " + i + " à " + chemin.getPointSortie());
            }
        }
    }

    public class Chemin {
    
        private int pointSortie;
        private Joueur.Couleur couleur;
    
        /**
         * @param pointSortie represente le point de sortie du chemin du joueur
         * @param couleur represente la couleur attribuer au chemin du joueur 
         */
        public Chemin(int pointSortie){
            this.pointSortie = pointSortie;
            couleur=Couleur.BLACK;
        }
    
        public int getPointSortie() {
            return pointSortie;
        }
    
        public Joueur.Couleur getCouleur() {
            return couleur;
        }
        public void setPointSortie(int pointSortie) {
            this.pointSortie = pointSortie;
        }
    
        public void setCouleur(Joueur.Couleur couleur) {
            this.couleur = couleur;
        }
    
        public boolean estEmprunte() {
            return this.couleur != Couleur.BLACK;
        }

        public void marquerCheminVisite(Joueur.Couleur couleur) {
            setCouleur(couleur);;
            
            int tmp=tableauChemins[getPointSortie()].getPointSortie();
            tableauChemins[tmp].setCouleur(couleur);// on doit aussi changer la couleur pour  tableauChemins[tmp] afin de gerer les doublons 
        }

        public boolean cheminEstEmprunte(int indiceChemin) {
            Chemin chemin = tableauChemins[indiceChemin];
            return chemin.estEmprunte();
        }
    }
    

    public static void main(String[] args) {
        System.out.println();
    }
}


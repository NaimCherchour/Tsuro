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
                        ", Joueur: " + chemin.getJoueur().getPrenom() + ")");
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
        private Joueur joueur;
        private Joueur.Couleur couleur;
    
        /**
         * @param pointSortie represente le point de sortie du chemin du joueur
         * @param couleur represente la couleur attribuer au chemin du joueur 
         * @param joueur represente le joueur
         */
        public Chemin(int pointSortie, Joueur joueur) {
            this.pointSortie = pointSortie;
            this.joueur = joueur;
            if (joueur == null){
                this.couleur = null;
            }
            else {
                this.couleur = joueur.getCouleur();
            }
        }
        public Chemin(int pointSortie){
            this.pointSortie = pointSortie;
            joueur=null;
            couleur=Couleur.NONE;
        }
    
        public int getPointSortie() {
            return pointSortie;
        }
    
        public Joueur.Couleur getCouleur() {
            return couleur;
        }
    
        public Joueur getJoueur() {
            return joueur;
        }
    
        public void setPointSortie(int pointSortie) {
            this.pointSortie = pointSortie;
        }
    
        public void setCouleur(Joueur.Couleur couleur) {
            this.couleur = couleur;
        }
    
        public boolean estEmprunte() {
            return this.couleur != Couleur.NONE;
        }
    }
    

    public static void main(String[] args) {
        System.out.println();
    }
}


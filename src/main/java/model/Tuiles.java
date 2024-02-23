package main.java.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.java.model.Chemin.Couleur;

public class Tuiles{

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
    public Tuiles() {
        this.id = ++dernierIdAttribue;
        tableauChemins = new Chemin[TAILLE_DU_TABLEAU];
        // Initialiser la tuile avec des valeurs par défaut pour éviter les chemins non définis
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            tableauChemins[i] = new Chemin(-1, null, null); // -1 indique qu'aucun chemin n'est encore défini
        }
    }
    public Tuiles(int id, int[] tableauEntreeSortie) {
        this.id = id;
        tableauChemins = new Chemin[TAILLE_DU_TABLEAU];
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            tableauChemins[i] = new Chemin(i, Couleur.NONE, null); // Crée un nouvel objet Chemin pour chaque élément du tableau
            tableauChemins[i].setPointSortie(tableauEntreeSortie[i]);
        }
        this.rotation = 0; // rotation à 0 par défaut
    }


    /**
     * Connecte deux points sur la tuile.
     * 
     * @param pointA Premier point à connecter.
     * @param pointB Deuxième point à connecter.
     */
    public void connecterPoints(int pointA, int pointB,Couleur couleur, Joueur joueur) {
        if (pointA < 0 || pointA >= TAILLE_DU_TABLEAU || pointB < 0 || pointB >= TAILLE_DU_TABLEAU || tableauChemins[pointA] !=null) {
            System.out.println("Indices des points invalides ou déjà connecté. ");
            return;
        }
        tableauChemins[pointA] = new Chemin(pointB, couleur, joueur);
        tableauChemins[pointB] = new Chemin(pointA, couleur, joueur);
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

    public static void main(String[] args) {
        System.out.println();
        Tuiles a=new Tuiles();
        Joueur j1=new Joueur(1, 2, 2,"toto");
        a.connecterPoints(0, 1, Couleur.BLUE, j1);
        a.afficherTuile();
    }
}


package main.java.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Tuiles{

    private static int dernierIdAttribue = 0;
    private int id;
    private static final int TAILLE_DU_TABLEAU = 8;
    private int[] tableauTuiles;
    private int rotation = 0;

    public int[] GettabTui(){
        return tableauTuiles;
    }
    public int getRotation(){
        return rotation;
    }

    public Tuiles() {
        this.id = ++dernierIdAttribue;
        tableauTuiles = new int[TAILLE_DU_TABLEAU];
        // Initialiser la tuile avec des valeurs par défaut pour éviter les chemins non définis
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            tableauTuiles[i] = -1; // -1 indique qu'aucun chemin n'est encore défini
        }
    }
    /**
     * Connecte deux points sur la tuile.
     * 
     * @param pointA Premier point à connecter.
     * @param pointB Deuxième point à connecter.
     */
    public void connecterPoints(int pointA, int pointB) {
        if (pointA < 0 || pointA >= TAILLE_DU_TABLEAU || pointB < 0 || pointB >= TAILLE_DU_TABLEAU) {
            System.out.println("Indices des points invalides.");
            return;
        }
        tableauTuiles[pointA] = pointB;
        tableauTuiles[pointB] = pointA;
    }
    /**
     * Affiche les connexions actuelles de la tuile.
     */
    public void afficherTuile() {
        System.out.println("Tuile ID: " + id);
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            if (tableauTuiles[i] != -1) {
                System.out.println("De " + i + " à " + tableauTuiles[i]);
            }
        }
    }

    /**
     * la tuile est un chemin  valide si :
     * -Chaque point d'entrée doit être connecté à un point de sortie unique,
     * et chaque point de sortie doit être connecté à un point d'entrée unique
     * -Chaque point d'entrée/sortie doit être connecté à au moins un autre point d'entrée/sortie
     */
    public boolean estCheminValide(){
        boolean [] pointsVisites=new boolean[TAILLE_DU_TABLEAU];
        int pointCourant=0;// c'est notre point de depart
        int pointRestant=TAILLE_DU_TABLEAU-1;

        while(pointRestant>0){
            pointsVisites[pointCourant]=true;
            int pointSuivant = tableauTuiles[pointCourant];

            // la confition verifie si le pointSuivant est deja visité ou si aucune connexions à été definie 
            if (pointSuivant == -1 || pointsVisites[pointSuivant]) {
                return false;
            }

            pointCourant = pointSuivant;
            pointRestant--;
    
        }

        return true;
    }
    public static void main(String[] args) {
        System.out.println();
    }
}


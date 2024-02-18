package main.java.model;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Joueur représente un joueur dans le jeu.
 */
public class Joueur {

    private int ligne;           // La ligne actuelle où se trouve le joueur sur le plateau.
    private int colonne;         // La colonne actuelle où se trouve le joueur sur le plateau.
    private int PointEntree;     // Le point d'entrée du joueur sur la tuile.

    /**
     * Constructeur de la classe Joueur.
     * 
     * @param ligne La ligne initiale du joueur sur le plateau.
     * @param colonne La colonne initiale du joueur sur le plateau.
     * @param PointEntree Le point d'entrée initial du joueur sur la tuile.
     */
    public Joueur(int ligne, int colonne, int PointEntree) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.PointEntree = PointEntree;
    }

    /**
     * Obtient la ligne actuelle du joueur.
     * 
     * @return La ligne actuelle du joueur.
     */
    public int getLigne() {
        return ligne;
    }

    /**
     * Modifie la ligne du joueur.
     * 
     * @param a La nouvelle valeur de la ligne du joueur.
     */
    public void setLigne(int a) {
        ligne = a;
    }

    /**
     * Obtient la colonne actuelle du joueur.
     * 
     * @return La colonne actuelle du joueur.
     */
    public int getColonne() {
        return colonne;
    }

    /**
     * Modifie la colonne du joueur.
     * 
     * @param a La nouvelle valeur de la colonne du joueur.
     */
    public void setColonne(int a) {
        colonne = a;
    }

    /**
     * Obtient le point d'entrée du joueur sur la tuile.
     * 
     * @return Le point d'entrée du joueur sur la tuile.
     */
    public int getEntree() {
        return PointEntree;
    }

    /**
     * Modifie le point d'entrée du joueur sur la tuile.
     * 
     * @param n Le nouveau point d'entrée du joueur sur la tuile.
     */
    public void setEntree(int n) {
        PointEntree = n;
    }

    
    public static void main(String[] args) {
        // Exemple d'utilisation de la classe Joueur
        Joueur joueur1 = new Joueur(3, 4, 2);
    }
}

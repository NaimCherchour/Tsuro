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
    private String prenom;

    /**
     * Constructeur de la classe Joueur.
     * 
     * @param ligne La ligne initiale du joueur sur le plateau.
     * @param colonne La colonne initiale du joueur sur le plateau.
     * @param PointEntree Le point d'entrée initial du joueur sur la tuile.
     * @param prenom Le prenom du joueur.
     */
    public Joueur(int ligne, int colonne, int PointEntree,String prenom) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.PointEntree = PointEntree;
        this.prenom=prenom;
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

    public String getPrenom(){
        return prenom;
    }

    /**
     * Modifie le point d'entrée du joueur sur la tuile.
     * 
     * @param chemin Le nouveau point d'entrée du joueur sur la tuile.
     */
    public void setEntree(Chemin chemin) {
        PointEntree = chemin.getPointSortie();
    }
    public static void main(String[] args) {
        // Exemple d'utilisation de la classe Joueur
        Joueur joueur1 = new Joueur(3, 4, 2,"max");
    }
}

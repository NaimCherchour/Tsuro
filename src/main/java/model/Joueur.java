package main.java.model;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Tuile.Chemin;


/**
 * La classe Joueur représente un joueur dans le jeu.
 */
public class Joueur {

    private int ligne;
    private int colonne;
    private int PointEntree;
    private String prenom;
    private Couleur couleur;
    private static int indexCouleur = 1 ;

    public enum Couleur {
        BLACK, ROUGE, VERT, BLEU, JAUNE // Ajoutez d'autres couleurs au besoin
    }

    /**
     * Constructeur de la classe Joueur.
     * 
     * @param ligne La ligne initiale du joueur sur le plateau.
     * @param colonne La colonne initiale du joueur sur le plateau.
     * @param PointEntree Le point d'entrée initial du joueur sur la tuile.
     * @param prenom Le prenom du joueur.
     */
    public Joueur(int ligne, int colonne, int PointEntree, String prenom) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.PointEntree = PointEntree;
        this.prenom = prenom;
        this.couleur = getNextCouleur();
        Joueur.indexCouleur+=1;
    }

    private Couleur getNextCouleur() {
        Couleur[] couleurs = Couleur.values();
        if (Joueur.indexCouleur>=couleurs.length){
            System.out.println("impossible d'ajouter d'autres joueurs");
            return null;
        }
        return couleurs[Joueur.indexCouleur];
    }

    /**
     * Obtient la ligne actuelle du joueur.
     * 
     * @return La ligne actuelle du joueur.
     */
    public void setPointEntree(int pointEntree) {
        PointEntree = pointEntree;
    }

    public int getLigne() {
        return ligne;
    }
    public Joueur.Couleur getCouleur() {
        return couleur;
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
    public void setEntree(Chemin chemin,Tuile tmp) {
        PointEntree = (chemin.getPointSortie()+2*tmp.getRotation())%8;
    }

    /**
     * Déplace le joueur sur le plateau en fonction de la tuile sur laquelle il se trouve.
     * 
     * @param plateau Le plateau de jeu.
     */
    public void deplacerJoueur(PlateauTuiles plateau) {
        plateau.ActualiserPosJ(this);
    }

    public static void main(String[] args) {
        // Exemple d'utilisation de la classe Joueur
        Joueur joueur1 = new Joueur(3, 4, 2, "max");
    }
}

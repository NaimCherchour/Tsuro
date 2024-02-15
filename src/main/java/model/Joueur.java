package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private int ligne;
    private int colonne;
    private int PointEntree; 

    public Joueur(int ligne, int colonne, int PointEntree) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.PointEntree = PointEntree;
    }

    public int getLigne() {
        return ligne;
    }
    
    public int getColonne() {
        return colonne;
    }
    public int getEntree() {
        return PointEntree;
    }
    public void setEntree(int n){
        PointEntree=n;
    }

    public static void main(String[] args) {
        // Exemple d'utilisation de la classe Joueur
        Joueur joueur1 = new Joueur(3, 4, 2);
    }
}

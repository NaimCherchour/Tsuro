package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private int ligne;
    private int colonne;
    private int entreeSelectionnee;
    private List<Integer> numeros;

    public Joueur(int ligne, int colonne, int entreeSelectionnee) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.entreeSelectionnee = entreeSelectionnee;
        this.numeros = new ArrayList<>();
    }


    public void setEntreeSelectionnee(int entreeSelectionnee) {
        this.entreeSelectionnee = entreeSelectionnee;
    }

    public List<Integer> getchemin() {
        return numeros;
    }

    public static void main(String[] args) {
        // Exemple d'utilisation de la classe Joueur
        Joueur joueur1 = new Joueur(3, 4, 2);
    }
}

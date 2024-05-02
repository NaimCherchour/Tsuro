package main.java.menu;

import java.io.Serializable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Profile implements Serializable {
    private static final long serialVersionUID = 1L; // Pour la sérialisation

    private String nom;
    private int score;

    public Profile(String nom, int score) {
        this.nom = nom;
        this.score = score;
    }

    // Constructeur prenant uniquement le nom
    public Profile(String nom) {
        this.nom = nom;
        this.score = 0; // Score initialisé à zéro par défaut
    }

    // Accesseurs (getters et setters)
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Méthode pour saisir le nom du joueur depuis la console et l'écrire dans un fichier texte
    public static Profile saisirNomJoueur() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Veuillez saisir votre nom : ");
        String nom = scanner.nextLine();

        // Écriture du nom dans un fichier texte
        try {
            FileWriter writer = new FileWriter("noms.txt", true); // Le fichier "noms.txt" sera créé s'il n'existe pas
            writer.write(nom + "\n"); // Écriture du nom suivi d'un saut de ligne
            writer.close(); // Fermeture du fichier après écriture
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans le fichier texte : " + e.getMessage());
        }

        return new Profile(nom);
    }


}

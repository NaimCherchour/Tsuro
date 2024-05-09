package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class TuileMagique extends Tuile {
    private int compteurCoups; // Compteur pour suivre le nombre de coups joués
    private int seuil; // Seuil pour générer une tuile magique
    private List<Joueur> joueurs; // Liste des joueurs

    /**
     * Constructeur de la classe TuileMagique.
     * @param seuil Le seuil après lequel une tuile magique est générée.
     */
    public TuileMagique(int id, int seuil) {
        super(id);
        this.seuil = seuil;
        this.compteurCoups = 0;
        this.joueurs = new ArrayList<>();
    }

    /**
     * Ajoute un joueur à la liste des joueurs.
     * @param joueur Le joueur à ajouter.
     */
    public void addJoueur(Joueur joueur) {
        joueurs.add(joueur);
    }

    /**
     * Gère les actions lorsqu'un joueur joue un coup.
     * Incrémente le compteur de coups et génère une tuile magique si le seuil est atteint.
     */
    public void jouerCoup() {
        compteurCoups++;
        if (compteurCoups % seuil == 0) {
            genererTuileMagique();
        }
    }

    /**
     * Génère une tuile magique, permettant aux joueurs vivants de rejouer.
     */
    private void genererTuileMagique() {
        System.out.println("Une tuile magique a été générée!");
        for (Joueur joueur : joueurs) {
            if (joueur.isAlive()) {
                System.out.println(joueur.getPrenom() + " peut rejouer!");
                joueur.setCompteur(0); // Réinitialise le compteur de coups pour ce joueur
            }
        }
    }
}

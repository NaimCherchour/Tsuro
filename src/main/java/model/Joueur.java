package main.java.model;

import main.java.model.Tuile.Chemin;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Random;


/**
 * La classe Joueur représente un joueur dans le jeu.
 */

public class Joueur {
    private int ligne; // La ligne actuelle ( de la tuile ) du joueur sur le plateau
    private int colonne; // La colonne actuelle ( de la tuile ) du joueur sur le plateau
    private int PointEntree; // Le point d'entrée du joueur sur le plateau et SA POSITION ACTUELLE dans le plateau par rapport à la tuile
    private final String prenom; // Le prénom du joueur
    private final Couleur couleur; // La couleur du joueur; c'est ce qui identifie le joueur pour la tuile
    private static int indexCouleur = Couleur.NOIR.ordinal() + 1; //La valeur de indexCouleur est initialisée à Couleur.NOIR.ordinal() et on fait +1 pour obtenir les couleurs suivantes
    private int compteur;
    private boolean Alive = true; // Le joueur est-il encore en jeu ?

    public enum Couleur {
        NOIR,ROUGE,BLEU,VERT,JAUNE,ORANGE,ROSE,CYAN,VIOLET
    }

    /**
     * Constructeur de la classe Joueur.
     * @param prenom Le prenom du joueur.
     * @param joueurs La liste des joueurs déjà présents/crées
     */

    public Joueur( String prenom, List<Joueur> joueurs) {
        this.PointEntree = calculPointDepart();
        this.ligne = calculLigneDepart(PointEntree);
        this.colonne = calculColonneDepart(PointEntree);
        // Générer une nouvelle position tant qu'elle est déjà occupée par un autre joueur
        while (positionOccupee(this.ligne, this.colonne,joueurs)) {
            this.PointEntree = calculPointDepart();
            this.ligne = calculLigneDepart(PointEntree);
            this.colonne = calculColonneDepart(PointEntree);
        }
        this.prenom = prenom;
        System.out.println(prenom+ " " + indexCouleur);
        this.couleur = Couleur.values()[indexCouleur];
        Joueur.indexCouleur+=1;
        this.compteur = 0;
    }


    /**
     * Vérifie si la position donnée est déjà occupée par un autre joueur.
     * J'ai retiré le point d'entree car on évite d'avoir 2 joueurs dans la même tuile
     * @param ligne
     * @param colonne
     * @param joueurs
     * @return
     */
    private boolean positionOccupee(int ligne, int colonne,List<Joueur> joueurs) {
        for (Joueur joueur : joueurs) {
            if ( joueur.getLigne() == ligne && joueur.getColonne() == colonne ) {
                return true;
            }
        }
        return false;
    }

    // Méthode pour déterminer le point d'entrée du joueur dans la première tuile
    private int calculPointDepart(){
        Random random = new Random();
        return random.nextInt(8);
    }

    private int calculLigneDepart(int pointDepart){
        if (pointDepart == 0 || pointDepart ==1){
            return 0;
        }else if(pointDepart == 4 || pointDepart ==5){
            return 5;
        }else{
            Random random = new Random();
            return random.nextInt(6);
        }
    }

    private int calculColonneDepart(int pointDepart){
        if (pointDepart == 6 || pointDepart == 7){
            return 0;
        }else if(pointDepart == 2 || pointDepart ==3){
            return 5;
        }else{
            Random random = new Random();
            return random.nextInt(6);
        }
    }

    public boolean isAlive(){
        return Alive;
    }
    public void setAlive(boolean x){
        Alive = x;
    }
    public int getCompteur() {
        return compteur;
    }
    public void incrementerCompteur() {
        compteur++;
    }

    /**
     * Obtient le point d'entrée du joueur sur la tuile.
     * @return Le point d'entrée du joueur sur la tuile.
     */
    public int getPointEntree() {
        return PointEntree;
    }

    public void setPointEntree(int pointEntree) {
        PointEntree = pointEntree;
    }

    /**
     * Obtient la ligne actuelle du joueur.
     * @return La ligne actuelle du joueur.
     */
    public int getLigne() {
        return ligne;
    }

    /**
     * Obtient la couleur du joueur.
     * @return La couleur du joueur.
     */
    public Joueur.Couleur getCouleur() {
        return couleur;
    }

    /**
     * Modifie la ligne du joueur.
     * @param a La nouvelle valeur de la ligne du joueur.
     */
    public void setLigne(int a) {
        ligne = a;
    }

    /**
     * Obtient la colonne actuelle du joueur.
     * @return La colonne actuelle du joueur.
     */
    public int getColonne() {
        return colonne;
    }

    /**
     * Modifie la colonne du joueur.
     * @param a La nouvelle valeur de la colonne du joueur.
     */
    public void setColonne(int a) {
        colonne = a;
    }

    public String getPrenom(){
        return prenom;
    }




    /**
     * Trouve le joueur ayant le chemin le plus long parmi une liste de joueurs
     * donnée.
     * @param joueurs Liste des joueurs.
     * @param chemins Liste des chemins.
     * @return Le joueur ayant le chemin le plus long, ou null s'il n'y en a pas.
     */
    public  static Joueur plusLongChemin(List<Joueur> joueurs,List<Chemin>chemins){
        Joueur joueurplusLongChemin=null;
        int maxCheminVisité=0;

        for(Joueur joueur:joueurs){
            int cheminVisitéJoueur=compterPassagesChemin(joueur,chemins);
            if(cheminVisitéJoueur>maxCheminVisité){
                maxCheminVisité=cheminVisitéJoueur;
                joueurplusLongChemin=joueur;
            }
        }
        return joueurplusLongChemin;
    }

    /**
     * Compte le nombre de passages sur les chemins d'un joueur donné.
     * @param joueur Joueur concerné.
     * @return Le nombre de passages du joueur sur les chemins.
     */

    public static int compterPassagesChemin(Joueur joueur, List<Chemin> chemins) {
        int compteur = 0;

        for (Chemin chemin : chemins) {
            if (chemin.getCouleur() == joueur.getCouleur()) {
                compteur++;
            }
        }
        return compteur;
    }


}

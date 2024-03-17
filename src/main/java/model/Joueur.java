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
    private int PointEntree; // Le point d'entrée du joueur sur le plateau ( 1ère tuile ) ; est ce la position par rapport au tuile ??
    private final String prenom; // Le prénom du joueur
    private final Couleur couleur; // La couleur du joueur; c'est ce qui identifie le joueur pour la tuile
    private static int indexCouleur = Couleur.NOIR.ordinal() + 1; //La valeur de indexCouleur est initialisée à Couleur.NOIR.ordinal() et on fait +1 pour obtenir les couleurs suivantes
    private Tuile[] deck;

    public enum Couleur {
        NOIR, ROUGE,BLEU,VERT,JAUNE,ORANGE,ROSE
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
        deck = GenereDeckJoueur();
        Joueur.indexCouleur+=1;
    }
    public Tuile[] GenereDeckJoueur(){
        Random random = new Random();
        Tuiles tuiles = new Tuiles();
        Tuile[]ret = new Tuile[3];
        for(int i=0;i<3;i++){
            Tuile t = tuiles.getTuile(random.nextInt(36) + 1);
            ret[i]=t;
        }
        return ret;
    }
    public Tuile getTuileJoueur(int i){
        if (i>=0 && i<3){
            return deck[i];
        }
        return null;
    }
    public void supprimerTuile(int i){
        Random random = new Random();
        Tuiles tuiles = new Tuiles();
        Tuile t = tuiles.getTuile(random.nextInt(36) + 1);
        deck[i]=t;

    }

    private Couleur getNextCouleur() {
        Couleur[] couleurs = Couleur.values();

        if (Joueur.indexCouleur>=couleurs.length){
            System.out.println("impossible d'ajouter d'autres joueurs");
            return Couleur.NOIR;
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

    /**
     * Obtient le point d'entrée du joueur sur la tuile.
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
     * @param chemin Le nouveau point d'entrée du joueur sur la tuile.
     */
    public void setEntree(Chemin chemin,Tuile tmp) {
        // on est censé changer le point d'entrée du joueur sur la tuile avec la nouvelle sortie
        PointEntree = (chemin.getPointSortie()+2*tmp.getRotation())%8;
    }


    /**
     * Déplace le joueur sur le plateau en fonction de la tuile sur laquelle il se trouve.
     * 
     * @param plateau Le plateau de jeu.
     */
    public void deplacerJoueur(PlateauTuiles plateau) {
        plateau.actualiserPosJ(this);
    }

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
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
     * @param tableauChemins Liste des chemins.
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

    public static void main(String[] args) {
        // Exemple d'utilisation de la classe Joueur
        Joueur joueur1 = new Joueur(3, 4, 2, "max");
    }
}

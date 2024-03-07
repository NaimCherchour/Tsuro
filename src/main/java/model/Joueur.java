package main.java.model;

import main.java.model.Tuile.Chemin;


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
        Joueur.indexCouleur+=1;
    }

    private Couleur getNextCouleur() {
        Couleur[] couleurs = Couleur.values();

        if (Joueur.indexCouleur>=couleurs.length){
            System.out.println("impossible d'ajouter d'autres joueurs");
            return Couleur.NOIR;
        }
        return couleurs[Joueur.indexCouleur];
    }

    public PlateauTuiles.Direction getDirectionEntree() {
        // Récupérer la direction du joueur en fonction du point d'entrée du joueur
        switch (PointEntree) {
            case 0, 1 -> {
                return PlateauTuiles.Direction.NORD;
            }
            case 2, 3 -> {
                return PlateauTuiles.Direction.EST;
            }
            case 4, 5 -> {
                return PlateauTuiles.Direction.SUD;
            }
            case 6, 7 -> {
                return PlateauTuiles.Direction.OUEST;
            }
            default -> {
                System.out.println("Point d'entrée invalide pour le joueur !");
                return null;
            }
        }
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

    public static void main(String[] args) {
        // Exemple d'utilisation de la classe Joueur
        Joueur joueur1 = new Joueur(3, 4, 2, "max");
    }
}

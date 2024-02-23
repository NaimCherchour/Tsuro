package main.java.model;

public class Chemin {

    enum Couleur{
        NONE,RED,BLUE
    }

    private int pointSortie;
    private Joueur joueur;
    private Couleur couleur;

    /**
     * @param pointSortie represente le point de sortie du chemin du joueur
     * @param couleur represente la couleur attribuer au chemin du joueur 
     * @param joueur represente le joueur
     */
    public Chemin(int pointSortie,Couleur couleur, Joueur joueur) {
        this.pointSortie = pointSortie;
        this.couleur = couleur;
        this.joueur = joueur;
    }

    public int getPointSortie() {
        return pointSortie;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setPointSortie(int pointSortie) {
        this.pointSortie = pointSortie;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public boolean estEmprunte() {
        return this.couleur ==null;
    }
}

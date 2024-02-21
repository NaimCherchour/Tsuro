package main.java.model;

public class Chemin {
    private int pointSortie;
    private String couleur;
    private Joueur joueur;


    /**
     * @param pointSortie represente le point de sortie du chemin du joueur
     * @param couleur represente la couleur attribuer au chemin du joueur 
     * @param joueur represente le joueur
     */
    public Chemin(int pointSortie, String couleur, Joueur joueur) {
        this.pointSortie = pointSortie;
        this.couleur = couleur;
        this.joueur = joueur;
    }

    public int getPointSortie() {
        return pointSortie;
    }

    public String getCouleur() {
        return couleur;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setPointSortie(int pointSortie) {
        this.pointSortie = pointSortie;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
}

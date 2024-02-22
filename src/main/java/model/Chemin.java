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
    public Chemin(){
        this.pointSortie = -1;
        this.couleur = null;
        this.joueur = null;
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

    /**
     * Vérifie si le chemin a déjà été emprunté par un joueur.
     * 
     * @return true si le chemin a été emprunté par un autre joueur, false sinon, sachant que les joueurs sont représentés par leurs couleurs respectives.
     */
    public boolean estEmprunte() {
        return this.joueur != null;
    }

    /**
     * Vérifie si le chemin a été emprunté par un joueur spécifique basé sur la couleur.
     * 
     * @param couleurJoueur La couleur du joueur à vérifier.
     * @return true si le chemin a été emprunté par le joueur avec la couleur spécifiée, false sinon.
     */
    public boolean estEmpruntePar(String couleurJoueur) {
        return this.joueur != null && this.couleur.equals(couleurJoueur);
    }

}

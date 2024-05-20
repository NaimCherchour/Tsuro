package main.java.model;

public interface ReadOnlyJoueur {
    public int getCompteur();
    public int getPointEntree();
    public int getLigne();
    public Joueur.Couleur getCouleur();
    public int getColonne();
    public String getPrenom();
}

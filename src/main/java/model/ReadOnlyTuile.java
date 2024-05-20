package main.java.model;

public interface ReadOnlyTuile {

    public int getId ();
    public int getRotation();

    public Tuile.Chemin[] getTableauChemins();

    public int getPointSortieAvecRot(int ind, int rotation);
    public int getPointSortieAvecRot(int ind);

    public interface ReadOnlyChemin {
        public int getPointSortie();
        public Joueur.Couleur getCouleur();
    }
}

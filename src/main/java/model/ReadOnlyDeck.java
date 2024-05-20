package main.java.model;

public interface ReadOnlyDeck {

    /** Le seul getter dont on a besoin pour le deck ;
     * @param i la Tuile sélectionné du deck
     */
    public ReadOnlyTuile getTuile(int i) ;

    public ReadOnlyTuile[] getSideTuiles();


}

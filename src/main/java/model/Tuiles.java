package main.java.model;

public class Tuiles{


    private static int dernierIdAttribue = 0;
    private int id;
    private static final int TAILLE_DU_TABLEAU = 8;
    private int[] tableauTuiles;

    public Tuiles() {
        this.id = ++dernierIdAttribue;
        tableauTuiles = new int[TAILLE_DU_TABLEAU];
    }

}
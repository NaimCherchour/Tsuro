package main.java.controller;


import main.java.model.PlateauTuiles;
import main.java.model.Tuile;

public class PlateauController {
    private PlateauTuiles plateau;

    public PlateauController(PlateauTuiles plateau) {
        this.plateau = plateau;
    }

    public void placerTuile(int ligne, int colonne, Tuile tuile) {
        plateau.setTuile(ligne, colonne, tuile);
    }

}

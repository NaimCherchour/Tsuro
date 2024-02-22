package main.java.model;

import java.util.HashMap;

public class TuileAdapt {
    private HashMap<Integer, Tuiles> tuiles; // Les tuiles sont stockées dans une map pour faciliter l'accès
    // J'ai utilisé le hashMap pour stocker les tuiles car il permet de stocker les tuiles avec un id unique et de les retrouver facilement

    public TuileAdapt() {
        tuiles = new HashMap<>(); // Initialiser la map
        initialiserTuiles(); // Initialiser les tuiles
    }

    private void initialiserTuiles() {
        // Initilisation des tuiles avec leurs connexions Naivement avec les 35 modèles de tuiles
        // rotation à 0
        tuiles.put(1, new Tuiles(1, new int[]{5, 6, 4, 7, 2, 0, 1, 3}));
        tuiles.put(2, new Tuiles(2, new int[]{1, 0, 4, 7, 2, 6, 5, 3}));
        tuiles.put(3, new Tuiles(3, new int[]{1, 0, 4, 6, 2, 7, 3, 5}));
        tuiles.put(4, new Tuiles(4, new int[]{2, 5, 0, 7, 6, 1, 4, 3}));
        tuiles.put(5, new Tuiles(5, new int[]{4, 2, 1, 6, 0, 7, 3, 5}));
        tuiles.put(6, new Tuiles(6, new int[]{1, 0, 5, 7, 6, 2, 4, 3}));
        tuiles.put(7, new Tuiles(7, new int[]{2, 4, 0, 6, 1, 7, 3, 5}));
        tuiles.put(8, new Tuiles(8, new int[]{4, 7, 5, 6, 0, 2, 3, 1}));
        tuiles.put(9, new Tuiles(9, new int[]{1, 0, 7, 6, 5, 4, 3, 2}));
        tuiles.put(10, new Tuiles(10, new int[]{4, 5, 6, 7, 0, 1, 2, 3}));
        tuiles.put(11, new Tuiles(11, new int[]{7, 2, 1, 4, 3, 6, 5, 0}));
        tuiles.put(12, new Tuiles(12, new int[]{2, 7, 0, 5, 6, 3, 4, 1}));
        tuiles.put(13, new Tuiles(13, new int[]{5, 4, 7, 6, 1, 0, 3, 2}));
        tuiles.put(14, new Tuiles(14, new int[]{3, 2, 1, 0, 7, 6, 5, 4}));
        tuiles.put(15, new Tuiles(15, new int[]{1, 0, 7, 4, 3, 6, 5, 2}));
        tuiles.put(16, new Tuiles(16, new int[]{1, 0, 5, 6, 7, 2, 3, 4}));
        tuiles.put(17, new Tuiles(17, new int[]{3, 5, 6, 0, 7, 1, 2, 4}));
        tuiles.put(18, new Tuiles(18, new int[]{2, 7, 0, 4, 3, 6, 5, 1}));
        tuiles.put(19, new Tuiles(19, new int[]{4, 3, 6, 1, 0, 7, 2, 5}));
        tuiles.put(20, new Tuiles(20, new int[]{3, 7, 4, 0, 2, 6, 5, 1}));
        tuiles.put(21, new Tuiles(21, new int[]{2, 3, 0, 1, 7, 6, 5, 4}));
        tuiles.put(22, new Tuiles(22, new int[]{2, 6, 0, 5, 7, 3, 1, 4}));
        tuiles.put(23, new Tuiles(23, new int[]{1, 0, 6, 4, 3, 7, 2, 5}));
        tuiles.put(24, new Tuiles(24, new int[]{5, 6, 7, 4, 3, 0, 1, 2}));
        tuiles.put(25, new Tuiles(25, new int[]{1, 0, 3, 2, 7, 6, 5, 4}));
        tuiles.put(26, new Tuiles(26, new int[]{1, 0, 6, 7, 5, 4, 2, 3}));
        tuiles.put(27, new Tuiles(27, new int[]{2, 4, 0, 7, 1, 6, 5, 3}));
        tuiles.put(28, new Tuiles(28, new int[]{4, 2, 1, 7, 0, 6, 5, 3}));
        tuiles.put(29, new Tuiles(29, new int[]{1, 0, 3, 2, 5, 4, 7, 6}));
        tuiles.put(30, new Tuiles(30, new int[]{2, 3, 0, 1, 6, 7, 4, 5}));
        tuiles.put(31, new Tuiles(31, new int[]{3, 6, 5, 0, 7, 2, 1, 4}));
        tuiles.put(32, new Tuiles(32, new int[]{1, 0, 6, 5, 7, 3, 2, 4}));
        tuiles.put(33, new Tuiles(33, new int[]{1, 0, 3, 2, 6, 7, 4, 5}));
        tuiles.put(34, new Tuiles(34, new int[]{4, 5, 7, 6, 0, 1, 3, 2}));
        tuiles.put(35, new Tuiles(35, new int[]{1, 0, 7, 5, 6, 3, 4, 2}));
    }

    public Tuiles getTuile(int id) {
        // l'id de la tuile est passé en paramètre et on retourne la tuile correspondante dans la map
        return tuiles.get(id);
    }

    public static void main(String[] args) {
        // A TEST
        TuileAdapt tuiles = new TuileAdapt();
        Tuiles tuile1 = tuiles.getTuile(1);
        tuile1.afficherTuileNaive(); 
    }
}



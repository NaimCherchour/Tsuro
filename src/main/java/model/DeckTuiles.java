package main.java.model;

import java.util.List;
import java.util.Random;

// cette classe permet de générer les tuiles du deck du side Panel latéral
public class DeckTuiles {
    private Tuile[] sideTuiles; // Sauvegarde des 3 tuiles du deck
    private static final int NB_TUILES = 3; // Nombre de tuiles dans le deck
    private List<Tuile> tuiles; // Tous les 35 Modèles du deck

    // Getter
    public Tuile[] getSideTuiles() {
        return sideTuiles;
    }


    // Initialiser le deck à 3 Tuiles
    public DeckTuiles() {
        //TODO: generated deck tiles should be different tiles ?
        this.tuiles = TuilesGenerator.genererToutesLesTuiles();
        sideTuiles = new Tuile[NB_TUILES];
        for (int i = 0; i < NB_TUILES; i++) {
            if ( !tuiles.isEmpty() ){
                // On crée le Deck de 3 tuiles aléatoirement parmi les 35 modèles
                sideTuiles[i] = shuffleTuile();
            }
        }
    }

    /**
     * @param i pour l'indice de la tuile du deck soit 0, 1 ou 2
     * @return La Tuile du deck à placer selon l'indice
     */
    public Tuile getTuile(int i) {
        // retourner la tuile à l'index i pour la placer dans le plateau et la dessiner également dans le sidePanel
        // aussi Il faudra la remplacer avec une autre tuile
        if (i >= 0 && i < NB_TUILES) {
            Tuile tmp = sideTuiles[i];
            sideTuiles[i] = shuffleTuile();
            return tmp;
        }
        return null;
    }

    /**
     * @return Une tuile sélectionnée aléatoirement parmi les tuiles pour le deck ou remplacer
     * une tuile du deck qui est placé sur le Plateau
     */
    public Tuile shuffleTuile() {
        this.tuiles = TuilesGenerator.genererToutesLesTuiles();
        Random random = new Random();
        return tuiles.get(random.nextInt(tuiles.size()));
    }
}


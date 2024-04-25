package main.java.model;

import java.util.List;
import java.util.Random;
import java.util.Collections;

// cette classe permet de générer les tuiles du deck du side Panel latéral
public class DeckTuiles {

    //TODO : Voir si il faut appliquer la Relation Observer/Observable pour le DeckTuiles et le SidePanel
    private Tuile[] sideTuiles; // Sauvegarde des 3 tuiles du deck
    private static final int NB_TUILES = 3; // Nombre de tuiles dans le deck
    private List<Tuile> tuiles; // Tous les 35 Modèles du deck

    // Getter
    public Tuile[] getSideTuiles() {
        return sideTuiles;
    }


    // Initialiser le deck à 3 Tuiles
    public DeckTuiles() {
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
        // On a décidé de remplacer toutes les tuiles du deck au lieu de remplacer une seule
        if (i >= 0 && i < NB_TUILES) {
            Tuile tmp = sideTuiles[i];
            // On remplace toutes les tuiles du deck
            for (int j = 0; j < NB_TUILES; j++) {
                sideTuiles[j] = shuffleTuile();
            }
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
    public Tuile prendreTuile(int index) {
        if (index < 0 || index >= NB_TUILES) return null;
        Tuile tuile = sideTuiles[index];
        if (!tuiles.isEmpty()) {
            sideTuiles[index] = tuiles.remove(0);  // Remplacer la tuile prise par une nouvelle de la liste
        } else {
            sideTuiles[index] = null;  // S'il n'y a plus de tuiles à tirer
        }
        return tuile;
    }
}


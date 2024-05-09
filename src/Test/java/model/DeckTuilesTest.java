package Test.java.model;

import main.java.model.*;

import static org.junit.Assert.*;
import org.junit.Test;


public class DeckTuilesTest {

    @Test
    public void testInitialisationDeck() {
        DeckTuiles deck = new DeckTuiles();
        Tuile[] sideTuiles = deck.getSideTuiles();

        // Vérifier que le deck est initialisé avec trois tuiles non nulles
        assertNotNull(sideTuiles);
        assertEquals(3, sideTuiles.length);
        for (Tuile tuile : sideTuiles) {
            assertNotNull(tuile);
        }
    }

    @Test
    public void testGetTuile() {
        DeckTuiles deck = new DeckTuiles();

        // Obtenir une tuile spécifique du deck
        Tuile tuile = deck.getTuile(0);

        // Vérifier que la tuile retournée n'est pas nulle
        assertNotNull(tuile);
    }

    @Test
    public void testShuffleTuile() {
        DeckTuiles deck = new DeckTuiles();

        // Mélanger les tuiles du deck
        Tuile tuile1 = deck.shuffleTuile();
        Tuile tuile2 = deck.shuffleTuile();

        // Vérifier que les deux tuiles mélangées sont différentes
        assertNotEquals(tuile1, tuile2);
    }

    @Test
    public void testCopy() {
        DeckTuiles deck = new DeckTuiles();
        Tuile[] original = deck.getSideTuiles();

        // Copier le tableau de tuiles du deck
        Tuile[] copie = deck.copy();

        for (int i = 0; i < 3; i++) {
            assertEquals(original[i].getRotation(),copie[i].getRotation() );
            int[] tuileIdentity = toInt(original[i].getTableauChemins()) ;
            int[] cloneIdentity = toInt(copie[i].getTableauChemins());
            assertArrayEquals(tuileIdentity,cloneIdentity);
        }
    }
    private int[] toInt (Tuile.Chemin[] tab){
        int[] tmp = new int[8];
        for (int i = 0; i < 8; i++) {
            tmp[i] = tab[i].getPointSortie();
        }
        return tmp;
    }
}


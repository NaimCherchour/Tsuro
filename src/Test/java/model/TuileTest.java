package Test.java.model;

import static org.junit.Assert.*;

import main.java.model.Tuile;
import main.java.model.Joueur.Couleur;
import org.junit.Test;
import org.junit.Before;

public class TuileTest {
    private Tuile tuile;

    @Before
    public void setUp() {
        // Initialisation de la tuile avant chaque test
        int[] tableauEntreeSortie = {1, 0, 3, 2, 5, 4, 7, 6};
        tuile = new Tuile(1, tableauEntreeSortie);
    }

    @Test
    public void testRotationTuile() {
        assertEquals(0, tuile.getRotation());

        tuile.tournerTuile();
        assertEquals(1, tuile.getRotation());

        // Test de rotation complète
        for (int i = 0; i < 3; i++) {
            tuile.tournerTuile();
        }
        assertEquals(0, tuile.getRotation());
    }

    @Test
    public void testCloneTuile() {
        Tuile clone = tuile.clone();

        // Vérifie si les identifiants sont différents
        assertNotEquals(tuile.getId(), clone.getId());

        // Vérifie si les rotations sont identiques
        assertEquals(tuile.getRotation(), clone.getRotation());

        //Vérifie si les tableaux de chemins sont les mêmes
        int[] tuileIdentity = toInt(tuile.getTableauChemins()) ;
        int[] cloneIdentity = toInt(clone.getTableauChemins());
        assertArrayEquals(tuileIdentity,cloneIdentity);
    }

    private int[] toInt (Tuile.Chemin[] tab){
        int[] tmp = new int[8];
        for (int i = 0; i < 8; i++) {
            tmp[i] = tab[i].getPointSortie();
        }
        return tmp;
    }

    // Test pour voir si la formule de rotation est correcte
    @Test
    public void testGetPointSortieAvecRot() {
        tuile = new Tuile(2, new int[]{1, 0, 4, 7, 2, 6, 5, 3});
        // Test avec rotation à 0
        assertEquals(1, tuile.getPointSortieAvecRot(0));

        // Test avec rotation à 1
        tuile.setRotation(1);
        assertEquals(7, tuile.getPointSortieAvecRot(0));
        assertEquals(5, tuile.getPointSortieAvecRot(1));
        assertEquals(3, tuile.getPointSortieAvecRot(2));


        // Test avec rotation à 2
        tuile.setRotation(2);
        assertEquals(6, tuile.getPointSortieAvecRot(0));
        assertEquals(2, tuile.getPointSortieAvecRot(1));
        assertEquals(1, tuile.getPointSortieAvecRot(2));


        // Test avec rotation à 3
        tuile.setRotation(3);
        assertEquals(2, tuile.getPointSortieAvecRot(0));
        assertEquals(1, tuile.getPointSortieAvecRot(5));
        assertEquals(7, tuile.getPointSortieAvecRot(6));

    }

    @Test
    public void testMarquerCheminVisite() {
        tuile.getTableauChemins()[0].marquerCheminVisite(0, Couleur.ROUGE);
        assertTrue(tuile.getTableauChemins()[0].estEmprunte());
        assertTrue(tuile.getTableauChemins()[1].estEmprunte()); //Chemin doublon
    }

    @Test
    public void testCheminEstEmprunte() {
        tuile = new Tuile(2, new int[]{1, 0, 4, 7, 2, 6, 5, 3});
        assertFalse(tuile.getTableauChemins()[0].estEmprunte());
        assertFalse(tuile.getTableauChemins()[1].estEmprunte());

        // Marquer un chemin visité
        tuile.getTableauChemins()[0].marquerCheminVisite(0, Couleur.ROUGE);

        assertTrue(tuile.getTableauChemins()[0].estEmprunte());
        assertTrue(tuile.getTableauChemins()[1].estEmprunte());
    }
}


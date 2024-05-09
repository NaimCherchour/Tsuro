package Test.java.model;

import main.java.model.Tuile;
import main.java.model.TuilesGenerator;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class TuilesGeneratorTest {

    @Test
    public void testGenererToutesLesTuiles() {
        List<Tuile> tuiles = TuilesGenerator.genererToutesLesTuiles();

        // Vérifier que 35 tuiles uniques sont générées
        assertEquals(35, tuiles.size());

        for (int i = 0; i < tuiles.size(); i++) {
            for (int j = i + 1; j < tuiles.size(); j++) {
                assertNotSame(toInt(tuiles.get(i).getTableauChemins()),toInt(tuiles.get(j).getTableauChemins()));
            }
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


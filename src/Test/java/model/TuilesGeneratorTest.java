package Test.java.model;

import main.java.model.Tuile;
import main.java.model.TuilesGenerator;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
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

    @Test
    public void testOfTuilesUniques(){
        // Générer toutes les tuiles uniques
        List<Tuile> tuilesUniques = TuilesGenerator.genererToutesLesTuiles();

        // Afficher les tuiles uniques
        ArrayList<String> models = new ArrayList<>(); // Pour stocker toutes les tuiles avec leurs rotations et vérifier la duplication
        int cpt = 0 ; // le Nombre

        System.out.println("AFFICHAGE DES TUILES AVEC ROTATIONS");
        for (Tuile tuile : tuilesUniques) {
            TuilesGenerator.afficherTuileAvecRotation(tuile);
            models.add(TuilesGenerator.convertirTuileEnChaine(tuile).get(0));
            models.add(TuilesGenerator.convertirTuileEnChaine(tuile).get(1));
            models.add(TuilesGenerator.convertirTuileEnChaine(tuile).get(2));
            models.add(TuilesGenerator.convertirTuileEnChaine(tuile).get(3));
            cpt ++;
        }

        System.out.println("models = "+ models.size() + ", 35 * 4 = 140");
        System.out.println("Le nombre de tuiles générées = "+cpt); // doit être égal à 35
        assertEquals(cpt,35);
        assertEquals(models.size(),140);

        HashSet<String> set = new HashSet<>(); // Pour vérifier les duplications
        int nbDuplic = 0 ;
        for (String element : models) {
            if (!set.add(element)) {
                nbDuplic ++ ;
                System.out.println("Duplicate element found: " + element);
            }
            System.out.println("success");
        }

        System.out.println("Le size du Set est  = "+set.size()); // doit être égal à 105
        assertEquals(set.size(),105);
        System.out.println("Nombre de Duplications = "+nbDuplic); // doit être égal à 35
        assertEquals(nbDuplic,35);
        System.out.println("Le nombre de Duplication est 35 ; car y'a des tuiles symétriques une fois retournée produit la même configuration");

    }
}



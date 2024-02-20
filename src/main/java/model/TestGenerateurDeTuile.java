package main.java.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static main.java.model.TuilesGenerator.convertirTableauEnChaine;

public class TestGenerateurDeTuile {
    //TODO : Cette classe sera supprimée une fois que le générateur de tuiles sera fonctionnel
    public static void main(String[] args) {
            List<Tuile> tuilesUniques = TuilesGenerator.genererToutesLesTuiles();
            Set<String> configurations = new HashSet<>();

            System.out.println(tuilesUniques.size());
            assert tuilesUniques.size() == 35 : "Le nombre de tuiles uniques devrait être 35.";

            System.out.println("/n/n RESULTAT");

            for (Tuile tuile : tuilesUniques) {
                String config = convertirTableauEnChaine(tuile.getTableauEntreeSortie());
                assert !configurations.contains(config) : "Configuration en double détectée.";
                configurations.add(config);
                // assert tuile.estCheminValide() : "La tuile ne possède pas un chemin valide.";
                System.out.println(convertirTableauEnChaine(tuile.getTableauEntreeSortie()));
            }
            System.out.println("Tous les tests sont passés avec succès !");

            int[] tuile = {1, 0, 3, 2, 5, 4, 7, 6};
            System.out.println(convertirTableauEnChaine(tuile)); // test de la méthode
    }
}



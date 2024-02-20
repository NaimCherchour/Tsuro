package main.java.model;

import java.util.*;

import static main.java.model.Tuile.TAILLE_DU_TABLEAU;

public class TuilesGenerator{
    // Classe pour generer les tuiles en utilisant Random
    static List<Tuile> genererToutesLesTuiles() {

        Set<String> configurationsUniques = new HashSet<>(); // Garder une trace des configurations déjà générées
        List<Tuile> tuilesUniques = new ArrayList<>(); // Sauvegarder les tuiles uniques

        // Générer toutes les combinaisons possibles de connexions
        for (int i = 0; i < 80; i++) {
            Tuile tuile = new Tuile();

            tuile.afficherTuile(); // test à supprimer
            System.out.println(); // test à supprimer

            genererConnexionsPourTuile(tuile); // Générer les connexions pour la tuile

            tuile.afficherTuile(); // test à supprimer
           // System.out.println(tuile.estCheminValide()); // TODO voir pourquoi ca sort FALSE pour toutes les tuiles

            if (verifierDoublonsDeRotations(tuile, configurationsUniques)) {
                configurationsUniques.add(convertirTableauEnChaine(tuile.getTableauEntreeSortie()));
                tuilesUniques.add(tuile);
            }

        }

        return tuilesUniques;
    }

    public static void genererConnexionsPourTuile(Tuile tuile) {
        List<Integer> pointsDisponibles = new ArrayList<>(); // les points pas encore connectés

        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            pointsDisponibles.add(i); // on ajoute tous les points dans la liste
        }

        Random rand = new Random();
        while (pointsDisponibles.size() > 1) {
            int indexPointA = rand.nextInt(pointsDisponibles.size()); // on genere un indice aleatoire
            int pointA = pointsDisponibles.remove(indexPointA);

            int indexPointB = rand.nextInt(pointsDisponibles.size());
            int pointB = pointsDisponibles.remove(indexPointB);

            tuile.connecterPoints(pointA, pointB);
        }
    }


    public static boolean verifierDoublonsDeRotations(Tuile tuile, Set<String> configurationsUniques) {
        Tuile tuileTemp = new Tuile(); // Créer une nouvelle instance pour tester les rotations
        tuileTemp.setTableauEntreeSortie(tuile.getTableauEntreeSortie().clone()); // Cloner le tableau de la tuile originale

        for (int rotation = 0; rotation < 3; rotation++) { // Tester les 3 rotations possibles
            tuileTemp.tournerTuile(); // Appliquer la rotation
            System.out.println("Rotation " + rotation + ":");
            tuileTemp.afficherTuile();
            String configuration = convertirTableauEnChaine(tuileTemp.getTableauEntreeSortie());

            if (configurationsUniques.contains(configuration)) {
                return false; // Trouvé un doublon dans les rotations
            }
        }

        return true;
    }

    static String convertirTableauEnChaine(int[] tableau) {
        StringBuilder sb = new StringBuilder();
        for (int valeur : tableau) {
            sb.append(valeur).append(',');
        }
        return sb.toString();
    }


}


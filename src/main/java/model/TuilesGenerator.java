package main.java.model;
import java.util.*;

import main.java.model.Tuile.Chemin;
public class TuilesGenerator {

    /**
     * permet de générer toutes les tuiles uniques ( 35 modèles )
     * static car on n'a pas besoin d'instancier un objet pour appeler cette méthode
     * @return une liste de tuiles uniques qu'on utilisera dans la partie
     */

    public static List<Tuile> genererToutesLesTuiles() {
        Set<String> configurationsUniques = new HashSet<>(); // Garder les configurations déjà générées
        List<Tuile> tuilesUniques = new ArrayList<>(); // Sauvegarder les tuiles uniques

        int i = 1 ; // pour le nombre de modèles
        // Générer toutes les combinaisons possibles de connexions
        while (i <= 35) { // On génère 35 tuiles uniques
            Tuile tuile = new Tuile(i); // Créer une nouvelle tuile avec un ID unique
            genererConnexionsPourTuile(tuile); // Générer les connexions pour la tuile

            if (verifierDoublonsDeRotations(tuile, configurationsUniques)) {
                // on vérifie si la tuile avec l'une de ses 3 rotations est déjà présente dans
                //les configurations uniques
                // Si la tuile avec toutes ses rotations n'a pas de duplica alors on l'ajoute avec ses 3 configurations possibles
                configurationsUniques.add(convertirTuileEnChaine(tuile).get(0)); // Avec des chaines de Caractères ; rotation = 0
                configurationsUniques.add(convertirTuileEnChaine(tuile).get(1)); // rotation = 1
                configurationsUniques.add(convertirTuileEnChaine(tuile).get(2)); // rotation = 2
                configurationsUniques.add(convertirTuileEnChaine(tuile).get(3)); // rotation = 3
                tuilesUniques.add(tuile); // On ajoute la Tuile dans Les tuiles Uniques
                i ++ ; // on incrémente le nombre de tuiles
            }
        }
        return tuilesUniques;
    }

    /**
     * Générer les connexions pour une tuile avec Random et des Conditions
     * @param tuile
     * @return void ; car on a juste à modifier le Tableau de Chemins de la tuile
     * Les couleurs de chemins sont initialisé à NOIR pour indiquer que le chemin n'est pas encore utilisé dans le Constructeur
     */

    public static void genererConnexionsPourTuile(Tuile tuile) {
        Random rand = new Random();
        int[] pointsSortie = new int[Tuile.TAILLE_DU_TABLEAU]; // Garder une trace des points
        Arrays.fill(pointsSortie,-1); // on initialise le tableau à -1 qui indique vide
        for (int i = 0; i < Tuile.TAILLE_DU_TABLEAU; i++) {
            if (pointsSortie[i] == -1) { // si la case est vide ; cette condition sert pour le doublon
                int pointSortie = rand.nextInt(Tuile.TAILLE_DU_TABLEAU); // on génère une sortie pour l'indice ( entrée )
                while (pointSortie == i || pointsSortie[pointSortie] != -1) {
                    // on regénère jusqu'à ne plus avoir le cas 0->0 ou 1->1 ... Il faut que pointSortie != indice (entrée)
                    // et que le point de sortie ne soit pas déjà utilisé ie : si je génère 1->5 alors 5->? doit être vide
                    pointSortie = rand.nextInt(Tuile.TAILLE_DU_TABLEAU); // Sélectionner aléatoirement un point de sortie
                }
                pointsSortie[i] = pointSortie; // on ajoute le point de Sortie par ex: 0->1
                pointsSortie[pointSortie] = i; // Le doublon donc par ex : 1->0
            }
        }
        tuile.setTableauChemins(pointsSortie); // on rempli le tableau de Chemins avec ce tableau int[] de sorties généré
    }

    /**
     * Vérifier les doublons de rotations ie : vérifie si une tuile ou une de ses 3 rotations est déjà présente dans le Set
     * ConfigurationUniques ; si c'est le cas alors on retourne false sinon on retourne true
     * @param tuile ; la tuile à vérifier
     * @param configurationsUniques ; Set de configurations unique
     * @return true si la tuile avec ses rotations n'a pas de doublon dans les tuiles dèjà générées ; false sinon
     */

    public static boolean verifierDoublonsDeRotations(Tuile tuile, Set<String> configurationsUniques) {
        Tuile tuileTemp = new Tuile(tuile.getId()); // Créer une copie de la tuile pour tester les rotations
        tuileTemp.setTableauChemins(Arrays.copyOf(tuile.getTableauChemins(), tuile.getTableauChemins().length));

        for (int rotation = 0; rotation < 4; rotation++) { // Tester les 4 rotations possibles dont la rotation = 0 ie : la tuile initiale
            String configuration = convertirTuileEnChaine(tuileTemp).get(rotation); // Convertir la tuile en chaîne de caractères
            // La méthode convertirTuileEnChaine retourne une liste de 4 chaines de caractères pour les 4 rotations possibles
            // Donc on récupère avec get(rotation) la chaine de caractères correspondant à la rotation
            if (configurationsUniques.contains(configuration)) {
                return false; // Trouvé une rotation dupliquée
            }
            tuileTemp.tournerTuile(); // Appliquer la rotation pour tester la prochaine configuration
        }
        return true;
    }

    /**
     * Convertir une tuile en chaîne de caractères ainsi que ses rotations
     * @param tuile
     * @return une liste de 4 chaînes de caractères pour les 4 rotations possibles
     */

    public static ArrayList<String> convertirTuileEnChaine(Tuile tuile) {
        String str = "";
        String str1 = "";
        String str2 = "";
        String str3 = "";
        ArrayList<String> ret = new ArrayList<>(); // Garder les 4 configurations possibles
        //ArrayList pour faciliter l'accès aux configurations
        // Tuile initiale
        for (int i = 0; i < Tuile.TAILLE_DU_TABLEAU; i++) {
            Chemin chemin = tuile.getTableauChemins()[i];
            str += i + ":" + chemin.getPointSortie() + "|"; // la chaine d'une tuile

        }
        ret.add(str);
        // Tuile avec rotation 1
        for (int i = 0; i < Tuile.TAILLE_DU_TABLEAU; i++) {
            str1 += i + ":" + tuile.getPointSortieAvecRot(i,1) + "|";
        }
        ret.add(str1);
        // Tuile avec rotation 2
        for (int i = 0; i < Tuile.TAILLE_DU_TABLEAU; i++) {
            str2 += i + ":" + tuile.getPointSortieAvecRot(i,2) + "|";

        }
        ret.add(str2);
        // Tuile avec rotation 3
        for (int i = 0; i < Tuile.TAILLE_DU_TABLEAU; i++) {
            str3 += i + ":" + tuile.getPointSortieAvecRot(i,3) + "|";

        }
        ret.add(str3);
        return ret; //On retourne les 4 configurations possibles dans un ArrayList
    }

    /**
     * Afficher une tuile avec ses 4 rotations possibles
     * @param tuile
     * @return void
     */
    public static void afficherTuileAvecRotation (Tuile tuile ){
        System.out.print("r=0 ");
        System.out.print(convertirTuileEnChaine(tuile).get(0));
        System.out.print(" r=1 ");
        System.out.print(convertirTuileEnChaine(tuile).get(1));
        System.out.print(" r=2 ");
        System.out.print(convertirTuileEnChaine(tuile).get(2));
        System.out.print(" r=3 ");
        System.out.print(convertirTuileEnChaine(tuile).get(3));
        System.out.println();
    }



}

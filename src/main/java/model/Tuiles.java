package main.java.model;

public class Tuiles{


    private static int dernierIdAttribue = 0;
    private int id;
    private static final int TAILLE_DU_TABLEAU = 8;
    private int[] tableauEntreeSortie;

    public Tuiles() {
        this.id = ++dernierIdAttribue;
        tableauEntreeSortie = new int[TAILLE_DU_TABLEAU];
        // Initialiser la tuile avec des valeurs par défaut pour éviter les chemins non définis
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            tableauEntreeSortie[i] = -1; // -1 indique qu'aucun chemin n'est encore défini
        }
    }
    /**
     * Connecte deux points sur la tuile.
     * 
     * @param pointA Premier point à connecter.
     * @param pointB Deuxième point à connecter.
     */
    public void connecterPoints(int pointA, int pointB) {
        if (pointA < 0 || pointA >= TAILLE_DU_TABLEAU || pointB < 0 || pointB >= TAILLE_DU_TABLEAU) {
            System.out.println("Indices des points invalides.");
            return;
        }
        tableauEntreeSortie[pointA] = pointB;
        tableauEntreeSortie[pointB] = pointA;
    }
    /**
     * Effectue une rotation de la tuile de 90 degrés dans le sens des aiguilles d'une montre.
     * Utilise la formule canonique avec ajustement pour maintenir l'orientation au Nord.
     */
    public void tournerTuile() {
        int[] nouveauTableau = new int[TAILLE_DU_TABLEAU];
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            int nouvellePosition = (i + 2) % TAILLE_DU_TABLEAU;
            nouveauTableau[nouvellePosition] = (tableauEntreeSortie[i] + 2) % TAILLE_DU_TABLEAU;
        }
        tableauEntreeSortie = nouveauTableau;
    }

    /**
     * Affiche les connexions actuelles de la tuile.
     */
    public void afficherTuile() {
        System.out.println("Tuile ID: " + id);
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            if (tableauEntreeSortie[i] != -1) {
                System.out.println("De " + i + " à " + tableauEntreeSortie[i]);
            }
        }
    }

}
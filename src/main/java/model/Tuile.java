package main.java.model;

import main.java.model.Joueur.Couleur;
import main.java.model.PlateauTuiles.Direction;

import java.io.Serializable;

public class Tuile implements Cloneable, Serializable, ReadOnlyTuile {
    private static final long serialVersionUID = 1L; // Définir un serialVersionUID cohérents
    private final int id; // Identifiant de la tuile ;
    public static final int TAILLE_DU_TABLEAU = 8; // Taille du tableau de chemins ;
    private Chemin[] tableauChemins; // représente les 4 chemins de la tuile ( en Doublons ) ; c'est l'identité de la tuile donc final
    private int rotation = 0; // Rotation de la tuile (0, 1-> +1/4, 2->+2/4, 3-> +3/4)


    // getters / setters
    public Chemin[] getTableauChemins(){
        return tableauChemins;
    }

    // modifie le tableau de chemins de la tuile avec Un tableau d'entiers sans avoir à créer un nouvel objet Chemin
    // Ce Setter sert à une seul chose : à la génération des tuiles
    // Car le Tableau de Chemins est final ; c'est l'identité de la Tuile
    public void setTableauChemins(int[] tableauSortie){
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            tableauChemins[i] = new Chemin(tableauSortie[i]); // Crée un nouvel objet Chemin pour chaque élément du tableau
        }
    }

    // modifie le tableau de chemins de la tuile avec Un tableau de Chemins
    // Ce Setter sert à une seul chose : à la génération des tuiles
    // Car le Tableau de Chemins est final ; c'est l'identité de la Tuile
    public void setTableauChemins(Chemin[] tableauSortie){
        tableauChemins = tableauSortie;
    }

    public int getRotation(){
        return rotation;
    }

    public void setRotation (int rot) {
        this.rotation = rot % 4 ;
    }

    public int getId (){
        return id;
    }

    /**
     * Le Tableau de chemin contient à chaque indice  le point de sortie ,la couleur du chemin
     * L'indice représente le point d'entrée
     */
    public Tuile(int id, int[] tableauEntreeSortie) {
        this.id = id;
        tableauChemins = new Chemin[TAILLE_DU_TABLEAU]; // création des chemins correspondant aux interconnexions du tableau
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            tableauChemins[i] = new Chemin(tableauEntreeSortie[i]); // Crée un nouvel objet Chemin pour chaque élément du tableau
        }
        this.rotation = 0; // rotation à 0 par défaut
    }

    // Constructeur pour la génération des tuiles ; On a besoin d'une Tuile vide au début de la génération
    public Tuile ( int id ){
        // For generating Tiles
        this.id = id;
        tableauChemins = new Chemin[TAILLE_DU_TABLEAU]; // création des chemins correspondant aux interconnexions du tableau
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            tableauChemins[i] = new Chemin(-1); // Crée un nouvel objet Chemin pour chaque élément du tableau
        }
        this.rotation = 0; // rotation à 0 par défaut
    }


    /**
     * copie profonde de la Tuile
     * @return copie ; la copie de la tuile actuel avec un Id + 200
     */
    @Override
    public Tuile clone() {
        try {
            Tuile clone = (Tuile) super.clone();
            // On copie l'identité de la Tuile
            int[] tab = new int[8];
            for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
                tab[i] = this.tableauChemins[i].getPointSortie();
            }
            clone = new Tuile(this.id + 200,tab);
            clone.rotation = this.rotation;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void tournerTuile(){
        // Tourne la tuile de 90° dans le sens des aiguilles d'une montre
        // Les chemins changent mais celà ne change pas l'identité de la tuile
        // On a une formule pour calculer le nouveau point de sortie selon la rotation
        this.rotation += 1;
        this.rotation = this.rotation % 4;
    }

    /**
     * permet de trouver le point de sortie d'un chemin selon la rotation
     * @param ind ( <=> l'entrée du Chemin)
     * @return le Point de sortie selon La rotation
     */
    public int getPointSortieAvecRot(int ind) {
        int tmp = (ind - (this.rotation * 2)) % 8;
        if (tmp < 0) {
            tmp += 8; // S'assurer que tmp est positive car Mod en java peut retourner un nombre négatif
        }
        return (tableauChemins[tmp].getPointSortie() + rotation*2)%8;
    }

    public int getPointSortieAvecRot(int ind, int rotation) {
        int tmp = (ind - (rotation * 2)) % 8;
        if (tmp < 0) {
            tmp += 8; // S'assurer que tmp est positive car Mod en java peut retourner un nombre négatif
        }
        return (tableauChemins[tmp].getPointSortie() + rotation*2)%8;
    }


    public class Chemin implements Serializable, ReadOnlyChemin{

        private int pointSortie; // Point de sortie du chemin tel que le point d'entrée est l'indice du tableau
        private Joueur.Couleur couleur; // Couleur du chemin ; les couleurs sont définies dans l'enum Couleur
        // couleur du chemin indique aussi le joueur qui a emprunté le chemin
        // Noir indique que le chemin n'est pas emprunté

        /**
         * @param pointSortie représente le point de sortie du chemin du joueur
         * @Description : Le constructeur de la classe Chemin ; la couleur sera initialisée par défault à NOIR
         */
        public Chemin(int pointSortie ) {
            this.pointSortie = pointSortie;
            this.couleur = Couleur.NOIR;
        }

        // Getters / Setters

        public int getPointSortie() {
            return pointSortie;
        }

        public Joueur.Couleur getCouleur() {
            return couleur;
        }

        public void setCouleur(Joueur.Couleur couleur) {
            this.couleur = couleur;
        }

        public boolean estEmprunte() {
            return this.couleur != Couleur.NOIR;
        }

        public void marquerCheminVisite(int indiceChemin, Joueur.Couleur couleur) {
            // Marque un chemin avec la couleur / joueur qui l'a emprunté ainsi que le doublon
            // Si ROUGE a emprunté 1-> 3 il sera colorier ainsi que le doublon 3 -> 1
            tableauChemins[indiceChemin].setCouleur(couleur);;
            int tmp = getPointSortieAvecRot(indiceChemin);
            tableauChemins[tmp].setCouleur(couleur);// on doit aussi changer la couleur pour  tableauChemins[tmp] afin de gerer les doublons
        }
    }
}


package main.java.model;
import java.util.Scanner;

import main.java.model.Joueur;

import main.java.model.Tuile.Chemin;

/**
 * La classe PlateauTuiles représente le plateau de jeu composé de tuiles.
 */
public class PlateauTuiles {

    private Joueur joueurActuel;
    // Enumération des directions
    public enum Direction {
        NORD, EST, SUD, OUEST;

        // Obtenir la direction opposée
        public Direction oppose() {
            return switch (this) {
                case NORD -> SUD;
                case EST -> OUEST;
                case SUD -> NORD;
                case OUEST -> EST;
            };
        }
        public int di() {
            return switch (this) {
                case NORD -> -1;
                case SUD -> 1;
                default -> 0;
            };
        }
        public int dj() {
            return switch (this) {
                case EST -> 1;
                case OUEST -> -1;
                default -> 0;
            };
        }
        public static Direction getDirectionFromPoint ( int ind ){
            // return values()[ind / 2];
            if ( ind == 0 || ind == 1 ) {
                return NORD;
            } else if (ind == 2 || ind == 3 ){
                return EST;
            } else if ( ind == 4 || ind == 5){
                return SUD;
            } else if ( ind == 6 || ind == 7 ){
                return OUEST;
            }
            return null;
        }
        // Méthode pour vérifier si deux directions sont du même côté
        public static boolean sameSide(Direction first, Direction other) {
            // Comparaison basée sur les indices (0 et 1, 2 et 3, 4 et 5, 6 et 7)
            return first.ordinal()  == other.ordinal() ;
        }

        // Méthode pour vérifier si deux directions sont opposées
        public static boolean opposite(Direction first, Direction other) {
            // Deux directions sont opposées si elles diffèrent de 4 (180 degrés)
            return Math.abs(first.ordinal() - other.ordinal()) == 2;
        }

        // Méthode pour vérifier si deux directions sont adjacentes
        public static boolean adjacent(Direction first, Direction other) {
            // Deux directions sont adjacentes si leur différence est 2 (90 degrés)
            return (Math.abs(first.ordinal() - other.ordinal()) == 1 ) ||(Math.abs(first.ordinal()-other.ordinal()) == 3);
        }
    }
    private Tuile[][] plateau; // Matrice représentant le plateau de tuiles.
    public int[] interConnection = {5,4,7,6,1,0,3,2}; // Pour chaque index, correspond à la nouvelle index dans la tuile suivante.

    /**
     * Constructeur de la classe PlateauTuiles.
     *
     * @param taille La taille du plateau de jeu.
     */
    public PlateauTuiles(int taille) {
        plateau = new Tuile[taille][taille];
    }

    /**
     * Place une tuile sur le plateau à la position spécifiée et vérifie si le joueur perd.
     *
     * @param ligne La ligne où placer la tuile.
     * @param colonne La colonne où placer la tuile.
     * @param tuile La tuile à placer.
     * @param j Le joueur associé à la tuile.
     */
    public void placerTuile(int ligne, int colonne, Tuile tuile, Joueur j) {
        if (ligne < 0 || ligne >= plateau.length || colonne < 0 || colonne >= plateau.length ||  plateau[ligne][colonne] != null ){
            System.out.println("Impossible de placer une tuile ici.");
            return;
        }
        plateau[ligne][colonne] = tuile;
    }
    public boolean isEmpty(int ligne, int colonne){
        return plateau[ligne][colonne]==null;
    }

    /**
     * Affiche le plateau de tuiles.
     */
    public void afficherPlateau() {
//TODO : à supprimer lors de la fin de la partie vue
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j] == null) {
                    System.out.print("X\t");
                } else {
                    System.out.print("T " + "\t");
                }
            }
            System.out.println("\n");
        }
    }

    private boolean coordonneesValides(int ligne, int colonne) {
        return (ligne >= 0 && ligne < plateau.length && colonne >= 0 && colonne < plateau[0].length);
    }

    /**
     * Actualise la position du joueur après le placement d'une tuile.
     *
     * @param j Le joueur dont la position doit être actualisée.
     */

    // Méthode pour actualiser la position du joueur après le placement d'une tuile
    public void actualiserPosJ(Joueur j) {
        // Cette méthode est censé être dans la classe Joueur et non dans PlateauTuiles
        Direction entree = j.getDirectionEntree().oppose(); // La direction du joueur
        int nouvelleLigne = j.getLigne() + entree.oppose().di(); // La nouvelle ligne du joueur selon sa direction
        int nouvelleColonne = j.getColonne() + entree.oppose().dj(); // La nouvelle colonne du joueur selon sa direction
        System.out.println("Nouvelle ligne : " + nouvelleLigne + " Nouvelle colonne : " + nouvelleColonne);

        // while
        if (coordonneesValides(nouvelleLigne, nouvelleColonne)) { // Si les nouvelles coordonnées sont valides
            if (!isEmpty(nouvelleLigne, nouvelleColonne)) { // S il n y a pas une tuile on se deplace
                j.setLigne(nouvelleLigne);
                j.setColonne(nouvelleColonne);
                Tuile nouvelleTuile = plateau[nouvelleLigne][nouvelleColonne]; // TODO : Le joueur place un etuile d'abord après il se déplace
                Chemin nouveauChemin = nouvelleTuile.trouverNouveauChemin(j.getDirectionEntree(), j.getEntree());
                System.out.println("sortie du chemin :"+(nouveauChemin.getPointSortie()+2*nouvelleTuile.getRotation())%8);
                if (nouveauChemin.estEmprunte()) {
                    System.out.println("Vous allez rentrer en collision avec un autre joueur !\nLe joueur a perdu.");
                } else {
                    j.setEntree(nouveauChemin,nouvelleTuile); // On change le point d'entrée du joueur sur la tuile avec la nouvelle sortie
                    nouveauChemin.setCouleur(j.getCouleur());
                    actualiserPosJ(j); // ??
                }
            } else {
                System.out.println("Déplacement impossible : aucune tuile dans la direction spécifiée.");
            }
        } else {
            System.out.println("Déplacement impossible : le joueur est sorti du plateau.");
        }
    }

    /**
     * Vérifie si le joueur a perdu en sortant du plateau.
     *
     * @param joueur Le joueur à vérifier.
     * @return true si le joueur a perdu, sinon false.
     */
    public boolean joueurPerdu(Joueur joueur) {
        int ligne = joueur.getLigne();
        int colonne = joueur.getColonne();

        return (ligne < 0 || ligne >= plateau.length || colonne < 0 || colonne >= plateau.length);
    }

    /**
     * Réinitialise le plateau de tuiles en le remettant à zéro.
     */
    public void reinitialiserPlateau() {
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                plateau[i][j] = null;
            }
        }
    }

    public void setTuile(int ligne, int colonne, Tuile tuile) {
        if (coordonneesValides(ligne, colonne)) {
            plateau[ligne][colonne] = tuile;
        } else {
            throw new IllegalArgumentException("Les coordonnées spécifiées sont invalides.");
        }
    }



    // TEST
    public static void main(String[] args) {

        Tuiles tuiles = new Tuiles();
        Tuile tuile1 = tuiles.getTuile(1);
        tuile1.afficherTuileNaive();
        Tuile tuile2 = tuiles.getTuile(2);
        tuile2.afficherTuileNaive();
        tuile2.tournerTuile();
        tuile2.tournerTuile();

        PlateauTuiles plateau = new PlateauTuiles(7);
        plateau.afficherPlateau();

        System.out.println("Entrez les coordonnées de départ (ligne colonne) :");
        int ligneDepart = 0;
        int colonneDepart = -1;
        Joueur joueur1 = new Joueur(ligneDepart, colonneDepart, 2, "J1");
        System.out.println(joueur1.getCouleur());

        System.out.println("Coordonnées de départ du joueur 1 : " + joueur1.getLigne() + ", " + joueur1.getColonne());
        System.out.println("Direction d'entrée du joueur 1 : " + joueur1.getDirectionEntree());
        System.out.println("Point d'entrée du joueur 1 : " + joueur1.getEntree());
        System.out.println("Entrez les coordonnées de la tuile à placer (ligne colonne) :");
        int ligneTuile = 0;
        int colonneTuile = 0;

        // Ici vous devez choisir quel joueur va placer la tuile, puis appeler la méthode placerTuile() en conséquence
        plateau.placerTuile(ligneTuile, colonneTuile, tuile1, joueur1); // Par exemple, placer la tuile pour le joueur 1
        plateau.placerTuile(ligneTuile, colonneTuile+1, tuile2, joueur1); // Par exemple, placer la tuile pour le joueur 1
        plateau.actualiserPosJ(joueur1);

        plateau.afficherPlateau();

        System.out.println("Coordonnées du joueur 1 après placement de tuile : " + joueur1.getLigne() + ", " + joueur1.getColonne() + ". Sorti : "+joueur1.getEntree());


        //les colisions entre joueur ne sont pas encore gerer, un joueuer perd uniquement lorsqu'il rentre dans une chemin qui a déja été occupé ou quand il sort du tableau
    }
}

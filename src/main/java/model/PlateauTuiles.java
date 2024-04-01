package main.java.model;
import java.util.List;
import java.util.Scanner;

import main.java.model.Joueur;
import main.java.model.Joueur.Couleur;
import java.util.*;

import main.java.model.Tuile.Chemin;
import main.java.vue.GameBoardUI;

/**
 * La classe PlateauTuiles représente le plateau de jeu composé de tuiles.
 */
public class PlateauTuiles {

    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

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
            return values()[ind / 2];
        }

        public static int getPointFromDirection(Direction d, int sortie){
            // la nouvelle entrée
            if (sortie % 2 == 0) {
                return d.ordinal() * 2 + 1;
            }else {
                return d.ordinal()*2;
            }
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
    private List<Joueur> joueurs; // La liste des joueurs


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
     * @param tuile La tuile à placer.
     * @param j Le joueur associé à la tuile.
     */
    public boolean placerTuile(Tuile tuile, Joueur j) {
        if (j.getLigne() < 0 || j.getLigne() >= plateau.length || j.getColonne() < 0 || j.getColonne() >= plateau.length ||  plateau[j.getLigne()][j.getColonne()] != null ){
            System.out.println("Impossible de placer une tuile ici.");
            return false ;
        }
        plateau[j.getLigne()][j.getColonne()] = tuile;
        actualiserPosJ(joueurs); // TODO : mettre la liste de joueur // FAIT
        return true;
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

    boolean coordonneesValides(int ligne, int colonne) {
        return (ligne >= 0 && ligne < plateau.length && colonne >= 0 && colonne < plateau[0].length);
    }

    public Tuile getTuile(int x, int y) {
        if (coordonneesValides(x, y)) {
            return plateau[x][y];
        }
        return null;
    }
    

    /**
     * Actualise la position du joueur après le placement d'une tuile.
     *
     * @param //j Le joueur dont la position doit être actualisée.
     */

    // Méthode pour actualiser la position du joueur après le placement d'une tuile
    public void actualiserPosJ(List<Joueur> joueurs) {
        // Cette méthode est censé être dans la classe Joueur et non dans PlateauTuiles
        for (Joueur j :joueurs) {
            if (j.getLigne()>=0 && j.getLigne()<6 && j.getColonne()>=0 && j.getColonne()<6){
                Tuile tuileAjouté = plateau[j.getLigne()][j.getColonne()];
                if ( !isEmpty(j.getLigne(),j.getColonne()) ) {
                    int sortie = tuileAjouté.getPointSortieAvecRot(j.getEntree());
                    if (tuileAjouté.getTableauChemins()[j.getEntree()].estEmprunte()) {
                        System.out.println("Lost CHEMIN DEJA VISITE");
                        j.setAlive(false);
                    } else {
                        Direction newEntry = Direction.getDirectionFromPoint(sortie); //vers newEntry
                        tuileAjouté.getTableauChemins()[j.getEntree()].marquerCheminVisite(j.getEntree(), j.getCouleur());
                        int nouvelleEntree = Direction.getPointFromDirection(newEntry.oppose(), sortie);
                        j.setPointEntree(nouvelleEntree);
                        int nouvelleLigne = j.getLigne() + newEntry.di(); // La nouvelle ligne du joueur selon sa direction
                        int nouvelleColonne = j.getColonne() + newEntry.dj(); // La nouvelle colonne du joueur selon sa direction
                        if (coordonneesValides(nouvelleLigne, nouvelleColonne)) {
                            j.setLigne(nouvelleLigne);
                            j.setColonne(nouvelleColonne);
                            j.incrementerCompteur();
                            System.out.println("Longueur du chemin du joueur"+ j.getCouleur()+ " :"+j.getCompteur());
                            actualiserPosJ(joueurs);
                        } else {
                            j.setLigne(nouvelleLigne);
                            j.setColonne(nouvelleColonne);
                            System.out.println("Lost OUT OF BORDER");
                            j.setAlive(false);
                        }
                    }
                } else {
                    System.out.println("Aucune Tuile");
                }
            }
            else {
                System.out.println("Joueur sortie du plateau");
                j.setAlive(false);
            }
        }
    }

    public void actualiserPosJ(Joueur j) {
        // Cette méthode est censé être dans la classe Joueur et non dans PlateauTuiles

        Tuile tuileAjouté = plateau[j.getLigne()][j.getColonne()];
        if ( !isEmpty(j.getLigne(),j.getColonne()) ) {
            int sortie = tuileAjouté.getPointSortieAvecRot(j.getEntree());
            if (tuileAjouté.getTableauChemins()[j.getEntree()].estEmprunte()) {
                System.out.println("Lost CHEMIN DEJA VISITE");
            } else {
                Direction newEntry = Direction.getDirectionFromPoint(sortie); //vers newEntry
                tuileAjouté.getTableauChemins()[j.getEntree()].marquerCheminVisite(j.getEntree(), j.getCouleur());
                int nouvelleEntree = Direction.getPointFromDirection(newEntry.oppose(), sortie);
                j.setPointEntree(nouvelleEntree);
                int nouvelleLigne = j.getLigne() + newEntry.di(); // La nouvelle ligne du joueur selon sa direction
                int nouvelleColonne = j.getColonne() + newEntry.dj(); // La nouvelle colonne du joueur selon sa direction
                if (coordonneesValides(nouvelleLigne, nouvelleColonne)) {
                    j.setLigne(nouvelleLigne);
                    j.setColonne(nouvelleColonne);
                    actualiserPosJ(j);
                } else {
                    j.setLigne(nouvelleLigne);
                    j.setColonne(nouvelleColonne);
                    System.out.println("Lost OUT OF BORDER");
                }
            }
        } else {
            System.out.println("Aucune Tuile");
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

    public boolean peutPlacerTuile(int x, int y, Tuile tuile) {
        if (!coordonneesValides(x, y) || !isEmpty(x, y)) {
            return false; // Vérifie si la position est dans les limites et vide
        }
        // on doit mettre ici la logique pour vérifier l'alignement avec les tuiles adjacentes si nécessaire
        return true;
    }
    

    // Dans la classe Joueur, la méthode getTuilesDisponibles est implémentée.
    public List<Tuile> getTuilesDisponibles() {
        // Retourne la liste des tuiles que le joueur peut utiliser.
        // Implémentation fictive :
        return new ArrayList<>(); // Doit retourner les tuiles réelles disponibles pour le joueur.
    }

    public int getTaille() {
        return plateau.length; // Supposant que le plateau est carré
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
        Joueur joueur1 = new Joueur(ligneDepart, colonneDepart, 2, "J1", false);
        System.out.println(joueur1.getCouleur());

        System.out.println("Coordonnées de départ du joueur 1 : " + joueur1.getLigne() + ", " + joueur1.getColonne());
        System.out.println("Direction d'entrée du joueur 1 : " + Direction.getDirectionFromPoint(joueur1.getEntree()));
        System.out.println("Point d'entrée du joueur 1 : " + joueur1.getEntree());
        System.out.println("Entrez les coordonnées de la tuile à placer (ligne colonne) :");
        int ligneTuile = 0;
        int colonneTuile = 0;

        // Ici vous devez choisir quel joueur va placer la tuile, puis appeler la méthode placerTuile() en conséquence
        //plateau.placerTuile(ligneTuile, colonneTuile, tuile1, joueur1); // Par exemple, placer la tuile pour le joueur 1
        //plateau.placerTuile(ligneTuile, colonneTuile+1, tuile2, joueur1); // Par exemple, placer la tuile pour le joueur 1
        //plateau.actualiserPosJ(joueur1);

        plateau.afficherPlateau();

        System.out.println("Coordonnées du joueur 1 après placement de tuile : " + joueur1.getLigne() + ", " + joueur1.getColonne() + ". Sorti : "+joueur1.getEntree());


        //les colisions entre joueur ne sont pas encore gerer, un joueuer perd uniquement lorsqu'il rentre dans une chemin qui a déja été occupé ou quand il sort du tableau
    }
}

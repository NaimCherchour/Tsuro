package main.java.model;
import java.util.List;


/**
 * La classe PlateauTuiles représente le plateau de jeu composé de tuiles.
 */

public class PlateauTuiles {
    // Crée une copie profonde du plateau actuel.
    public PlateauTuiles copiePlateau() {
        PlateauTuiles copie = new PlateauTuiles(plateau.length);
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j] != null) {
                    copie.plateau[i][j] = plateau[i][j].copier(); // Assurez-vous que la méthode copier() de Tuile fonctionne correctement
                }
            }
        }
        return copie;
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
        // Le calcul du déplacement en ligne selon la direction
        public int di() {
            return switch (this) {
                case NORD -> -1;
                case SUD -> 1;
                default -> 0;
            };
        }
        // Le calcul du déplacement en colonne selon la direction
        public int dj() {
            return switch (this) {
                case EST -> 1;
                case OUEST -> -1;
                default -> 0;
            };
        }

        // Obtenir la direction à partir d'un point d'entrée vers une tuile par exemple : 0 et 1 -> NORD
        public static Direction getDirectionFromPoint ( int ind ){
            return values()[ind / 2];
        }

        // Obtenir le prochain point d'entrée à la tuile adjacente à partir d'une direction et d'un point de sortie
        public static int getPointFromDirection(Direction d, int sortie){
            // la nouvelle entrée
            if (sortie % 2 == 0) {
                return d.ordinal()*2 + 1;
            }else {
                return d.ordinal()*2;
            }
        }

        // Méthode pour vérifier si deux directions sont opposées
        public static boolean opposite(Direction first, Direction other) {
            // Deux directions sont opposées si elles diffèrent de 4 (180 degrés)
            return Math.abs(first.ordinal() - other.ordinal()) == 2;
        }

        // Méthode pour vérifier si deux directions sont adjacentes
        public static boolean adjacent(Direction first, Direction other) {
            // Deux directions sont adjacentes si leur différence est égal à impaire
            return (Math.abs(first.ordinal() - other.ordinal()) % 2 == 1);
        }
    }

    private Tuile[][] plateau; // Matrice représentant le plateau de tuiles.


    /**
     * Constructeur de la classe PlateauTuiles.
     * @param taille La taille du plateau de jeu.
     */
    public PlateauTuiles(int taille) {
        plateau = new Tuile[taille][taille];
    }

    public Tuile getTuile(int ligne, int colonne){
        return plateau[ligne][colonne];
    }



    /**
     * Place une tuile sur le plateau à la position spécifiée et vérifie si le joueur perd.
     * @param tuile La tuile à placer.
     * @param j Le joueur associé à la tuile.
     * @param joueurs Les joueurs de la partie.
     */
    public boolean placerTuile(Tuile tuile, Joueur j, List<Joueur> joueurs) {
        if (j.getLigne() < 0 || j.getLigne() >= plateau.length || j.getColonne() < 0 || j.getColonne() >= plateau.length ||  plateau[j.getLigne()][j.getColonne()] != null ){
            System.out.println("Impossible de placer une tuile ici.");
            return false ;
        }
        plateau[j.getLigne()][j.getColonne()] = tuile;
        actualiserPosJ(joueurs);
        return true;
    }

    public boolean isEmpty(int ligne, int colonne){
        return plateau[ligne][colonne]==null;
    }

    protected boolean coordonneesValides(int ligne, int colonne) {
        return (ligne >= 0 && ligne < plateau.length && colonne >= 0 && colonne < plateau[0].length);
    }
    public boolean peutPlacerTuile(int x, int y) {
        if (!coordonneesValides(x, y) || !isEmpty(x, y)) {
            System.out.println("Placement invalide ou emplacement déjà occupé.");
            return false;
        }
        return true;
    }


    /**
     * Actualise la position du joueur après le placement d'une tuile.
     * @param joueurs Les joueurs dont la position doit être actualisée après le placement d'une tuile.
     */

    public void actualiserPosJ(List<Joueur> joueurs) {
        // Cette méthode est censé être dans la classe Joueur et non dans PlateauTuiles
        for (Joueur j :joueurs) {
            if (j.getLigne()>=0 && j.getLigne()<6 && j.getColonne()>=0 && j.getColonne()<6){
                Tuile tuileAjouté = plateau[j.getLigne()][j.getColonne()];
                if ( !isEmpty(j.getLigne(),j.getColonne()) ) {
                    int sortie = tuileAjouté.getPointSortieAvecRot(j.getPointEntree());
                    if (tuileAjouté.getTableauChemins()[j.getPointEntree()].estEmprunte()) {
                        System.out.println("Lost CHEMIN DEJA VISITE");
                        j.setAlive(false);
                    } else {
                        Direction newEntry = Direction.getDirectionFromPoint(sortie); //vers newEntry
                        tuileAjouté.getTableauChemins()[j.getPointEntree()].marquerCheminVisite(j.getPointEntree(), j.getCouleur());
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


    /**
     * Vérifie si le joueur a perdu en sortant du plateau.
     * @param joueur Le joueur à vérifier.
     * @return true si le joueur a perdu, sinon false.
     */
    public boolean joueurPerdu(Joueur joueur) {
        //TODO : À SUPPRIMER
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


}

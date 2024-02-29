package main.java.model;

import main.java.model.Joueur.Couleur;

public class Tuile{
private int id; // Identifiant de la tuile ; TODO : à supprimer ou à garder ?
    private static final int TAILLE_DU_TABLEAU = 8; // Taille du tableau de chemins ; TODO : Peut-on utiliser 4 cases seulement ?
    private final Chemin[] tableauChemins; // représente les 4 chemins de la tuile ( en Doublons ) ; c'est l'identité de la tuile donc final
    private int rotation = 0; // Rotation de la tuile (0, 1-> +1/4, 2->+2/4, 3-> +3/4)

    
    // getters / setters
    public Chemin[] getTableauChemins(){
        return tableauChemins;
    }

    public int getRotation(){
        return rotation;
    }
    
    public void setRotation(int rotation){
        this.rotation = rotation;
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
    /**
     * Affiche les connexions actuelles de la tuile.
     */
        public void afficherTuileNaive() {
// affichage d'une tuil terminal pour aider aux tests
        // TODO : à supprimer lors de la fin de la partie vue
        System.out.println("Tuile ID: " + id);
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            Chemin chemin = tableauChemins[i];
            if (chemin.getPointSortie() != -1) {
                System.out.println("De " + i + " à " + chemin.getPointSortie() +" (Couleur: " + chemin.getCouleur() + ")" );
            }
        }
    }

    public void tournerTuile(){
        // Tourne la tuile de 90° dans le sens des aiguilles d'une montre
        // Les chemins changent mais celà ne change pas l'identité de la tuile
        // On a une formule pour calculer le nouveau point de sortie selon la rotation
        this.rotation += 1;
        this.rotation = this.rotation % 4;
    }

    public Chemin trouverNouveauChemin(PlateauTuiles.Direction ancienPoint, int pointActuel) {
        int nouvelIndice = (ancienPoint.oppose().ordinal() * 2 + (pointActuel + 1) % 2) % 8;
        return getTableauChemins()[(nouvelIndice-(2*getRotation()))%8];
    }

    public class Chemin {
    
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
// TODO : La formule de calcul du point de sortie en fonction de la rotation
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
            int tmp = tableauChemins[indiceChemin].getPointSortie();
            tableauChemins[tmp].setCouleur(couleur);// on doit aussi changer la couleur pour  tableauChemins[tmp] afin de gerer les doublons 
        }
    }
    

    public static void main(String[] args) {
        System.out.println();
    }
}


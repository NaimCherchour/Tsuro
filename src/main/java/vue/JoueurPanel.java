package main.java.vue;

import main.java.model.Joueur;

import javax.swing.*;
import java.awt.*;


/**
 * Cette classe représente l'interface utilisateur d'un joueur dans un jeu.
 */
public class JoueurPanel extends JPanel {
    //TODO : à voir si on doit appliquer le Observer Pattern ici

    private Joueur joueur;
    private static final int TAILLE_PION = 15; // La taille du pion du joueur
    private static final int MARGE_GAUCHE = 195;
    private static final int MARGE_TOP = 45;
    private static final int TUILE_SIZE = 120;
    //int posY = MARGE_TOP + joueur.getLigne() * TUILE_SIZE + 40 + new Random().nextInt(1) * 40;
    //int posX = MARGE_TOP + joueur.getColonne() * TUILE_SIZE + 40 + new Random().nextInt(1) * 40;

    /**
     * Constructeur de la classe JoueurUI.
     * @param joueur Le joueur associé à cette interface utilisateur.
     */
    public JoueurPanel(Joueur joueur) {
        this.joueur = joueur;
        setPreferredSize(new Dimension(TAILLE_PION, TAILLE_PION));
    }

    /**
     * Redessine le composant de l'interface utilisateur du joueur.
     * @param g L'objet Graphics utilisé pour dessiner le composant.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Calcul de la position horizontale aléatoire limitée au bord
        int posX = calculPosX(joueur);
        // Position verticale fixée sur la ligne du haut*/
        int posY = calculPosY(joueur);

        dessinerPion(g, posX, posY);
    }
    
    private int calculPosX(Joueur j){
        int posActuelle = j.getPointEntree();
        int posJoueurSurBoard = 0;
        if (posActuelle == 0 || posActuelle == 5){
            posJoueurSurBoard =38;
        }else if (posActuelle == 2 || posActuelle == 3){
            posJoueurSurBoard =118;
        }else if (posActuelle == 1 || posActuelle == 4){
            posJoueurSurBoard = 78;
        }else if (posActuelle == 6 || posActuelle == 7){
            posJoueurSurBoard = 0;
        }
        return MARGE_GAUCHE + j.getColonne() * TUILE_SIZE + posJoueurSurBoard;
    }

    private int calculPosY(Joueur j){
        int posActuelle = j.getPointEntree();
        int posJoueurSurBoard= 0;
        if (posActuelle == 2 || posActuelle == 7){
            posJoueurSurBoard =38;
        }else if (posActuelle == 4 || posActuelle == 5){
            posJoueurSurBoard = 118;
        }else if (posActuelle == 3 || posActuelle == 6){
            posJoueurSurBoard = 78;
        }else if (posActuelle == 0 || posActuelle == 1){
            posJoueurSurBoard = 0;
        }
        return MARGE_TOP + j.getLigne() * TUILE_SIZE + posJoueurSurBoard;
    }

    /**
     * Dessine le pion du joueur à une position spécifique.
     *
     * @param g L'objet Graphics utilisé pour dessiner le pion.
     * @param x La coordonnée x de la position du pion.
     * @param y La coordonnée y de la position du pion.
     */
    private void dessinerPion(Graphics g, int x, int y) {
        // Adaptez la couleur et la forme du pion ici si nécessaire
        Color c = convertirCouleur(joueur.getCouleur());
        g.setColor(c); // Couleur rouge
        g.fillOval(x, y, TAILLE_PION, TAILLE_PION); // Dessine un cercle de diamètre 50 (vous pouvez ajuster la taille selon vos besoins)
    }

    /**
     * Convertit l'énumération Couleur en Color de Java.
     *
     * @param couleur La couleur de l'énumération du joueur.
     * @return La couleur correspondante en objet Color de Java.
     */
    private Color convertirCouleur(Joueur.Couleur couleur) {
        return switch (couleur) {
            case NOIR -> Color.BLACK;
            case ROUGE -> Color.RED;
            case BLEU -> Color.BLUE;
            case VERT -> Color.GREEN;
            case JAUNE -> Color.YELLOW;
            case ORANGE -> Color.ORANGE;
            case ROSE ->new Color(220, 12, 253); //rose
            case CYAN -> Color.CYAN;
            case VIOLET -> new Color(128, 0, 128); // Violet
        };
    }


    /**
     * Définit le joueur associé à cette interface utilisateur.
     * @param joueur Le joueur associé.
     */
    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        this.repaint();
    }


}

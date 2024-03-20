package main.java.vue;

import main.java.model.Joueur;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Cette classe représente l'interface utilisateur d'un joueur dans un jeu.
 */
public class JoueurPanel extends JPanel implements PropertyChangeListener {
    private Joueur joueur;
    private static final int TAILLE_PION = 15; // La taille du pion du joueur
    private static final int MARGE_GAUCHE = 195;
    private static final int MARGE_TOP = 45;
    private static final int TUILE_SIZE = 120;

    /**
     * Constructeur de la classe JoueurUI.
     *
     * @param joueur Le joueur associé à cette interface utilisateur.
     */
    public JoueurPanel(Joueur joueur) {
        this.joueur = joueur;
        joueur.addPropertyChangeListener(this);
        setPreferredSize(new Dimension(TAILLE_PION, TAILLE_PION));
    }

    /**
     * Redessine le composant de l'interface utilisateur du joueur.
     *
     * @param g L'objet Graphics utilisé pour dessiner le composant.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dessinerPion(g,  calculPosX(joueur),  calculPosY(joueur));
    }
    private int calculPosX(Joueur j){
        int posActuelle = j.getEntree();
        int posJoueurSurBoard = 0;

        return MARGE_GAUCHE + j.getColonne() * TUILE_SIZE + posJoueurSurBoard;
    }

    private int calculPosY(Joueur j){
        int posActuelle = j.getEntree();
        int posJoueurSurBoard=0;
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
            case ROSE -> Color.PINK;
        };
    }

    /**
     * Gère les modifications de propriété du joueur.
     *
     * @param evt L'événement de changement de propriété.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Supposons que la propriété qui change est la position du joueur
        if ("position".equals(evt.getPropertyName())) {
            this.repaint(); // redessine le pion avec la nouvelle position
        }
    }

    /**
     * Définit le joueur associé à cette interface utilisateur.
     *
     * @param joueur Le joueur associé.
     */
    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        joueur.addPropertyChangeListener(this);
        this.repaint();
    }

    /**
     * Méthode principale utilisée pour tester cette classe.
     *
     * @param args Les arguments de la ligne de commande (non utilisés dans cette application).
     */
    public static void main(String[] args) {
        // Créer un nouveau joueur.
        Joueur joueur = new Joueur(3, 4, 2, "Max");

        // Créer la fenêtre.
        JFrame frame = new JFrame("Test JoueurPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Créer l'UI du joueur.
        JoueurPanel JoueurPanel = new JoueurPanel(joueur);

        // Ajouter l'UI du joueur à la fenêtre.
        frame.add(JoueurPanel);

        // Centrer le pion du joueur dans la fenêtre (à titre d'exemple).
        JoueurPanel.setLocation(390, 290); // À ajuster en fonction de la position réelle du joueur sur le plateau.

        // Afficher la fenêtre.
        frame.setVisible(true);

        // Pour tester la mise à jour de la position:
        // Modifier la position du joueur après un certain délai.
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(2000); // Attendre 2 secondes.
                joueur.setLigne(4); // Nouvelle position.
                joueur.setColonne(5);
                //joueurUI.updatePosition(joueur);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

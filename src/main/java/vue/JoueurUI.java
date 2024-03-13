package main.java.vue;

import main.java.model.Joueur;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class JoueurUI extends JPanel implements PropertyChangeListener {
    private Joueur joueur;
    private static final int TAILLE_PION = 90; // La taille du pion du joueur

    public JoueurUI(Joueur joueur) {
        this.joueur = joueur;
        joueur.addPropertyChangeListener(this);
        setPreferredSize(new Dimension(TAILLE_PION, TAILLE_PION));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dessinerPion(g, joueur.getColonne() * 10 + 60, joueur.getLigne() * 10 + 100);
    }

    private void dessinerPion(Graphics g, int x, int y) {
        // Adaptez la couleur et la forme du pion ici si nécessaire
        g.setColor(Color.RED); // Couleur rouge
        g.fillOval(x, y, 10, 10); // Dessine un cercle de diamètre 50 (vous pouvez ajuster la taille selon vos besoins)
    }
    

    // Convertit l'énumération Couleur en Color de Java
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Supposons que la propriété qui change est la position du joueur
        if ("position".equals(evt.getPropertyName())) {
            this.repaint(); // redessine le pion avec la nouvelle position
        }
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        joueur.addPropertyChangeListener(this);
        this.repaint();
    }


    public static void main(String[] args) {
        // Créer un nouveau joueur.
        Joueur joueur = new Joueur(3, 4, 2, "Max");

        // Créer la fenêtre.
        JFrame frame = new JFrame("Test JoueurUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Créer l'UI du joueur.
        JoueurUI joueurUI = new JoueurUI(joueur);

        // Ajouter l'UI du joueur à la fenêtre.
        frame.add(joueurUI);

        // Centrer le pion du joueur dans la fenêtre (à titre d'exemple).
        joueurUI.setLocation(390, 290); // À ajuster en fonction de la position réelle du joueur sur le plateau.

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
package main.java.vue;

import main.java.model.ReadOnlyTuile;
import main.java.model.Tuile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TuilePanel extends JPanel {

    private final DessinateurDeTuile dessinateur; // le dessinateur de Tuile
    private final ReadOnlyTuile tuile; // La tuile à dessiner
    private static final int TILE_SIZE = 120;

    // getter
    public ReadOnlyTuile getTuile() {
        return tuile;
    }

    // Un panel de Tuile est un JPanel qui affiche une tuile.
    public TuilePanel(ReadOnlyTuile tuile) throws IOException {
        this.tuile = tuile;
        this.dessinateur = new DessinateurDeTuile();
        setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE)); // Taille préférée du panneau de la tuile
        repaint();
    }

    /**
     * Dessine la tuile dans un rectangle gris avec une bordure noire.
     * 
     * @param g
     */
    private void drawTile(Graphics g) {
        if(tuile.getId() == 19 || tuile.getId() == 27){
            g.setColor(new Color(255, 215, 0));
        }
        else{
            g.setColor(new Color(52, 82, 110));
        }
        g.fillRoundRect(0, 0, TILE_SIZE, TILE_SIZE, 10, 10); // Fill rectangle representing the tile
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, TILE_SIZE, TILE_SIZE, 10, 10); // Draw outline of the tile
        // Draw the tile's image
        dessinateur.dessinerTuile(g, tuile, dessinateur.getSpritesSet(), 0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTile(g);
    }
}

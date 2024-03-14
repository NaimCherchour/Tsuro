package main.java.vue;

import main.java.model.Tuile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TuilePanel extends JPanel {

    private final DessinateurDeTuile dessinateur;
    private final Tuile tuile;

    public TuilePanel() throws IOException {
        this.tuile = new Tuile(1, new int[]{1, 0, 3, 2, 5, 4, 7, 6}) ;
        this.dessinateur= new DessinateurDeTuile();
        setPreferredSize(new Dimension(120, 120)); // Taille préférée du panneau de la tuile
        repaint();    }

    private void drawTile(Graphics g, int size) {
        // Example method to draw a tile with a button below it
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, size, size); // Fill rectangle representing the tile
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, size, size); // Draw outline of the tile
        // Draw the tile's image
        dessinateur.dessinerTuile(g, tuile, dessinateur.getSpritesSet(), 0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 120, 120);

        for (int i = 0; i < 3; i++) {
            drawTile(g, 120);
        }
    }
}

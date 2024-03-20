package main.java.vue;

import main.java.model.Tuile;
import main.java.model.TuilesGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class TuilePanel extends JPanel {

    private final DessinateurDeTuile dessinateur;
    private final Tuile tuile;

    //getter
    public Tuile getTuile() {
        return tuile;
    }

    public TuilePanel() throws IOException {
        Random random = new Random();
        int i = random.nextInt(35) + 1;
        List<Tuile> tuiles = TuilesGenerator.genererToutesLesTuiles();
        this.tuile = tuiles.get(i);
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
        drawTile(g, 120);

    }
}

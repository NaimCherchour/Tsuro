package main.java.vue;

import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class TestDessinateurDeTuile {

    public static void drawRotatedSprite(Graphics g, BufferedImage sprite, int x, int y,int h , int w, double angle) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Trouver le centre du sprite pour la rotation
        int centerX = sprite.getWidth() /2 ;
        int centerY = sprite.getHeight() /2 ;

        // h and w pour la taille du sprite

        AffineTransform transform = new AffineTransform(); // pour la rotation du sprite
        transform.rotate(angle, x + centerX, y + centerY);

        // Appliquer la transformation et dessiner le sprite
        g2d.setTransform(transform);
        g2d.drawImage(sprite, x, y, null);
        g2d.dispose();
    }





    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Dessinateur De Tuile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DessinateurDeTuile dessinateur = null;
        try {
            dessinateur = new DessinateurDeTuile(120, 120,  7);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // exemple
        int[] tuile = {1, 0, 3, 2, 5, 4, 7, 6};

        DessinateurDeTuile Dessinateur = dessinateur;
        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < tuile.length; i += 2) { // Parcourir les paires de connexions
                   // int indexSprite = tuile[i]; // Obtenir l'index pour le sprite correspondant à la paire de connexion
                    BufferedImage sprite = Dessinateur.getSprite(1);
                    g.drawImage(sprite, 120,120,70,70,this);
                    drawRotatedSprite(g, sprite, 120, 120,70,70, Math.PI ); // Pour le côté droit

                }
            }
        };

        frame.add(panel);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}


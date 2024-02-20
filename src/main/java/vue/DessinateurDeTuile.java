package main.java.vue;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DessinateurDeTuile extends JPanel {
    private BufferedImage spritesCollection;
    private int spriteWidth;
    private int spriteHeight;
    private int spritesPerRow;

    public DessinateurDeTuile(int spriteWidth, int spriteHeight, int spritesPerRow) throws IOException {
        try {
           this.spritesCollection = ImageIO.read(new File("src/main/resources/sprites_tsuro.png")); }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spritesPerRow = spritesPerRow; // 7 on each line of the png
    }

    //TODO : méthode principale qui permettera de dessiner les tuiles selon les connexions et les rotations et set de sprites
    public void dessinerTuile(Graphics g, int[] connexions, int rotations, BufferedImage sprites) {
       // int[] nouvellesConnexions = tournerTuile(connexions, rotations);

        // Pour chaque connexion dans la tuile
       // for (int i = 0; i < nouvellesConnexions.length; i++) {
            // Calculer la position et la rotation du sprite
          //  int spriteX = /* calculer la position X du sprite */;
          //  int spriteY = /* calculer la position Y du sprite */;
         //   double angleRotation = /* calculer l'angle de rotation basé sur i et nouvellesConnexions[i] */;

            // Extraire le sprite du set
         //   BufferedImage sprite = sprites.getSubimage(spriteX, spriteY, spriteWidth, spriteHeight);

            // Effectuer la rotation du sprite si nécessaire
         //   BufferedImage spriteTourne = tournerSprite(sprite, angleRotation);

            // Dessiner le sprite sur la tuile à la position calculée et avec la rotation correspendante
          //  g.drawImage(spriteTourne, /* position X */, /* position Y */, null);
        }


    // Fonction pour calculer les connexions après rotation deja faite dans la classe Tuiles


    //TODO à remplir avec la version du test
    public BufferedImage tournerSprite(BufferedImage sprite, double angle) {
        // Effectuer la rotation de l'image est dans le test

        return null ;
    }

    public BufferedImage getSprite(int index) {
        int x = (index % spritesPerRow) * spriteWidth;
        int y = (index / spritesPerRow) * spriteHeight;
        return spritesCollection.getSubimage(x, y, spriteWidth, spriteHeight);
    }


    private int calculerIndexSprite(int connection, int rotation) {

        return 0;
    }
}



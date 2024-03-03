package main.java.vue;

import main.java.model.Tuile; // Relation Observer entre la vue et le model

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import static main.java.model.Tuile.TAILLE_DU_TABLEAU;

public class DessinateurDeTuile extends JPanel {
    private BufferedImage spritesSet;
    private final int SPRITE_WIDTH= 120;
    private final int SPRITE_HEIGHT = 120;
    private final int SPRITES_PER_ROW = 7;

    private BufferedImage getSpritesSet() {
        return spritesSet;
    }

    public DessinateurDeTuile() throws IOException {
        try {
           this.spritesSet = ImageIO.read(new File("src/main/resources/sprites_tsuro.png")); }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO : méthode principale qui permettra de dessiner les tuiles selon les connexions et les rotations
    public void dessinerTuile(Graphics g, Tuile tuile, BufferedImage sprites) {

        for (int i = 0; i < TAILLE_DU_TABLEAU /2; i++) {
            int connection = tuile.getTableauChemins()[i].getPointSortie(); // le point de sortie selon la rotation
            int color = tuile.getTableauChemins()[i].getCouleur().ordinal(); // le Cardinal de La couleur
            int rotation = tuile.getRotation(); // La rotation de la tuile
            int indexSprite;

            // Calculer l'indice du chemin opposé dans le tableau
            int oppositeIndex = (i + TAILLE_DU_TABLEAU / 2) % TAILLE_DU_TABLEAU;
            // Utiliser l'indice du chemin opposé pour éviter les doublons
            int oppositeConnection = tuile.getTableauChemins()[oppositeIndex].getPointSortie();


            switch (Math.abs(connection - i)) {
                case 1:
                    indexSprite = 0;
                    break;
                case 2:
                    indexSprite = 1;
                    break;
                case 3:
                    indexSprite = 2;
                    break;
                case 4:
                    indexSprite = 3;
                    break;
                case 5:
                    indexSprite = 4;
                    break;
                case 6:
                    indexSprite = 5;
                    break;
                case 7:
                    indexSprite = 6;
                    break;
                default:
                    indexSprite = -1;
                    break;
            }
            // Calculer la position du Sprite à extraire du Set
            int spriteX = indexSprite * SPRITE_WIDTH;
            int spriteY = color * SPRITE_HEIGHT;
            System.out.println("SpriteX: " + spriteX + " SpriteY: " + spriteY + " Color: " + color + " IndexSprite: " + indexSprite + " Connection: " + connection + " i: " + i + " Rotation: " + rotation);

            // Extraire le sprite du set
            BufferedImage sprite = sprites.getSubimage(spriteX, spriteY, SPRITE_WIDTH, SPRITE_HEIGHT);

            // Calculer la position et la rotation du sprite
            //int spriteX = /* calculer la position X du sprite */;
            //int spriteY = /* calculer la position Y du sprite */;
            // double angleRotation = /* calculer l'angle de rotation basé sur i et nouvellesConnexions[i] */;


            // Effectuer la rotation du sprite si nécessaire
            // BufferedImage spriteTourne = tournerSprite(sprite, angleRotation);

            g.drawImage(sprite, i*22, 0, null);
            // Vérifier s'il y a un chemin opposé pour éviter les doublons
            if (oppositeConnection == connection) {
                // Si le chemin opposé est identique, dessiner le même sprite à la position opposée
                g.drawImage(sprite, (oppositeIndex) * 15, 0, null);
            }
        }
    }

    public static void main(String[] args) {
        // Création d'une nouvelle tuile à dessiner (vous devez avoir votre propre logique pour initialiser la tuile)
        Tuile tuile = new Tuile(1, new int[]{3, 7,4, 0, 2, 6, 5, 1});

        // Création d'un nouveau dessinateur de tuile
        DessinateurDeTuile dessinateur;
        try {
            dessinateur = new DessinateurDeTuile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Création d'un nouveau frame pour afficher la tuile
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Création d'un nouveau JPanel pour dessiner la tuile
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Appel de la méthode dessinerTuile du dessinateur avec la tuile à dessiner
                dessinateur.dessinerTuile(g, tuile, dessinateur.getSpritesSet());
            }
        };

        // Ajout du JPanel au frame et affichage du frame
        frame.add(panel);
        frame.setVisible(true);
    }



}



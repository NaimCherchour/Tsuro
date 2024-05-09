package main.java.vue;

import main.java.model.Tuile;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;

import main.java.model.PlateauTuiles.Direction;
import static main.java.model.PlateauTuiles.Direction.*;
import static main.java.model.Tuile.TAILLE_DU_TABLEAU;
import static main.java.vue.DessinateurDeTuile.ImageFlip.drawFlippedImage;


public class DessinateurDeTuile extends JPanel {

    //TODO : à voir si on doit appliquer le Observer Pattern ici

    private BufferedImage spritesSet;
    private final int SPRITE_WIDTH= 120;
    private final int SPRITE_HEIGHT = 120;
    private Set<Pair<Integer, Integer>> drawnConnections = new HashSet<>(); // POUR ÉVITER LES DOUBLONS

    public BufferedImage getSpritesSet() {
        return spritesSet;
    }

    public DessinateurDeTuile() throws IOException {
        try {
            this.spritesSet = ImageIO.read(new File("src/main/resources/sprites/sprites_tsuro.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDrawnConnections(Set<Pair<Integer, Integer>> drawnConnections) {
        this.drawnConnections = drawnConnections;
    }

    // Méthode principale pour dessiner une tuile

    public void dessinerTuile(Graphics g, Tuile tuile, BufferedImage sprites, int x, int y) {
        drawnConnections.clear(); // Clear the set before drawing each tile
        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
           // int connection = tuile.getPointSortieAvecRot(i);
            int connection = tuile.getPointSortieAvecRot(i);
            if (!isConnectionDrawn(i, connection)) { // Check if connection is already drawn
                int color = tuile.getTableauChemins()[i].getCouleur().ordinal();
                double rotation = calculateRotationAngle(connection, i);
                int indexSprite = getIndexSprite(connection, i);
                BufferedImage sprite = sprites.getSubimage(indexSprite * SPRITE_WIDTH, color * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
                drawPath(g, i, connection, sprite, rotation, x, y);
                markConnectionAsDrawn(i, connection); // Mark the connection as drawn
            }
        }
    }





    private boolean isConnectionDrawn(int from, int to) {
        return drawnConnections.contains(new Pair<>(from, to)) || drawnConnections.contains(new Pair<>(to, from));
    }

    private void markConnectionAsDrawn(int from, int to) {
        drawnConnections.add(new Pair<>(from, to));
    }

    /**
     * @param g pour
     * @param from est le point d'entrée
     * @param to est le point de sortie
     * @param sprite est le sprite extrait à dessiner
     * @param rotation est l'angle de Rotation
     */
    public void drawPath(Graphics g, int from, int to, BufferedImage sprite, double rotation,int x,int y) {
        BufferedImage transformedSprite;

        if (mirrorNeeded(from, to)) {
            transformedSprite = drawFlippedImage(g, sprite, x, y);

            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(rotation), SPRITE_WIDTH / 2.0, SPRITE_HEIGHT / 2.0); // Rotate around the center of the sprite

            // Create a new BufferedImage for the rotated sprite
            BufferedImage rotatedSprite = new BufferedImage(SPRITE_WIDTH, SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rotatedSprite.createGraphics();
            g2d.setTransform(transform);
            g2d.drawImage(transformedSprite, 0, 0, null);
            g2d.dispose();
            g.drawImage(rotatedSprite,x,y,null);
        } else {
            transformedSprite = sprite;
            // Apply rotation using AffineTransform
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(rotation), SPRITE_WIDTH / 2.0, SPRITE_HEIGHT / 2.0); // Rotate around the center of the sprite

            // Create a new BufferedImage for the rotated sprite
            BufferedImage rotatedSprite = new BufferedImage(SPRITE_WIDTH, SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rotatedSprite.createGraphics();
            g2d.setTransform(transform);
            g2d.drawImage(transformedSprite, 0, 0, null);
            g2d.dispose();
            g.drawImage(rotatedSprite,x,y,null);
        }

    }

    private boolean mirrorNeeded(int from, int to) {
        // C'est le cas ou l'entrée est impair
        int enter = Math.min(from,to);
        return enter % 2 == 1 ;
    }





    // Méthode pour obtenir l'indice du sprite en fonction de la connexion et de l'indice du chemin
    private int getIndexSprite(int connection, int index) {
        Direction first = getDirectionFromPoint(index);
        Direction other = getDirectionFromPoint(connection);

        int enter = Math.min(connection, index);
        int out = Math.max(connection,index);
        int diff = Math.abs(connection - index);
        if (enter == 0 ) {
            return out-1;
        } else if ( enter == 1) {
            if (diff == 1 && first == other ) {
                return 0; }
            else if ( (diff == 6 && adjacent(first,other)) ) {
                return 1;
            } else if (diff == 5  && adjacent(first,other) ) {
                return 2; }
            else if (diff == 4 && opposite(first,other) ){
                return 3;
            } else if ( (diff == 3 ) && opposite(first,other) ){
                return 4;
            } else if (diff == 2 && adjacent(first,other) ) {
                return 5;
            } else if ( diff == 1 && adjacent(first,other) ) {
                return 6; }
        } else if ( enter == 2 || enter == 4 ) {
            return diff -1 ;
        } else if ( enter == 3 ) {
            if (diff == 1 && adjacent(first,other)) {
                return 6;
            } else if (diff == 4 && opposite(first,other) ){
                return 3;
            } else if ( diff == 3  && opposite(first,other) ){
                return 4;
            } else if (diff == 2 && adjacent(first,other) ) {
                return 1;}
        }  else if ( enter == 5 ) {
            if ( diff == 2 && adjacent(first,other)){
                return 1;
            } else if ( diff == 1 && adjacent(first,other)){
                return 6;
            }
        } else if ( enter == 6 ) {
            if ( diff == 1 && first == other ){
                return 0 ;
            }
        }
        return 0;
    }


    private double calculateRotationAngle(int connection, int index) {
        Direction first = getDirectionFromPoint(index); // La direction d'entrée
        Direction other = getDirectionFromPoint(connection); // La direction de sortie
        // Calculer la Différence entre les deux
        int diff = Math.abs(connection - index);

        int enter = Math.min(connection, index);
        int out = Math.max(connection,index);
        if ( enter == 2 ) { // aucun mirroir
            return 90 ;
        } else if ( enter == 3 ) {  // il y aura un mirroir
            if (diff == 1 && adjacent(first,other)) {
                return 90;
            } else if (diff == 4 && opposite(first,other) ){
                return 90;
            } else if ( diff == 3  && opposite(first,other) ){
                return 90;
            } else if (diff == 2 && adjacent(first,other) ) {
                return 180;}
        } else if ( enter == 4 ){
           return 180 ;
        } else if (enter == 5 ) {
            if ( diff == 1 && adjacent(first,other)){
                return 180;
            } else if ( diff == 2 && adjacent(first,other)){
                return 270;
            }
        } else if ( enter == 6 ) {
            if ( diff == 1 && first == other){
                return 270;
            }
        }
        return 0;
    }

    public class Pair<A, B> {
        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            return Objects.equals(first, pair.first) &&
                    Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }


    public static class ImageFlip {
        public static BufferedImage drawFlippedImage(Graphics g, BufferedImage image, int x, int y) {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate( -image.getHeight(null),0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            return op.filter(image, null);
        }
    }




}



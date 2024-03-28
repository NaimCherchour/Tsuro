package main.java.vue;

import main.java.model.Joueur;
import main.java.model.Tuile; // Relation Observer entre la vue et le model
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

    // todo : passer le model en attribut
    private BufferedImage spritesSet;
    private final int SPRITE_WIDTH= 120;
    private final int SPRITE_HEIGHT = 120;
    private Set<Pair<Integer, Integer>> drawnConnections = new HashSet<>(); // POUR ÉVITER LES DOUBLONS

    public BufferedImage getSpritesSet() {
        return spritesSet;
    }

    public DessinateurDeTuile() throws IOException {
        try {
            this.spritesSet = ImageIO.read(new File("src/main/resources/sprites_tsuro.png"));
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


    public void test(Graphics g) {
        BufferedImage sprite = spritesSet.getSubimage(4 * SPRITE_WIDTH, 0 * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        BufferedImage i = drawFlippedImage(g, sprite, 0, 0);
        g.drawImage(i,0,0,null);
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
            if (diff == 1 && sameSide(first,other)) {
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
            if ( diff == 1 && sameSide(first,other)){
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
            if ( diff == 1 && sameSide(first,other)){
                return 270;
            }
        }
        return 0;
    }




        public static void main(String[] args) {

        Tuile tuile1 = new Tuile(1,new int[]{5,4,7,6,1,0,3,2});

        HashMap<Integer, Tuile> tuiles = new HashMap<>();

        tuiles.put(1, new Tuile(1, new int[]{5, 6, 4, 7, 2, 0, 1, 3})); //Correct
        tuiles.put(2, new Tuile(2, new int[]{1, 0, 4, 7, 2, 6, 5, 3})); //Correct
        tuiles.put(3, new Tuile(3, new int[]{1, 0, 4, 6, 2, 7, 3, 5})); //Correct
        tuiles.put(4, new Tuile(4, new int[]{2, 5, 0, 7, 6, 1, 4, 3})); //Correct
        tuiles.put(5, new Tuile(5, new int[]{4, 2, 1, 6, 0, 7, 3, 5})); //Correct
        tuiles.put(6, new Tuile(6, new int[]{1, 0, 5, 7, 6, 2, 4, 3})); //Correct
        tuiles.put(7, new Tuile(7, new int[]{2, 4, 0, 6, 1, 7, 3, 5})); //Correct
        tuiles.put(8, new Tuile(8, new int[]{4, 7, 5, 6, 0, 2, 3, 1})); //FAUSSE 1->7
        tuiles.put(9, new Tuile(9, new int[]{1, 0, 7, 6, 5, 4, 3, 2})); // Correct
        tuiles.put(10, new Tuile(10, new int[]{4, 5, 6, 7, 0, 1, 2, 3}));//correct
        tuiles.put(11, new Tuile(11, new int[]{7, 2, 1, 4, 3, 6, 5, 0}));//correct
        tuiles.put(12, new Tuile(12, new int[]{2, 7, 0, 5, 6, 3, 4, 1})); //FAUSSE
        tuiles.put(13, new Tuile(13, new int[]{5, 4, 7, 6, 1, 0, 3, 2})); // Correct
        tuiles.put(14, new Tuile(14, new int[]{3, 2, 1, 0, 7, 6, 5, 4}));
        tuiles.put(15, new Tuile(15, new int[]{1, 0, 7, 4, 3, 6, 5, 2}));
        tuiles.put(16, new Tuile(16, new int[]{1, 0, 5, 6, 7, 2, 3, 4}));
        tuiles.put(17, new Tuile(17, new int[]{3, 5, 6, 0, 7, 1, 2, 4}));
        tuiles.put(18, new Tuile(18, new int[]{2, 7, 0, 4, 3, 6, 5, 1}));
        tuiles.put(19, new Tuile(19, new int[]{4, 3, 6, 1, 0, 7, 2, 5}));
        tuiles.put(20, new Tuile(20, new int[]{3, 7, 4, 0, 2, 6, 5, 1}));
        tuiles.put(21, new Tuile(21, new int[]{2, 3, 0, 1, 7, 6, 5, 4}));
        tuiles.put(22, new Tuile(22, new int[]{2, 6, 0, 5, 7, 3, 1, 4}));
        tuiles.put(23, new Tuile(23, new int[]{1, 0, 6, 4, 3, 7, 2, 5}));
        tuiles.put(24, new Tuile(24, new int[]{5, 6, 7, 4, 3, 0, 1, 2}));
        tuiles.put(25, new Tuile(25, new int[]{1, 0, 3, 2, 7, 6, 5, 4}));
        tuiles.put(26, new Tuile(26, new int[]{1, 0, 6, 7, 5, 4, 2, 3}));
        tuiles.put(27, new Tuile(27, new int[]{2, 4, 0, 7, 1, 6, 5, 3}));
        tuiles.put(28, new Tuile(28, new int[]{4, 2, 1, 7, 0, 6, 5, 3}));
        tuiles.put(29, new Tuile(29, new int[]{1, 0, 3, 2, 5, 4, 7, 6}));
        tuiles.put(30, new Tuile(30, new int[]{2, 3, 0, 1, 6, 7, 4, 5}));
        tuiles.put(31, new Tuile(31, new int[]{3, 6, 5, 0, 7, 2, 1, 4}));
        tuiles.put(32, new Tuile(32, new int[]{1, 0, 6, 5, 7, 3, 2, 4}));
        tuiles.put(33, new Tuile(33, new int[]{1, 0, 3, 2, 6, 7, 4, 5}));
        tuiles.put(34, new Tuile(34, new int[]{4, 5, 7, 6, 0, 1, 3, 2}));
        tuiles.put(35, new Tuile(35, new int[]{1, 0, 7, 5, 6, 3, 4, 2}));


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
        frame.setSize(1400, 1000);

            tuiles.get(1).tournerTuile();
            //tuiles.get(1).tournerTuile();
            //tuiles.get(1).tournerTuile();

        // Création d'un nouveau JPanel pour dessiner la tuile
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //dessinateur.test(g);
                // Appel de la méthode dessinerTuile du dessinateur avec la tuile à dessiner

                for (int i = 1; i <= 10 ; i++) {
                    dessinateur.dessinerTuile(g, tuiles.get(i), dessinateur.getSpritesSet(), (i-1)*120 + 20*i  , 0);

                }
            }
        };



            // Ajout du JPanel au frame et affichage du frame
        frame.add(panel);
        frame.setVisible(true);
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



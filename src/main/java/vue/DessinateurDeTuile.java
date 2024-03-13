package main.java.vue;

import main.java.model.Joueur;
import main.java.model.Tuile; // Relation Observer entre la vue et le model
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;

import main.java.model.PlateauTuiles.Direction;

import static main.java.model.PlateauTuiles.Direction.*;
import static main.java.model.Tuile.TAILLE_DU_TABLEAU;

public class DessinateurDeTuile extends JPanel {
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


    /**
     *
     * @param g pour
     * @param from est le point d'entrée
     * @param to est le point de sortie
     * @param sprite est le sprite extrait à dessiner
     * @param rotation est l'angle de Rotation
     */
    private void drawPath(Graphics g, int from, int to, BufferedImage sprite, double rotation) {

        System.out.println("I draw a path");

        BufferedImage transformedSprite;
        // Check if mirroring is needed
        if (mirrorNeeded(from, to)) {
            // Create a new AffineTransform for mirroring Vertically
            AffineTransform mirrorTransform = AffineTransform.getScaleInstance(1, -1);
            mirrorTransform.translate(0, -SPRITE_HEIGHT);

            // Apply mirroring
            transformedSprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = transformedSprite.createGraphics();
            g2d.setTransform(mirrorTransform);
            g2d.drawImage(sprite, 0, 0, null);
            g2d.dispose();
        } else {
            transformedSprite = sprite; // Sans Mirroir
        }

        // Apply rotation using AffineTransform
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotation), SPRITE_WIDTH / 2.0, SPRITE_HEIGHT / 2.0); // Rotate around the center of the sprite

        // Create a new BufferedImage for the rotated sprite
        BufferedImage rotatedSprite = new BufferedImage(SPRITE_WIDTH, SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedSprite.createGraphics();
        g2d.setTransform(transform);
        g2d.drawImage(transformedSprite, 0, 0, null);
        g2d.dispose();

        int enter = Math.min(from,to);
        int diff = Math.abs(from - to);
        Direction first = getDirectionFromPoint(from);
        Direction other = getDirectionFromPoint(to);
        // Cas Spécial 4->1 et 6->3 on doit avancer le le x et le y initiale
        if ( diff == 3 && opposite(first,other)) {
            if ( enter == 1 ) {
                g.drawImage(rotatedSprite,40,0,null);
            } else {
                g.drawImage(rotatedSprite,0,40,null);
            }
        } else {
            g.drawImage(rotatedSprite,0,0,null); // sinon 0 et 0
        }

    }

    private boolean mirrorNeeded(int from, int to) {
        // C'est le cas ou les deux points sont impairs
        return from % 2 == 1 && to % 2 == 1;
    }

    // Method to draw the entire tile
    public void dessinerTuile(Graphics g, Tuile tuile, BufferedImage sprites) {
        drawnConnections.clear(); // Clear the set before drawing each tile


        for (int i = 0; i < TAILLE_DU_TABLEAU; i++) {
            int connection = tuile.getTableauChemins()[i].getPointSortie(); // OBSERVER !!!!
            int color = tuile.getTableauChemins()[i].getCouleur().ordinal();
            double rotation = calculateRotationAngle(connection,i);
            int indexSprite = getIndexSprite(connection, i);
            BufferedImage sprite = sprites.getSubimage(indexSprite * SPRITE_WIDTH, color * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
            drawPath(g, i, connection, sprite,rotation);
        }
    }



    // Méthode pour obtenir l'indice du sprite en fonction de la connexion et de l'indice du chemin
    private int getIndexSprite(int connection, int index) {
        int diff = Math.abs(connection - index);
        Direction first = getDirectionFromPoint(index);
        Direction other = getDirectionFromPoint(connection);
        if (diff == 1 && sameSide(first,other)) {
            System.out.println("0");
            return 0;
        } else if ( (diff == 1 && adjacent(first,other)) || diff == 7  ) {
            System.out.println("6");
            return 6;
        } else if (diff == 2 && adjacent(first,other) && !(connection == 3 && index == 1 ) && !(index == 3 && connection == 1 )) {
            System.out.println("1");
            return 1;
        }else if ( (diff == 3 || diff == 5 ) && adjacent(first,other) ) {
            System.out.println("2");
            return 2;
        } else if (diff == 4 && opposite(first,other) ){
            System.out.println("3");
            return 3;
        } else if ( (diff == 5 || diff == 3 ) && opposite(first,other) ){
            System.out.println("4");
            return 4;
        } else if ( (diff == 6 && adjacent(first,other) ) || ( connection == 1 && index == 3 ) || ( connection == 3 && index == 1) ) {
            System.out.println("5");
            System.out.println("diff 6");
            return 5;
        } else {
            System.out.println("hors truc");
            return 0; }
    }

    private double calculateRotationAngle(int connection, int index) {
        Direction first = getDirectionFromPoint(index); // La direction d'entrée
        Direction other = getDirectionFromPoint(connection); // La direction de sortie

        // Calculer la Différence entre les deux
        int diff = Math.abs(connection - index);

        // Determine the angle of rotation based on the direction and entry point
        if ((diff == 1 && sameSide(first, other))) {
            return first.ordinal()*90;  // first or other would be the same because its on the same side
        } else if ((diff == 2 ) && adjacent(first, other)) {
            if ( ( connection == 5 && index == 7) || ( connection == 7 && index == 5) ) {
                return 90 ;
            } else if ( ( connection == 5 && index == 3) || ( connection == 3 && index == 5) ) {
                return 180;
            }
            System.out.println(Math.min(first.ordinal(),other.ordinal()) * 90);
            System.out.println(connection +"->"+index);
            return Math.min(first.ordinal(),other.ordinal()) * 90;
        } else if (diff == 4 && opposite(first, other)) {
            return Math.min(first.ordinal(),other.ordinal()) * 90;
        } else if ((diff == 5 || diff == 3) && opposite(first, other)) {
            return Math.min(first.ordinal(),other.ordinal())*90;
        } else if ( (diff == 3 || diff == 5 ) && adjacent(first,other) ){
            if ( diff == 5) {
                return 3*90;
            }
            return Math.min(first.ordinal(),other.ordinal())*90;
        } else if ( (diff == 1 && adjacent(first,other)) || diff == 7  ){
            if ( diff == 7 ) {
                return 0;
            }
            return Math.max(first.ordinal(),other.ordinal())*90;
        }
        else {
            return 0; // Default rotation (no rotation) diff = 6
        }
    }



    public static void main(String[] args) {
        // Création d'une nouvelle tuile à dessiner (vous devez avoir votre propre logique pour initialiser la tuile)
        Tuile tuile = new Tuile(1, new int[]{1,0,3,2,5,4,7,6});
        Tuile tuile2 = new Tuile (2,new int[]{6,3,4,1,2,7,0,5});
        // Tuile tuile2 = new Tuile(2,new int[]{2,7,0,5,6,3,4,1});
        //Tuile tuile3 = new Tuile (3,new int[]{6,5,4,7,2,1,0,3}); // prob
        //Tuile tuile4 = new Tuile(4,new int[]{5,4,7,6,1,0,3,2}); // prob startX and Y for the sprite
        //tuile4.getTableauChemins()[0].marquerCheminVisite(0, Joueur.Couleur.ROUGE);
        Tuile tuile5 = new Tuile (5,new int[]{4,5,6,7,0,1,2,3});
        Tuile tuile6 = new Tuile (6,new int[]{5,4,7,6,1,0,3,2});
        Tuile tuile7 = new Tuile (7,new int[]{7,2,1,4,3,6,5,0});
        Tuile tuile8 = new Tuile ( 8 , new int[]{3,6,5,0,7,2,1,4});
        Tuile tuile9 = new Tuile(9,new int[]{6,3,4,1,2,7,0,5});
        Tuile tuile10 = new Tuile(10,new int[]{2,7,0,5,6,3,4,1}); // FAUX 1->3 et 5->3

        HashMap<Integer, Tuile> tuiles = new HashMap<>();

        tuiles.put(1, new Tuile(1, new int[]{5, 6, 4, 7, 2, 0, 1, 3})); //Correct
        tuiles.put(2, new Tuile(2, new int[]{1, 0, 4, 7, 2, 6, 5, 3})); //Correct
        tuiles.put(3, new Tuile(3, new int[]{1, 0, 4, 6, 2, 7, 3, 5})); //Correct
        tuiles.put(4, new Tuile(4, new int[]{2, 5, 0, 7, 6, 1, 4, 3})); //Correct
        tuiles.put(5, new Tuile(5, new int[]{4, 2, 1, 6, 0, 7, 3, 5})); //Correct
        tuiles.put(6, new Tuile(6, new int[]{1, 0, 5, 7, 6, 2, 4, 3})); //Correct
        tuiles.put(7, new Tuile(7, new int[]{2, 4, 0, 6, 1, 7, 3, 5})); //Correct
        tuiles.get(7).getTableauChemins()[5].marquerCheminVisite(5, Joueur.Couleur.ROUGE);
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
        frame.setSize(400, 400);

        // Création d'un nouveau JPanel pour dessiner la tuile
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Appel de la méthode dessinerTuile du dessinateur avec la tuile à dessiner
                dessinateur.dessinerTuile(g, tuiles.get(2), dessinateur.getSpritesSet());

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






}



package main.java.vue;

import main.java.model.Joueur;
import main.java.model.PlateauTuiles;
import main.java.model.Tuile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameBoardUI extends JPanel implements MouseListener {
    private PlateauTuiles board;
    private Joueur joueur;
    DessinateurDeTuile dessinateurDeTuile;

    // getter
    public PlateauTuiles getBoard() {
        return board;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public GameBoardUI(Joueur j ) throws IOException {
        this.board = new PlateauTuiles(6);
        this.joueur = j;
        //setPreferredSize(new Dimension(1200, 800)); // Set preferred size of the panel
        this.dessinateurDeTuile = new DessinateurDeTuile();
        this.setBackground(Color.RED);
        addMouseListener(this);

    }

    public int[] calculDePosition(int x,int y){
        int X = 0 ;
        int Y = 0 ;
        while (x > 320 || y > 170){
            if (x > 320){
                x-=120;
                X+=1;
            }
            if (y > 170){
                y-=120;
                Y+=1;
            }
        }
        int t[]= {X,Y};
        return t;
    }


    public void mouseClicked(MouseEvent e) {
        int x = e.getX(); // Coordonnée x du clic
        int y = e.getY(); // Coordonnée y du clic
        int XY[]= calculDePosition( x, y);
        System.out.println("Coordonnées du clic : (" + XY[0] + ", " + XY[1] + ")");
    }
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private void drawTile(Graphics g, int x, int y, int size) {
        // Example method to draw a tile at a specified position and size
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, size, size); // Fill rectangle representing the tile
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size); // Draw outline of the tile
        // Draw the tile's image
        dessinateurDeTuile.dessinerTuile(g, board.getTuile((y-50)/120, (x-200)/120), dessinateurDeTuile.getSpritesSet(),x,y);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background image
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(200, 50, 720, 720); // Fill the board area with a rectangle

        // Draw tiles on the board
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board.getTuile(i, j) != null ) {
                    System.out.println(board.getTuile(i,j).getRotation()+ "   is the rotat");
                drawTile(g, 200+j * 120, 50+i * 120, 120);}
            }
        }

        drawPlayer(g, joueur);
    }

    public void drawPlayer (Graphics g , Joueur j ){
        JoueurPanel joueurUI = new JoueurPanel(j);
        joueurUI.paintComponent(g);
    }

    public void placerTuileSurPlateau(Joueur j , Tuile tuile) {
        int ligne = j.getLigne();
        int colonne = j.getColonne();
        board.placerTuile(ligne, colonne, tuile,j);
        repaint();  // Rafraîchir l'affichage
    }

    public void rotateTile(Tuile tuile) {
        tuile.tournerTuile();
        repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 850);
            frame.setResizable(false);

            frame.setLayout(new BorderLayout());
            JPanel panel = null;
            try {
                 Joueur j = new Joueur(0,0,2,"RED Max"); // juste pour afficher une couleur apart le rouge car le player 1 crée est la couleur RED
                Joueur j2 = new Joueur(1,0,2,"BLUE Max");
                panel = new GameBoardUI(j2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            frame.add(panel, BorderLayout.CENTER); // Add the panel to the center of the frame
            SidePanel sidePanel = null; // Initialize the side panel
            try {
                sidePanel = new SidePanel((GameBoardUI) panel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sidePanel.setPreferredSize(new Dimension(200, 600)); // Set fixed width and adjust height automatically
            frame.add(sidePanel, BorderLayout.EAST); // Add the side panel to the right
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            //frame.pack();
            frame.setVisible(true);
        });
    }


}


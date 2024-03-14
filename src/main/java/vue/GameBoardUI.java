package main.java.vue;

import main.java.model.Joueur;
import main.java.model.PlateauTuiles;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameBoardUI extends JPanel {
    private PlateauTuiles board;
    DessinateurDeTuile dessinateurDeTuile;

    public GameBoardUI() throws IOException {
        this.board = new PlateauTuiles(6);
        Joueur j = new Joueur(0,0,0,"BLEU");
        this.board.placerTuile(0, 0, new main.java.model.Tuile(1,new int[]{1, 0, 3, 2, 5, 4, 7, 6}),j);
        this.board.placerTuile(0, 1, new main.java.model.Tuile(1,new int[]{1, 0, 3, 2, 5, 4, 7, 6}),j);
        this.board.placerTuile(0, 2, new main.java.model.Tuile(1,new int[]{1, 0, 3, 2, 5, 4, 7, 6}),j);
        //setPreferredSize(new Dimension(1200, 800)); // Set preferred size of the panel
        this.dessinateurDeTuile = new DessinateurDeTuile();
        this.setBackground(Color.RED);

    }




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
    }

    public void rotateTile() {
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
                panel = new GameBoardUI();
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


package main.java.vue;

import main.java.model.Joueur;

import javax.swing.*;
import java.awt.*;

public class PlateauUI {

    private static final int GRID_SIZE = 6;
    private static final int TILE_SIZE = 120;
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 800;

    public PlateauUI(Joueur joueur) {
        JFrame frame = new JFrame("Plateau de jeu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());

        // Créer un JPanel qui servira de conteneur pour la grille et les boutons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setBackground(Color.GRAY);

        // Ajouter de l'espace autour de la grille
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // Créer le panel de la grille
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE, 0, 0));

        // Dans la boucle de création de la grille, ajoutez le joueur à la case correspondante
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
            ajouterJoueurSurPlateau(joueur, panel);
            gridPanel.add(panel);
        }

        // Créer le panel latéral pour les tuiles et les boutons
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        for (int i = 0; i < 3; i++) {
            JPanel tilePanel = new JPanel();
            tilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            Component rigidArea = Box.createRigidArea(new Dimension(TILE_SIZE, TILE_SIZE));
            tilePanel.add(rigidArea);

            JButton rotateButton = new JButton("Rotate");
            rotateButton.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE / 4));
            // Aligner le bouton rotate au centre horizontalement
            rotateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Ajouter le tilePanel et le bouton rotate au sidePanel
            sidePanel.add(tilePanel);
            sidePanel.add(rotateButton);
        }

        mainPanel.add(gridPanel);
        mainPanel.add(Box.createHorizontalStrut(50)); // Espace entre grille et panel latéral
        mainPanel.add(sidePanel);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Méthode pour ajouter le joueur à la case de la grille
    private void ajouterJoueurSurPlateau(Joueur joueur, JPanel panel) {
        JoueurUI joueurUI = new JoueurUI(joueur);
        panel.add(joueurUI);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer un nouveau joueur.
            Joueur joueur = new Joueur(3, 4, 2, "Max");

            // Créer la fenêtre du plateau avec le joueur.
            new PlateauUI(joueur);
        });
    }
}

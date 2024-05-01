package main.java.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Accueil {

    private JFrame frame;
    private boolean startButtonClicked = false;
    private AnimatedCursorFrame animatedCursor;
    //private JProgressBar progressBar;

    /**
     * Affiche l'écran d'accueil.
     */
    public void show() {
        // Création de la fenêtre principale
        frame = new JFrame("TSURO");
        ImageIcon icon = new ImageIcon("src/main/ressources/logo.png");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1065, 600);

        // Utilisation d'un GIF en tant que fond
        JLabel background = new JLabel(new ImageIcon("src/main/ressources/accueil.gif"));

        // Création du bouton de démarrage
        JButton startButton = new JButton();
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        startButton.setFont(buttonFont);

        // Ajout d'un écouteur pour le bouton de démarrage
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButtonClicked = true;
                launchMainMenu();
            }
        });

        // Création d'un panneau pour le bouton de démarrage
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(startButton);

        // Mise en page du fond avec les panneaux de bouton
        background.setLayout(new BorderLayout());
        background.add(buttonPanel, BorderLayout.CENTER);

        // Ajout du fond à la fenêtre principale
        frame.getContentPane().add(background);
        frame.setLocationRelativeTo(null);

        // Initialise et démarre l'animation du curseur
        animatedCursor = new AnimatedCursorFrame("src/main/ressources/defaultCursor.png", "src/main/ressources/hoverCursor.png");

        /*
        AnimatedCursorFrame cursorFrame = new AnimatedCursorFrame(
            "src/main/ressources/defaultCursor.png", // Chemin de l'image par défaut
            "src/main/ressources/hoverCursor.png",  // Chemin de l'image de survol
            5, // Largeur du curseur (ajustez selon votre besoin)
            5  // Hauteur du curseur (ajustez selon votre besoin)
        );
         */
        
        frame.setCursor(animatedCursor.getDefaultCursor());
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setCursor(animatedCursor.getHoverCursor());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setCursor(animatedCursor.getDefaultCursor());
            }
        });

        frame.setVisible(true);
    }

    /**
     * Lance le menu principal.
     */
    private void launchMainMenu() {
        MainMenu.createAndShowGUI(frame); // Utilise la fenêtre existante
        if (animatedCursor != null) {
            animatedCursor.stopAnimation(frame);
        }
    }

    /**
     * Indique si le bouton de démarrage a été cliqué.
     * @return true si le bouton de démarrage a été cliqué, sinon false.
     */
    public boolean isStartButtonClicked() {
        return startButtonClicked;
    }

    public static void main(String[] args) {
        new Accueil().show();
    }
}

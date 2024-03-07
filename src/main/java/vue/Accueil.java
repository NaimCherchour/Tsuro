package main.java.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe Accueil représente l'écran d'accueil du jeu TSURO, lancé avant le Menu. Elle permet d'accéder au menu grâce à son unique bouton.
 */
public class Accueil {

    private JFrame frame;
    private boolean startButtonClicked = false;
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

        // Création de la barre de progression
        // progressBar = new JProgressBar(0, 100);
        // progressBar.setStringPainted(true);
        // progressBar.setFont(new Font("Arial", Font.PLAIN, 16));
        // progressBar.setPreferredSize(new Dimension(1000, 30));

        // Ajout d'un écouteur pour le bouton de démarrage
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButtonClicked = true;

                // Appeler la méthode pour lancer le menu principal
                launchMainMenu();
            }
        });

        // Création d'un panneau pour le bouton de démarrage
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(startButton);

        // Création d'un panneau pour la barre de progression
        // JPanel progressBarPanel = new JPanel();
        // progressBarPanel.setOpaque(false);
        // progressBarPanel.setLayout(new BorderLayout());
        // progressBarPanel.add(progressBar, BorderLayout.SOUTH);

        // Mise en page du fond avec les panneaux de bouton et de barre de progression
        background.setLayout(new BorderLayout());
        background.add(buttonPanel, BorderLayout.CENTER);
        // background.add(progressBarPanel, BorderLayout.SOUTH);

        // Ajout du fond à la fenêtre principale
        frame.getContentPane().add(background);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Animation de la barre de progression
        // Timer timer = new Timer(250, new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         int value = progressBar.getValue() + 1;
        //         if (value > progressBar.getMaximum()) {
        //             value = progressBar.getMinimum();
        //         }
        //         progressBar.setValue(value);
        //     }
        // });
        // timer.start();
    }

    /**
     * Indique si le bouton de démarrage a été cliqué.
     * @return true si le bouton de démarrage a été cliqué, sinon false.
     */
    public boolean isStartButtonClicked() {
        return startButtonClicked;
    }

    /**
     * Lance le menu principal.
     */
    private void launchMainMenu() {
        // Fermez la fenêtre d'accueil
        frame.dispose();

        // Lancer le menu principal
        MainMenu.createAndShowGUI();
    }
}

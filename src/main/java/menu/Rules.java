package main.java.menu;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Classe gérant l'affichage et la navigation dans les règles du jeu.
 */
public class Rules {

    private static int currentIndex = 0;
    // Indice de l'image actuelle
    private static final String[] imagePaths = {
            // Chemins des images de règles
            "src/main/resources/rulesImage1.png",
            "src/main/resources/rulesImage2.png",
            "src/main/resources/rulesImage3.png",
            "src/main/resources/rulesImage4.png",
            "src/main/resources/rulesImage5.png",
            "src/main/resources/rulesImage6.png",
            "src/main/resources/rulesImage7.png",
            "src/main/resources/rulesImage8.png"
    };
    private static JPanel buttonPanel;
    // Panneau global pour les boutons de navigation
    private static JPanel homeButtonPanel;
    // Panneau pour le bouton Home
    /**
     * Affiche les règles en utilisant un JFrame passé en paramètre.
     * @param frame Le cadre dans lequel les règles sont affichées.
     * @param cursorFrame Non utilisé actuellement, prévu pour la gestion du curseur.
     */
    public static void displayRules(JFrame frame, AnimatedCursorFrame cursorFrame, String username) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        // Initialisation du panneau pour les boutons de navigation
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton backButton = createButton("src/main/resources/returnButton.png",
                "src/main/resources/returnButtonHovered.png",
                "src/main/resources/returnButtonPressed.png", true, frame);
        JButton forwardButton = createButton("src/main/resources/forwardButton.png",
                "src/main/resources/forwardButtonHovered.png",
                "src/main/resources/forwardButtonPressed.png", false, frame);
        buttonPanel.add(backButton);
        buttonPanel.add(forwardButton);

        // Initialisation du panneau pour le bouton de retour au menu principal
        if (homeButtonPanel == null) {
            homeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            homeButtonPanel.setOpaque(false);
            ImageIcon homeIcon = new ImageIcon(new ImageIcon("src/main/resources/returnButton.png").getImage().getScaledInstance(110, 55, Image.SCALE_SMOOTH));  // Resize to 40x40
            JButton homeButton = new JButton(homeIcon);
            homeButton.setRolloverIcon(new ImageIcon(new ImageIcon("src/main/resources/returnButtonHovered.png").getImage().getScaledInstance(110, 55, Image.SCALE_SMOOTH)));
            homeButton.setPressedIcon(new ImageIcon(new ImageIcon("src/main/resources/returnButtonPressed.png").getImage().getScaledInstance(110, 55, Image.SCALE_SMOOTH)));
            homeButton.setBorder(BorderFactory.createEmptyBorder());
            homeButton.setContentAreaFilled(false);
            homeButton.setFocusPainted(false);
            homeButton.addActionListener(e -> {
                Option.playSound();  // Jouer le son de clic avant de changer de vue
                MainMenu.createAndShowGUI(frame,username);
                // Retour au menu principal
            });
            homeButtonPanel.add(homeButton);
        }



        // Ajout des panneaux au cadre
        frame.add(homeButtonPanel, BorderLayout.NORTH);

        updateBackground(frame);  // Update the background initially

        frame.setSize(1065, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Met à jour l'arrière-plan du cadre avec l'image actuelle basée sur currentIndex.
     * @param frame Le cadre dont l'arrière-plan doit être mis à jour.
     */
    private static void updateBackground(JFrame frame) {
        ImageIcon icon = new ImageIcon(imagePaths[currentIndex]);
        JLabel background = new JLabel(icon);
        background.setLayout(new BorderLayout());
        background.add(homeButtonPanel, BorderLayout.NORTH); // Ensure home button stays at the top left
        background.add(buttonPanel, BorderLayout.SOUTH); // Add navigation buttons at the bottom

        frame.setContentPane(background);
        frame.revalidate();
        frame.repaint();
    }
    /**
     * Crée un bouton avec des icônes pour les états normal, survolé et pressé.
     * @param iconPath Chemin de l'icône normale.
     * @param hoverIconPath Chemin de l'icône lors du survol.
     * @param pressedIconPath Chemin de l'icône lors de la pression.
     * @param isBackButton Indique si le bouton est pour revenir en arrière dans les images.
     * @param frame Le cadre sur lequel le bouton agira.
     * @return Le bouton configuré.
     */

    private static JButton createButton(String iconPath, String hoverIconPath, String pressedIconPath,
                                        boolean isBackButton, JFrame frame) {
        // Création de l'icône et ajustement de sa taille

        ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(90, 45, Image.SCALE_SMOOTH));
        JButton button = new JButton(icon);
        button.setRolloverIcon(new ImageIcon(new ImageIcon(hoverIconPath).getImage().getScaledInstance(90, 45, Image.SCALE_SMOOTH)));
        button.setPressedIcon(new ImageIcon(new ImageIcon(pressedIconPath).getImage().getScaledInstance(90, 45, Image.SCALE_SMOOTH)));
        // Configuration visuelle du bouton
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            // Ajout d'un action listener pour gérer les clics sur le bouton
            Option.playSound();
            // Mise à jour de l'indice de l'image et rafraîchissement de l'arrière-plan
            if (isBackButton) {
                currentIndex = (currentIndex - 1 + imagePaths.length) % imagePaths.length;
            } else {
                currentIndex = (currentIndex + 1) % imagePaths.length;
            }
            updateBackground(frame);
        });
        return button;
    }

}

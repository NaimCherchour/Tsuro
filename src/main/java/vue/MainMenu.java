package main.java.vue;

import javax.swing.*;
import java.awt.*;

/**
 * Représente le menu principal du jeu TSURO.
 */
public class MainMenu {

    private static String playerName = "";

    /**
     * Méthode principale pour démarrer le menu principal du jeu TSURO.
     * @param args Arguments en ligne de commande d'un main.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::createAndShowGUI);
    }

    /**
     * Crée et affiche l'interface graphique du menu principal (partie vue).
     */
    public static void createAndShowGUI() {
        // Création de la fenêtre principale
        JFrame frame = new JFrame("TSURO : MENU");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Utilisation d'un GIF en tant que fond d'écran
        JLabel background = new JLabel(new ImageIcon("src/main/resources/menu.gif"));
        frame.setContentPane(background);
        frame.setLayout(new BorderLayout());

        // Création d'un panneau pour les boutons avec un layout GridBag
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false); // Rend le panneau transparent

        // Création des boutons avec des images personnalisées
        JButton playButton = new JButton(new ImageIcon("src/main/resources/but2.png"));
        JButton scoresButton = new JButton(new ImageIcon("src/main/resources/button.png"));
        JButton quitButton = new JButton(new ImageIcon("src/main/resources/button.png"));
        JButton rulesButton = new JButton(new ImageIcon("src/main/resources/button.png"));

        // Personnalisation des boutons
        customizeButtons(playButton, scoresButton, quitButton, rulesButton);
        mainStyle(buttonsPanel, playButton, scoresButton, quitButton, rulesButton);

        // Ajout du panneau de boutons à la fenêtre principale
        frame.add(buttonsPanel, BorderLayout.CENTER);

        // Configuration de la taille et de la position de la fenêtre
        frame.setSize(888, 500);
        frame.setLocationRelativeTo(null);

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }

    /**
     * Applique un style spécifique au panneau principal en utilisant GridBagLayout.
     * @param panel Le panneau à styliser.
     * @param buttons Les boutons à ajouter au panneau.
     */
    private static void mainStyle(JPanel panel, JButton... buttons) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Ajuste l'espacement entre les boutons

        // Ajoute les boutons au panneau avec le style défini
        for (JButton button : buttons) {
            panel.add(button, gbc);
        }
    }

    /**
     * Personnalise l'apparence des boutons en utilisant des images.
     * @param buttons Les boutons à personnaliser.
     */
    private static void customizeButtons(JButton... buttons) {
        // Itère sur chaque bouton pour personnaliser son apparence
        for (JButton button : buttons) {
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);

            // Redimensionne l'icône du bouton
            ImageIcon icon = (ImageIcon) button.getIcon();
            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(150, 100, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(newimg));
            button.setPressedIcon(new ImageIcon(newimg)); // Assure que l'icône reste identique lorsqu'elle est pressée
        }
    }

    /**
     * Permet de saisir le pseudo du joueur.
     * @param layeredPane Le pannel pour afficher la boîte de dialogue.
     */
    private static void showPseudoInput(JLayeredPane layeredPane) {
        String pseudo = JOptionPane.showInputDialog(layeredPane, "Quel est votre pseudo ?", "Entrer votre pseudo", JOptionPane.QUESTION_MESSAGE);
        if (pseudo != null && !pseudo.trim().isEmpty()) {
            playerName = pseudo.trim().toLowerCase();
        } else {
            JOptionPane.showMessageDialog(layeredPane, "Veuillez choisir un pseudo valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Affiche le classement des joueurs.
     * @param frame La fenêtre principale pour afficher le tableau des scores.
     */
    private static void showLeaderboard(JFrame frame) {
        // Implémentation future pour afficher le tableau des scores
        JOptionPane.showMessageDialog(frame, "Tableau des scores");
    }

    /**
     * Affiche la sélection de skin.
     * @param frame La fenêtre principale pour afficher la sélection du profil.
     */
    private static void showSkinSelection(JFrame frame) {
        // Implémentation future pour permettre la sélection du profil
        JOptionPane.showMessageDialog(frame, "Quel profil veux-tu choisir?");
    }
}

package main.java.vue;

import javax.swing.*;
import java.awt.*;

public class MainMenu {

    private static String playerName = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("TSURO : MENU");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Utilisation d'un GIF en fond
        // Assurez-vous que le chemin d'accès et le nom du fichier GIF sont corrects
        JLabel background = new JLabel(new ImageIcon("src/main/ressources/menu.gif"));
        frame.setContentPane(background);
        frame.setLayout(new BorderLayout());

        // Préparation du panneau pour les boutons avec transparence
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false); // Rend le panneau transparent

        JButton playButton = new JButton(new ImageIcon("src/main/ressources/but2.png"));
        JButton scoresButton = new JButton(new ImageIcon("src/main/ressources/button.png"));
        JButton quitButton = new JButton(new ImageIcon("src/main/ressources/button.png"));
        JButton rulesButton = new JButton(new ImageIcon("src/main/ressources/button.png"));

        customizeButtons(playButton, scoresButton, quitButton, rulesButton);
        mainStyle(buttonsPanel, playButton, scoresButton, quitButton, rulesButton);

        frame.add(buttonsPanel, BorderLayout.CENTER);
        frame.setSize(888, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void mainStyle(JPanel panel, JButton... buttons) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Ajuste l'espacement entre les boutons

        for (JButton button : buttons) {
            panel.add(button, gbc);
        }
    }

    private static void customizeButtons(JButton... buttons) {
        for (JButton button : buttons) {
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            ImageIcon icon = (ImageIcon) button.getIcon();
            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(150, 100, java.awt.Image.SCALE_SMOOTH); // Ajuster la taille si nécessaire
            button.setIcon(new ImageIcon(newimg));
            button.setPressedIcon(new ImageIcon(newimg)); // Assure que l'icône reste identique lorsqu'elle est pressée
        }
    }

    // Les méthodes suivantes pourraient être utilisées pour étendre la fonctionnalité de l'application :
    private static void showPseudoInput(JLayeredPane layeredPane) {
        String pseudo = JOptionPane.showInputDialog(layeredPane, "Quel est votre pseudo ?", "Entrer votre pseudo", JOptionPane.QUESTION_MESSAGE);
        if (pseudo != null && !pseudo.trim().isEmpty()) {
            playerName = pseudo.trim().toLowerCase();
        } else {
            JOptionPane.showMessageDialog(layeredPane, "Veuillez choisir un pseudo valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showLeaderboard(JFrame frame) {
        // Implémentation future pour afficher le tableau des scores
        JOptionPane.showMessageDialog(frame, "Tableau des scores");
    }

    private static void showSkinSelection(JFrame frame) {
        // Implémentation future pour permettre la sélection du skin
        JOptionPane.showMessageDialog(frame, "Quel profil veux-tu choisir?");
    }
}

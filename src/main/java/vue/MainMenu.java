package main.java.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    private static String playerName = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("TSURO : MENU\n" + "");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ajouter une image en fond
        JLabel background = new JLabel(new ImageIcon("src/main/ressources/fondv1.jpg"));
        background.setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        background.add(layeredPane, BorderLayout.CENTER);

        JButton playButton = new JButton("JOUER");
        JButton scoresButton = new JButton("Scores");
        JButton profilePicButton = new JButton("Profile");
        JButton quitButton = new JButton("Quitter");
        JButton rulesButton = new JButton("Règles");

        mainStyle(layeredPane, playButton, scoresButton, profilePicButton, quitButton, rulesButton);

        frame.getContentPane().add(background);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void mainStyle(JLayeredPane layeredPane, JButton play, JButton scores, JButton profilePic, JButton quit, JButton rulesButton) {
        play.setFont(new Font("Arial", Font.PLAIN, 20));
        scores.setFont(new Font("Arial", Font.PLAIN, 20));
        quit.setFont(new Font("Arial", Font.PLAIN, 20));
        profilePic.setFont(new Font("Arial", Font.PLAIN, 20));
        rulesButton.setFont(new Font("Arial", Font.PLAIN, 20));

        profilePic.setContentAreaFilled(false);
        profilePic.setOpaque(true);

        play.setFocusPainted(false);
        scores.setFocusPainted(false);
        profilePic.setFocusPainted(false);
        quit.setFocusPainted(false);
        rulesButton.setFocusPainted(false);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerName.isEmpty()) {
                    showPseudoInput(layeredPane);
                } else {
                }
            }
        });

        scores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLeaderboard((JFrame) SwingUtilities.getWindowAncestor(layeredPane));
            }
        });

        profilePic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSkinSelection((JFrame) SwingUtilities.getWindowAncestor(layeredPane));
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir quitter ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        

        // Utiliser GridBagLayout
        layeredPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Positionnement des boutons au milieu
        addComponent(layeredPane, play, gbc, 0, 0, 1, 1, GridBagConstraints.CENTER);
        addComponent(layeredPane, scores, gbc, 0, 1, 1, 1, GridBagConstraints.CENTER);
        addComponent(layeredPane, profilePic, gbc, 0, 2, 1, 1, GridBagConstraints.CENTER);
        addComponent(layeredPane, quit, gbc, 0, 3, 1, 1, GridBagConstraints.CENTER);
        addComponent(layeredPane, rulesButton, gbc, 0, 4, 1, 1, GridBagConstraints.CENTER);
    }

    private static void addComponent(Container container, Component component, GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        container.add(component, gbc);
    }

    private static void showPseudoInput(JLayeredPane layeredPane) {
        String pseudo = JOptionPane.showInputDialog(layeredPane, "Quel est votre pseudo ?", "Entrer votre pseudo", JOptionPane.QUESTION_MESSAGE);
        if (pseudo != null && !pseudo.trim().isEmpty()) {
            playerName = pseudo.trim().toLowerCase();
        } else {
            JOptionPane.showMessageDialog(layeredPane, "Veuillez choisir un pseudo valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    private static void showLeaderboard(JFrame frame) {
        // A developper
        JOptionPane.showMessageDialog(frame, "Tableau des scores");
    }

    private static void showSkinSelection(JFrame frame) {
        // A developper
        JOptionPane.showMessageDialog(frame, "Quel profil veux-tu choisir?");
    }
    
}

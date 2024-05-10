package main.java.menu;

import main.java.Main;
import main.java.model.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Représente la page de profil utilisateur.
 */
public class ProfilePage {

    /**
     * Constructeur de la classe ProfilePage.
     *
     * @param existingFrame Le frame existant sur lequel afficher la page de profil.
     * @param userProfile   Le profil utilisateur à afficher.
     */
    public ProfilePage(JFrame existingFrame, Profile userProfile) {
        existingFrame.getContentPane().removeAll();

        // Chargement de l'arrière-plan depuis une image
        ImageIcon backgroundIcon = new ImageIcon("src/main/resources/fond.png");
        JLabel background = new JLabel(backgroundIcon);
        background.setLayout(new BorderLayout());
        existingFrame.setContentPane(background);

        // Bouton de retour
        JButton returnButton = new JButton();
        ImageIcon returnIcon = new ImageIcon("src/main/resources/returnButton.png");
        Image img = returnIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        returnIcon = new ImageIcon(img);
        returnButton.setIcon(returnIcon);
        returnButton.setBorderPainted(false);
        returnButton.setFocusPainted(false);
        returnButton.setContentAreaFilled(false);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.playSound("src/main/resources/sound/buttonClickSound.wav");
                MainMenu.createAndShowGUI(existingFrame,userProfile.getUsername());
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(returnButton);
        existingFrame.add(topPanel, BorderLayout.NORTH);

        // Affichage du nom d'utilisateur
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setOpaque(false);
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel usernameLabel = new JLabel("Nom d'utilisateur: " + userProfile.getUsername());
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(usernameLabel, gbc);

        // Affichage des boutons pour les jeux sauvegardés
        JPanel buttonPanel = new JPanel(new GridLayout(userProfile.getSavedGames().size(), 1));
        for (String savedGame : userProfile.getSavedGames()) {
            JButton button = new JButton(savedGame);
            ImageIcon apercuIcon = new ImageIcon("src/main/resources/apercu_jeu.png");
            Image scaledImage = apercuIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            button.setIcon(scaledIcon);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainMenu.playSound("src/main/resources/sound/buttonClickSound.wav");
                    Main.initializeAndRunGame(savedGame, userProfile.getUsername());
                    System.out.println("Jeu sauvegardé sélectionné : " + savedGame);
                }
            });
            buttonPanel.add(button);
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(buttonPanel, gbc);
        background.add(mainPanel,BorderLayout.CENTER);

        existingFrame.pack();
        existingFrame.setLocationRelativeTo(null);
        existingFrame.setVisible(true);

        existingFrame.revalidate();
        existingFrame.repaint();
    }
}

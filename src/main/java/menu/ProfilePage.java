package main.java.menu;

import main.java.Main;
import main.java.model.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ProfilePage {

    public ProfilePage(JFrame existingFrame, Profile userProfile) {
        existingFrame.getContentPane().removeAll();

        ImageIcon backgroundIcon = new ImageIcon("src/main/resources/fond.png");
        JLabel background = new JLabel(backgroundIcon);
        background.setLayout(new BorderLayout());
        existingFrame.setContentPane(background);


        JButton returnButton = new JButton();
        // Charger l'image depuis le fichier returnButton.png
        ImageIcon returnIcon = new ImageIcon("src/main/resources/returnButton.png");
        // Redimensionner l'icône pour qu'elle soit plus petite
        Image img = returnIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        returnIcon = new ImageIcon(img);
        returnButton.setIcon(returnIcon);
        returnButton.setBorderPainted(false); // Pour enlever la bordure
        returnButton.setFocusPainted(false); // Pour enlever la couleur de focus lors du clic
        returnButton.setContentAreaFilled(false); // Pour enlever le remplissage
        // Ajouter un ActionListener pour détecter les clics sur le bouton
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.playSound("src/main/resources/sound/buttonClickSound.wav");
                MainMenu.createAndShowGUI(existingFrame,userProfile.getUsername());
            }
        });
        // Ajout du bouton de retour à un panneau en haut de la fenêtre.
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(returnButton);
        existingFrame.add(topPanel, BorderLayout.NORTH);


        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setOpaque(false);
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel usernameLabel = new JLabel("Username: " + userProfile.getUsername());
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(usernameLabel, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(userProfile.getSavedGames().size(), 1));
        for (String savedGame : userProfile.getSavedGames()) {
            JButton button = new JButton(savedGame);
            // Load the image
            ImageIcon apercuIcon = new ImageIcon("src/main/resources/apercu_jeu.png");

            // Scale the image to fit within the button
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
                    System.out.println("Selected saved game: " + savedGame);
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

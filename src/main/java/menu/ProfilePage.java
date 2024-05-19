package main.java.menu;

import main.java.Main;
import main.java.model.Profile;
import main.java.model.ProfileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

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
        // Reprend le frame d'accueil
        existingFrame.getContentPane().removeAll(); // Clean le contenu avant de faire d'autres réglages
        existingFrame.setSize(1065, 600);
        existingFrame.setLocationRelativeTo(null);

        // Chargement de l'arrière-plan depuis une image
        ImageIcon backgroundIcon = new ImageIcon("src/main/resources/fond.png");
        JLabel background = new JLabel(backgroundIcon);
        background.setLayout(new BorderLayout());
        existingFrame.setContentPane(background);

        JButton returnButton = Option.createReturnButton();
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Option.playSound();
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
        // Customize the appearance of the JLabel
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set the font to Arial, bold, size 16
        usernameLabel.setForeground(new Color(56, 166, 239)); // Set the foreground color to blue
        usernameLabel.setBackground(new Color(22, 80, 6)); // Set the background color to yellow
        usernameLabel.setOpaque(true); // Make the background color visible by setting opaque to true
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(usernameLabel, gbc);

// Affichage des boutons pour les jeux sauvegardés
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
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
                    Option.playSound();
                    Main.initializeAndRunGame(existingFrame, savedGame, userProfile.getUsername());
                    System.out.println("Jeu sauvegardé sélectionné : " + savedGame);
                }
            });
            buttonPanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setPreferredSize(new Dimension(300, 150)); // You can adjust this size as needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(scrollPane, gbc);
        background.add(mainPanel, BorderLayout.CENTER);

        existingFrame.pack();
        existingFrame.setLocationRelativeTo(null);
        existingFrame.setVisible(true);

        existingFrame.revalidate();
        existingFrame.repaint();
    }

    public ProfilePage(JFrame existingFrame, String username) {
        this(existingFrame, Objects.requireNonNull(ProfileManager.getProfile(username)));
    }

    }

package main.java.menu;

import main.java.Main;

import javax.swing.*;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Option {
    private static Clip clip; // Déclarer la variable Clip pour le son
    private static boolean soundMuted = false; // Indicateur de son coupé

    public static void gererClicSurBoutonOption(JFrame frame, Clip clips,String username) {
        frame.getContentPane().removeAll();

        ImageIcon backgroundIcon = new ImageIcon("src/main/resources/fond.png");
        JLabel background = new JLabel(backgroundIcon);
        background.setLayout(new BorderLayout());
        frame.setContentPane(background);

        clip = clips;

        // Créer un JButton pour afficher le bouton returnButton.png
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
                MainMenu.createAndShowGUI(frame,username);  // Assurer que le menu principal gère également correctement le curseur.
            }
        });
        // Ajout du bouton de retour à un panneau en haut de la fenêtre.
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(returnButton);
        frame.add(topPanel, BorderLayout.NORTH);


        // Créer un bouton pour augmenter le volume
        JButton increaseVolumeButton = new JButton("Augmenter le volume");
        increaseVolumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adjustVolume(0.1f); // Augmenter le volume de 10%
            }
        });

        // Créer un bouton pour diminuer le volume
        JButton decreaseVolumeButton = new JButton("Diminuer le volume");
        decreaseVolumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adjustVolume(-0.1f); // Diminuer le volume de 10%
            }
        });

        // Créer un bouton pour couper ou réactiver le son
        JButton muteButton = new JButton("Couper le son");
        muteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (clip != null) {
                    if (soundMuted) {
                        clip.start(); // Réactiver le son
                        muteButton.setText("Couper le son");
                        muteButton.setForeground(Color.black); // Enlever le style rouge
                        soundMuted = false;
                    } else {
                        clip.stop(); // Couper le son
                        muteButton.setText("Réactiver le son");
                        muteButton.setForeground(Color.red); // Appliquer le style rouge
                        soundMuted = true;
                    }
                }
            }
        });

        // Ajouter les boutons au panneau avec un gestionnaire de disposition approprié
        JPanel buttonPanel = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout
        buttonPanel.setOpaque(false); // Rendre le panneau transparent pour voir le fond d'écran
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants
        buttonPanel.add(increaseVolumeButton, gbc); // Ajout du bouton augmenter le volume
        gbc.gridy = 1;
        buttonPanel.add(decreaseVolumeButton, gbc); // Ajout du bouton diminuer le volume
        gbc.gridy = 2;
        buttonPanel.add(muteButton, gbc); // Ajout du bouton couper ou réactiver le son
        // Ajouter les boutons au panneau avec un gestionnaire de disposition approprié
        background.add(buttonPanel, BorderLayout.CENTER); // Ajout du panneau au fond d'écran
        

        frame.revalidate();
        frame.repaint();
    }

    private static void adjustVolume(float value) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float currentVolume = gainControl.getValue();
            float newVolume = currentVolume + value;
            gainControl.setValue(newVolume);
        }
    }
}

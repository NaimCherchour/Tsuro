package main.java.vue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * Représente le menu principal du jeu TSURO.
 */
public class MainMenu {

    private static String playerName = "";

    /**
     * Crée et affiche l'interface graphique du menu principal (partie vue).
     */
    public static void createAndShowGUI() {
        // Création de la fenêtre principale
        JFrame frame = new JFrame("TSURO : MENU");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Chargement et définition de l'icône de la fenêtre à partir de 'logo.png'
        ImageIcon icone = new ImageIcon("src/main/ressources/logo.png");
        frame.setIconImage(icone.getImage());

        // Utilisation d'un GIF en tant que fond d'écran
        JLabel background = new JLabel(new ImageIcon("src/main/ressources/fond.png"));
        frame.setContentPane(background);
        frame.setLayout(new BorderLayout());

        try {
            // Création de l'audioInputStream à partir du fichier audio
            File soundFile = new File("src/main/ressources/SoundMenu.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

            // Lecture du son en boucle
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // Affichage d'un message pour indiquer que tout s'est bien passé
            System.out.println("Tout s'est bien passé !");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Une erreur est survenue lors de la lecture du fichier audio : " + e.getMessage());
            e.printStackTrace();
        }



        // Création d'un panneau pour les boutons avec un layout GridBag
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(68, 0, 0, 0));
        buttonsPanel.setOpaque(false);

        // Création des boutons avec des images personnalisées
        JButton playButton = new JButton(new ImageIcon("src/main/ressources/playButton.png"));
        playButton.setActionCommand("play");
        JButton optionsButton = new JButton(new ImageIcon("src/main/ressources/optionsButton.png"));
        optionsButton.setActionCommand("options");
        JButton quitButton = new JButton(new ImageIcon("src/main/ressources/quitButton.png"));
        quitButton.setActionCommand("quit");
        JButton rulesButton = new JButton(new ImageIcon("src/main/ressources/rulesButton.png"));
        rulesButton.setActionCommand("rules");

        // Ajoute un ActionListener au bouton "Quitter"

        quitButton.addActionListener(e -> {
            customDialog dialog = new customDialog(frame);
            dialog.setVisible(true);
        });

        // Personnalisation des boutons
        Button.customizeButtons(playButton, optionsButton, quitButton);
        // Applique le style principal aux boutons, à l'exception du bouton 'rules'
        Button.mainStyle(buttonsPanel, playButton, optionsButton, quitButton);
        rulesButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        rulesButton.setFocusPainted(false);
        rulesButton.setContentAreaFilled(false);
        // Redimensionne l'icône du bouton
        ImageIcon icon = (ImageIcon) rulesButton.getIcon();
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(90, 45, java.awt.Image.SCALE_SMOOTH);
        rulesButton.setIcon(new ImageIcon(newimg));
        ImageIcon pressedIconRules = new ImageIcon("src/main/ressources/rulesButtonPressed.png");
        Image pressedImgRules = pressedIconRules.getImage();
        Image newPressedImgRules = pressedImgRules.getScaledInstance(90, 45, java.awt.Image.SCALE_SMOOTH); // Taille
                                                                                                           // personnalisée
                                                                                                           // pour
                                                                                                           // l'état
                                                                                                           // pressé
        rulesButton.setPressedIcon(new ImageIcon(newPressedImgRules)); // Définit l'icône pour l'état pressé // Assigner
                                                                       // l'icône de hover au bouton 'rules'
        ImageIcon hoverIconRules = new ImageIcon("src/main/ressources/rulesButtonHover.png");
        Image hoverImgRules = hoverIconRules.getImage();
        Image newHoverImgRules = hoverImgRules.getScaledInstance(90, 45, java.awt.Image.SCALE_SMOOTH); // Taille
                                                                                                       // personnalisée
                                                                                                       // pour le hover
        rulesButton.setRolloverIcon(new ImageIcon(newHoverImgRules));

        // Création d'un panneau spécifique pour le bouton 'rules'
        JPanel rulesPanel = new JPanel(new BorderLayout());
        rulesPanel.setOpaque(false);
        rulesPanel.add(rulesButton, BorderLayout.EAST);

        // Ajout du panneau de boutons et du panneau 'rules' à la fenêtre principale

        frame.add(buttonsPanel, BorderLayout.CENTER);
        frame.add(rulesPanel, BorderLayout.SOUTH);

        // Configuration de la taille et de la position de la fenêtre
        frame.setSize(1110, 625);
        frame.setLocationRelativeTo(null);

        playButton.addActionListener(e -> {
            Jouer.gererClicSurBoutonJouer(frame);
        });

        
        frame.setVisible(true);

    }
}

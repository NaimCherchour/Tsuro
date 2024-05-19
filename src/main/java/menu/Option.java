package main.java.menu;

import main.java.Main;

import javax.swing.*;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Option {
    private static Clip clip; // Déclarer la variable Clip pour le son
    protected static boolean soundMuted = false; // Indicateur de son coupé
    protected static final String SOUND_PATH = "src/main/resources/sound/buttonClickSound.wav" ;
    private static JButton muteButton ;
    private static  String text = "Couper le son";

    public static void gererClicSurBoutonOption(JFrame frame, Clip clips,String username) {
        frame.getContentPane().removeAll();

        ImageIcon backgroundIcon = new ImageIcon("src/main/resources/fond.png");
        JLabel background = new JLabel(backgroundIcon);
        background.setLayout(new BorderLayout());
        frame.setContentPane(background);

        clip = clips;

        JButton returnButton = createReturnButton();
        // Ajouter un ActionListener pour détecter les clics sur le bouton
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Option.playSound();
                MainMenu.createAndShowGUI(frame,username);  // Assurer que le menu principal gère également correctement le curseur.
            }
        });
        // Ajout du bouton de retour à un panneau en haut de la fenêtre.
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(returnButton);
        frame.add(topPanel, BorderLayout.NORTH);


        // Créer un bouton pour couper ou réactiver le son
        muteButton = new JButton(text);
        muteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (clip != null) {
                    if (!soundMuted) { // Si le son n'est pas déjà coupé
                        clip.stop(); // Couper le son
                        text = "Réactiver le son";
                        muteButton.setText("Réactiver le son");
                        muteButton.setForeground(Color.red);
                    } else {
                        clip.start(); // Réactiver le son
                        playSound();
                        text = "Couper le son";
                        muteButton.setText("Couper le son");
                        muteButton.setForeground(Color.black);
                    }
                    soundMuted = !soundMuted; // Inverser l'état de la sourdine
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
        buttonPanel.add(muteButton, gbc); // Ajout du bouton couper ou réactiver le son
        // Ajouter les boutons au panneau avec un gestionnaire de disposition approprié
        background.add(buttonPanel, BorderLayout.CENTER); // Ajout du panneau au fond d'écran
        

        frame.revalidate();
        frame.repaint();
    }

    public static JButton createReturnButton(){
        JButton returnButton = new JButton();
        //returnButton.setBackground(new Color(0,0,0,0));
        // Charger l'image depuis le fichier returnButton.png
        ImageIcon returnIcon = new ImageIcon("src/main/resources/returnButton.png");
        // Redimensionner l'icône pour qu'elle soit plus petite
        Image img = returnIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        returnIcon = new ImageIcon(img);
        returnButton.setIcon(returnIcon);
        returnButton.setBorderPainted(false); // Pour enlever la bordure
        returnButton.setFocusPainted(false); // Pour enlever la couleur de focus lors du clic
        returnButton.setContentAreaFilled(false); // Pour enlever le remplissage
        return returnButton;
    }

    /**
     * Joue un son à partir du fichier spécifié.
     */
    public static void playSound() {
        try {
            if ( !soundMuted) {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SOUND_PATH).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start(); }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

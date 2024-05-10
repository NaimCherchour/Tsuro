package main.java.menu;

import main.java.model.Profile;
import main.java.model.ProfileManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



/**
 * Représente le menu principal du jeu TSURO.
 */
public class MainMenu {
    private static JFrame frame; // La déclare comme variable de classe (static)
    private static AnimatedCursorFrame cursorFrame;
    private static Clip clip;

    /**
     * Crée et affiche l'interface graphique du menu principal (partie vue).
     */

    public static void createAndShowGUI(JFrame existingFrame,String username) {
        // Reprend le frame d'accueil
        frame = existingFrame;
        frame.getContentPane().removeAll(); // Clean le contenu avant de faire d'autres réglages

        // Assure l'initialisation unique de cursorFrame
        if (cursorFrame == null) {
            cursorFrame = new AnimatedCursorFrame(
                    "src/main/resources/defaultCursor.png",
                    "src/main/resources/hoverCursor.png"
            );
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1065, 600);
        frame.setLocationRelativeTo(null);

        // Définissez le curseur après toutes les modifications de la fenêtre pour garantir qu'il reste appliqué
        frame.setCursor(cursorFrame.getDefaultCursor());

        // Chargement et définition de l'icône de la fenêtre à partir de 'logo.png'
        ImageIcon icone = new ImageIcon("src/main/resources/logo.png");
        frame.setIconImage(icone.getImage());

        // Utilisation d'un GIF en tant que fond d'écran
        JLabel background = new JLabel(new ImageIcon("src/main/resources/fond.png"));
        frame.setContentPane(background);
        frame.setLayout(new BorderLayout());


        /*
        AnimatedCursorFrame cursorFrame = new AnimatedCursorFrame(
            "src/main/resources/defaultCursor.png", // Chemin de l'image par défaut
            "src/main/resources/hoverCursor.png",  // Chemin de l'image de survol
            5, // Largeur du curseur (ajustez selon votre besoin)
            5  // Hauteur du curseur (ajustez selon votre besoin)
        );
         */

        try {
            // Création de l'audioInputStream à partir du fichier audio
            File soundFile = new File("src/main/resources/sound/SoundMenu.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

            // Lecture du son en boucle
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Une erreur est survenue lors de la lecture du fichier audio : " + e.getMessage());
            e.printStackTrace();
        }
        // Création d'un panneau pour les boutons avec un layout GridBag
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(68, 0, 0, 0));
        buttonsPanel.setOpaque(false);

        // Création des boutons avec des images personnalisées
        JButton playButton = new JButton(new ImageIcon("src/main/resources/playButton.png"));
        playButton.setActionCommand("play");
        JButton optionsButton = new JButton(new ImageIcon("src/main/resources/optionsButton.png"));
        optionsButton.setActionCommand("options");
        JButton profilButton = new JButton(new ImageIcon("src/main/resources/profilButton.png"));
        profilButton.setActionCommand("profil");
        JButton quitButton = new JButton(new ImageIcon("src/main/resources/quitButton.png"));
        quitButton.setActionCommand("quit");
        JButton rulesButton = new JButton(new ImageIcon("src/main/resources/rulesButton.png"));
        rulesButton.setActionCommand("rules");

        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Appel de la méthode gererClicSurBoutonOption de la classe Option
                Option.gererClicSurBoutonOption(frame, clip,username);
            }
        });
        

        // Affectation du curseur par défaut à la frame
        frame.setCursor(cursorFrame.getDefaultCursor());

        // Création de l'adaptateur de la souris pour le changement de curseur
        MouseAdapter buttonHoverAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                MainMenu.frame.setCursor(cursorFrame.getHoverCursor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                MainMenu.frame.setCursor(cursorFrame.getDefaultCursor());
            }
        };


        // Ajout de l'adaptateur de la souris aux boutons
        playButton.addMouseListener(buttonHoverAdapter);
        optionsButton.addMouseListener(buttonHoverAdapter);
        profilButton.addMouseListener(buttonHoverAdapter);
        quitButton.addMouseListener(buttonHoverAdapter);
        rulesButton.addMouseListener(buttonHoverAdapter);


        rulesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Rules.displayRules(frame, cursorFrame,username); // Assurez-vous que cursorFrame est bien passé ici
            }
        });



        // Ajoute un ActionListener au bouton "Quitter"

        quitButton.addActionListener(e -> {
            customDialog dialog = new customDialog(frame);
        });

        

        // Personnalisation des boutons
        main.java.menu.Button.customizeButtons(playButton, optionsButton, profilButton, quitButton);
        // Applique le style principal aux boutons, à l'exception du bouton 'rules'
        Button.mainStyle(buttonsPanel, playButton, optionsButton,profilButton, quitButton);
        rulesButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        rulesButton.setFocusPainted(false);
        rulesButton.setContentAreaFilled(false);
        // Redimensionne l'icône du bouton
        ImageIcon icon = (ImageIcon) rulesButton.getIcon();
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(90, 45, java.awt.Image.SCALE_SMOOTH);
        rulesButton.setIcon(new ImageIcon(newimg));
        ImageIcon pressedIconRules = new ImageIcon("src/main/resources/rulesButtonPressed.png");
        Image pressedImgRules = pressedIconRules.getImage();
        Image newPressedImgRules = pressedImgRules.getScaledInstance(90, 45, java.awt.Image.SCALE_SMOOTH);
        // Taille personnalisée pour l'état pressé
        rulesButton.setPressedIcon(new ImageIcon(newPressedImgRules)); // Définit l'icône pour l'état pressé // Assigner
        // l'icône de hover au bouton 'rules'
        ImageIcon hoverIconRules = new ImageIcon("src/main/resources/rulesButtonHover.png");
        Image hoverImgRules = hoverIconRules.getImage();
        Image newHoverImgRules = hoverImgRules.getScaledInstance(90, 45, java.awt.Image.SCALE_SMOOTH);
        // Taille personnalisée pour le hover
        rulesButton.setRolloverIcon(new ImageIcon(newHoverImgRules));

        // Création d'un panneau spécifique pour le bouton 'rules'
        JPanel rulesPanel = new JPanel(new BorderLayout());
        rulesPanel.setOpaque(false);
        rulesPanel.add(rulesButton, BorderLayout.EAST);

        configureButton(playButton, () -> {
            Jouer.gererClicSurBoutonJouer(frame, cursorFrame,username);
        });
        configureButton(optionsButton, () -> {
            // Code à exécuter lors du clic sur optionsButton
        });

        configureButton(profilButton, () -> {
            ProfileManager profileManager = new ProfileManager();
            Profile userProfile = profileManager.getProfile(username);
            ProfilePage pdp = new ProfilePage(existingFrame, userProfile);
        });

        configureButton(quitButton, () -> {
            // Code à exécuter lors du clic sur quitButton
            customDialog dialog = new customDialog(frame);
            dialog.setVisible(true);
        });
        configureButton(rulesButton, () -> {

        });

        // Ajout du panneau de boutons et du panneau 'rules' à la fenêtre principale

        frame.add(buttonsPanel, BorderLayout.CENTER);
        frame.add(rulesPanel, BorderLayout.SOUTH);

        // Configuration de la taille et de la position de la fenêtre
        frame.setSize(1110, 625);
        frame.setLocationRelativeTo(null);

        playButton.addActionListener(e -> {
            Jouer.gererClicSurBoutonJouer(frame,cursorFrame,username);
        });

        optionsButton.addActionListener(e -> {
            Option.gererClicSurBoutonOption(existingFrame, clip,username);
        });


        // Définir le curseur par défaut juste avant de rendre la fenêtre visible
        frame.setCursor(cursorFrame.getDefaultCursor());
        frame.getContentPane().setCursor(cursorFrame.getDefaultCursor());
        frame.setVisible(true);

    }
    /**
     * Joue un son à partir du fichier spécifié.
     *
     * @param soundFileName Le chemin vers le fichier son.
     */
    public static void playSound(String soundFileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFileName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure le bouton avec un adaptateur de souris pour changer le curseur
     * et un ActionListener pour jouer un son et exécuter une action.
     *
     * @param button Le bouton à configurer.
     * @param action L'action à exécuter après le clic sur le bouton.
     */
    private static void configureButton(JButton button, Runnable action) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(cursorFrame.getHoverCursor()); // Utilisation du curseur de survol défini dans AnimatedCursorFrame
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(cursorFrame.getDefaultCursor()); // Réinitialisation au curseur par défaut défini dans AnimatedCursorFrame
            }
        });

        button.addActionListener(e -> {
            playSound("src/main/resources/sound/buttonClickSound.wav");
            action.run();
        });
    }

}

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;



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
        AnimatedCursorFrame cursorFrame = new AnimatedCursorFrame(
                "src/main/ressources/defaultCursor.png", // Remplacer avec le chemin réel de l'image par défaut
                "src/main/ressources/hoverCursor.png"    // Remplacer avec le chemin réel de l'image de survol
        );

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
        JButton profilButton = new JButton(new ImageIcon("src/main/ressources/profilButton.png"));
        profilButton.setActionCommand("profil");
        JButton quitButton = new JButton(new ImageIcon("src/main/ressources/quitButton.png"));
        quitButton.setActionCommand("quit");
        JButton rulesButton = new JButton(new ImageIcon("src/main/ressources/rulesButton.png"));
        rulesButton.setActionCommand("rules");
        // Récupération des curseurs de AnimatedCursorFrame
        Cursor defaultCursor = cursorFrame.getDefaultCursor();
        Cursor hoverCursor = cursorFrame.getHoverCursor();

        // Affectation du curseur par défaut à la frame
        frame.setCursor(defaultCursor);

        // Création de l'adaptateur de la souris pour le changement de curseur
        MouseAdapter buttonHoverAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                frame.setCursor(hoverCursor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.setCursor(defaultCursor);
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
        // fenetre pour afficher les regles
        JDialog dialog = new JDialog(frame, "Rules", true);

      
        Color color = new Color(64, 224, 208);
        dialog.getContentPane().setBackground(color);

        // Contenu de la fenêtre JDialog avec les règles
        JTextArea rulesTextArea = new JTextArea(
            "Objectif:\n" +
            "Placez les carreaux et voyagez à travers le tableau pour survivre à vos adversaires.\n\n" +
            "Composants:\n" +
            "- 6 x 6 tiles de jeu.\n" +
            "- 35 Tiravaillons avec des chemins différents.\n" +
            "- 2 à 8 jetons de couleur différentes, un pour chaque joueur.\n" +
            "- Une tuile de dragon spéciale.\n\n" +
            "Mise en place:\n" +
            "- Tout le monde choisit un jeton et le met sur un point de départ au bord de la planche.\n" +
            "- Créez un tas de sillon à partir des tuiles de 35 chemins.\n" +
            "- Chaque joueur tire trois Tiles de chemin aléatoires pour commencer, et le plus jeune joueur va en premier.\n" +
            "- Le jeu continue dans le sens des aiguilles d'une montre autour de la planche.\n\n" +
            "Comment jouer:\n" +
            "Que faire à tour de votre tour :\n" +
            "- Jouez un trait de chemin : Choisissez l'un de vos paquettes et mettez-le sur le plateau. Connectez-le à l'endroit où se trouve votre jeton maintenant.\n" +
            "- Déplacez le (les) jeton(s) : Après avoir placé votre tuile de chemin, suivez le chemin que vous avez tracé, et déplacez votre jeton le long de ce chemin. Déplacez tout autre jeton de joueur qui se connecte également au Tile de chemin que vous avez placé.\n" +
            "- Surveillez les collisions : Si votre jeton entre en collision avec le jeton d'un autre joueur, vous êtes tous les deux sortis du jeu. Tout jeton qui sort du plateau n'est pas non plus en jeu.\n" +
            "- Obtenez un nouveau chemin : Quand vous aurez fini, prenez une autre tuile de chemin pour en garder trois dans votre main. Si vous n'êtes pas en jeu, alors mélangez vos tuiles à nouveau dans la pile de tirage.\n" +
            "Comment gagner:\n" +
            "Continuez à jouer jusqu'à ce que le jeton d'un seul joueur soit laissé sur le plateau. Ce joueur gagne.\n\n" +
            "Règles supplémentaires:\n" +
            "- Les chemins qui se croisent ne comptent pas comme une collision. Ce n'est que lorsque les jetons finissent sur la même voie qu'ils entrent en collision.\n" +
            "- Vous devez placer votre Tile Path adjacent à votre jeton.\n" +
            "- Si tout le monde sort en même temps, le jeu est une cravate.\n\n" +
            "Variations:\n" +
            "- Classique : Standard Tsuro.\n" +
            "- Le plus long chemin : À la fin de la partie, le joueur avec le chemin le plus long gagne\n" +
            "- Solo : Placez tous les Tiles du chemin tout en empêchant chaque jeton d'entrer en collision ou de sortir de la planche.\n"
        );
        rulesTextArea.setEditable(false);
        rulesTextArea.setOpaque(false);
        rulesTextArea.setWrapStyleWord(true);

        rulesTextArea.setLineWrap(true);
        rulesTextArea.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(rulesTextArea);
        scrollPane.getViewport().setBackground(new Color(173, 216, 230));
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Taille et position de la fenêtre JDialog
        dialog.setSize(new Dimension(600, 400));
        dialog.setLocationRelativeTo(frame);
        
        // Afficher la fenêtre JDialog
        dialog.setVisible(true);
    }
});

        
        // Ajoute un ActionListener au bouton "Quitter"

        quitButton.addActionListener(e -> {
            customDialog dialog = new customDialog(frame);
            dialog.setVisible(true);
        });

        // Personnalisation des boutons
        Button.customizeButtons(playButton, optionsButton, profilButton, quitButton);
        // Applique le style principal aux boutons, à l'exception du bouton 'rules'
        Button.mainStyle(buttonsPanel, playButton, optionsButton,profilButton, quitButton);
        rulesButton.setBorder(BorderFactory.createEmptyBorder(0, 0, -40, 0));
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

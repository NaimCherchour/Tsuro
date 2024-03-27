package main.java.vue;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        customizeButtons(playButton, optionsButton, quitButton);
        // Applique le style principal aux boutons, à l'exception du bouton 'rules'
        mainStyle(buttonsPanel, playButton, optionsButton, quitButton);
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
           
            frame.setVisible(false);
            // nouvelle scene pour choisir le mode de jeu
            JFrame newFrame = new JFrame("Mode de jeu:");
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            newFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Affiche de la fenetre principale apres avoir fermer
                    frame.setVisible(true);
                }
            });

            // boutons "Jouer contre le bot" et "Jouer multijoueur"
            JButton playAgainstBotButton = new JButton("Jouer contre le bot");
            JButton playMultiplayerButton = new JButton("Jouer multijoueur");
            JButton playOnLine = new JButton("Jouer en ligne");

            playAgainstBotButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    playAgainstBotButton.setForeground(Color.RED);
                }

                public void mouseExited(MouseEvent e) {
                    playAgainstBotButton.setForeground(Color.BLACK);
                }
            });

            playMultiplayerButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    playMultiplayerButton.setForeground(Color.RED);
                }

                public void mouseExited(MouseEvent e) {
                    playMultiplayerButton.setForeground(Color.BLACK);
                }
            });

            playOnLine.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    playOnLine.setForeground(Color.RED);
                }

                public void mouseExited(MouseEvent e) {
                    playOnLine.setForeground(Color.BLACK);
                }
            });

            // Ajouter les boutons à un panneau
            JPanel newPanel = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon backgroundImage = new ImageIcon("src/main/ressources/fond.png");
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            };
            newPanel.add(playAgainstBotButton);
            newPanel.add(playMultiplayerButton);
            newPanel.add(playOnLine);

            newPanel.setBorder(BorderFactory.createEmptyBorder(68, 0, 0, 0));
            newPanel.setOpaque(false);
           
            //placement des boutons en colonne et au milieu de la fenetre 
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.insets = new Insets(20, 0, 20, 0); // Espacement entre les boutons
            constraints.anchor = GridBagConstraints.CENTER;

         
            newPanel.add(playAgainstBotButton, constraints);

            constraints.gridy = 1;
            newPanel.add(playMultiplayerButton, constraints);

            constraints.gridy = 2;
            newPanel.add(playOnLine, constraints);

           
            newFrame.getContentPane().add(newPanel);

            // taille et la position de la nouvelle scène
            newFrame.setSize(1065, 600);
            newFrame.setLocationRelativeTo(null);

            newFrame.setVisible(true);

        });

        
        frame.setVisible(true);

    }

    /**
     * Applique un style spécifique au panneau principal en utilisant GridBagLayout.
     * 
     * @param panel   Le panneau à styliser.
     * @param buttons Les boutons à ajouter au panneau.
     */
    private static void mainStyle(JPanel panel, JButton... buttons) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        // Ajoute les boutons au panneau avec le style défini
        for (JButton button : buttons) {
            panel.add(button, gbc);
        }
    }

    /**
     * Personnalise l'apparence des boutons en utilisant des images.
     * 
     * @param buttons Les boutons à personnaliser.
     */
    private static void customizeButtons(JButton... buttons) {
        for (JButton button : buttons) {
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);

            // Utiliser les noms de fichiers fournis pour les icônes de base
            ImageIcon icon = new ImageIcon("src/main/ressources/" + button.getActionCommand() + "Button.png");
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(150, 80, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(newImg));

            // Utiliser les noms de fichiers fournis pour les icônes pressées
            ImageIcon pressedIcon = new ImageIcon(
                    "src/main/ressources/" + button.getActionCommand() + "ButtonPressed.png");
            Image pressedImg = pressedIcon.getImage();
            Image newPressedImg = pressedImg.getScaledInstance(150, 80, java.awt.Image.SCALE_SMOOTH);
            button.setPressedIcon(new ImageIcon(newPressedImg));

            // Utiliser les noms de fichiers fournis pour les icônes de survol
            ImageIcon rolloverIcon = new ImageIcon(
                    "src/main/ressources/" + button.getActionCommand() + "ButtonHover.png");
            Image rolloverImg = rolloverIcon.getImage();
            Image newRolloverImg = rolloverImg.getScaledInstance(150, 80, java.awt.Image.SCALE_SMOOTH);
            button.setRolloverIcon(new ImageIcon(newRolloverImg));

            // Ajoute un MouseAdapter pour changer l'icône lors du clic
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button.setIcon(new ImageIcon(newPressedImg));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setIcon(new ImageIcon(newImg));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setIcon(new ImageIcon(newImg));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setIcon(new ImageIcon(newRolloverImg));
                }
            });
        }
    }

}

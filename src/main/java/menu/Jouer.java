package main.java.menu;

import main.java.Main;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Classe responsable de gérer l'interaction utilisateur dans le cadre d'un jeu,
 * notamment le traitement des clics sur les boutons du menu de jeu.
 */
public class Jouer {

    private static final int CHARGING_TIME = 4; // Durée de la scène de chargement

    /**
     * Gère les clics sur les boutons dans le menu du jeu.
     * @param frame La fenêtre principale dans laquelle les éléments du jeu sont chargés.
     * @param cursorFrame Une instance de AnimatedCursorFrame contenant les curseurs personnalisés.
     */
    public static void gererClicSurBoutonJouer(JFrame frame, AnimatedCursorFrame cursorFrame,String username) {
        Cursor hoverCursor = cursorFrame.getHoverCursor();  // Curseur lors du survol d'un bouton.
        Cursor defaultCursor = cursorFrame.getDefaultCursor();  // Curseur par défaut.

        // Suppression de tous les composants actuellement présents dans le conteneur principal de la fenêtre.
        frame.getContentPane().removeAll();

        // Création et configuration d'un nouveau panneau pour les options de jeu.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);  // Rendre le panneau transparent.
        panel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));  // Ajout d'une marge en haut du panneau.

        GridBagConstraints gbc = new GridBagConstraints();  // Contraintes pour le positionnement des composants.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Création des boutons pour les différentes options de jeu.
        JButton soloButton = createButton("src/main/resources/gameSolo.png", 175, hoverCursor, defaultCursor, frame, "src/main/resources/gameSoloHovered.png", "src/main/resources/gameSoloHovered.png");
        JButton localButton = createButton("src/main/resources/gameLocal.png", 175, hoverCursor, defaultCursor, frame, "src/main/resources/gameLocalHovered.png", "src/main/resources/gameLocalHovered.png");
        JButton onlineButton = createButton("src/main/resources/gameOnline.png", 175, hoverCursor, defaultCursor, frame, "src/main/resources/gameOnlineHovered.png", "src/main/resources/gameOnlineHovered.png");

        // Ajout d'une action pour jouer un son lors du clic sur un bouton.
        ActionListener playSoundAction = e -> playSound("src/main/resources/sound/buttonClickSound.wav");
        soloButton.addActionListener(playSoundAction);
        localButton.addActionListener(playSoundAction);
        onlineButton.addActionListener(playSoundAction);

        // Ajout des boutons au panneau.
        panel.add(soloButton, gbc);
        gbc.gridy++;
        panel.add(localButton, gbc);
        gbc.gridy++;
        panel.add(onlineButton, gbc);

        // Ajout du panneau à la fenêtre principale.
        frame.add(panel, BorderLayout.CENTER);

        // Création du bouton de retour avec changement d'image au survol et lors du clic.
        JButton backButton = createButton("src/main/resources/returnButton.png", 75, hoverCursor, defaultCursor, frame, "src/main/resources/returnButtonHovered.png", "src/main/resources/returnButtonPressed.png");
        backButton.addActionListener(e -> {
            playSound("src/main/resources/sound/buttonClickSound.wav");
            MainMenu.createAndShowGUI(frame,username);  // Assurer que le menu principal gère également correctement le curseur.
        });

        soloButton.addActionListener(e-> {
            playSound("src/main/resources/sound/buttonClickSound.wav");
            //TODO: Scène de Chargement Rapide 2 secondes
            int selectedMode = 0;
            boolean flag = false;
            while (!flag) {
                Object[] options = {"Classic", "Longest Path", "Timer"};
                int choice = JOptionPane.showOptionDialog(null, "Sélectionnez le mode de jeu :", "Choix du Mode de Jeu",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == JOptionPane.CLOSED_OPTION) {
                    // Close the dialog without exiting the application
                    return; // Exit the action listener
                } else {
                    // User has made a selection
                    selectedMode = choice ;
                    flag = true; // Exit the loop
                }
            }
            Main.solo(selectedMode,username);
        });

        localButton.addActionListener(e -> {
            playSound("src/main/resources/sound/buttonClickSound.wav");
            int numberOfPlayers = 0 ;
            while (true) {
                String input = JOptionPane.showInputDialog("Enter the number of players (2-8):");
                if (input == null) {
                    // Close the dialog without exiting the application
                    return; // Exit the action listener
                }
                try {
                    numberOfPlayers = Integer.parseInt(input);
                    if ( !(numberOfPlayers >= 2 && numberOfPlayers <= 8) ) {
                        JOptionPane.showMessageDialog(null, "Enter a number between 2 and 8.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter a valid number.");
                }
            }

            int selectedMode = 0;
            boolean flag = false;
            while (!flag) {
                Object[] options = {"Classic", "Longest Path", "Timer"};
                int choice = JOptionPane.showOptionDialog(null, "Sélectionnez le mode de jeu :", "Choix du Mode de Jeu",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == JOptionPane.CLOSED_OPTION) {
                    // Close the dialog without exiting the application
                    return; // Exit the action listener
                } else {
                    // User has made a selection
                    selectedMode = choice ;
                    flag = true; // Exit the loop
                }
            }
            //TODO: Scène de Chargement Rapide 2 secondes
            Main.multiPlayer(numberOfPlayers,selectedMode,username);

        });

        onlineButton.addActionListener(e-> {
            playSound("src/main/resources/sound/buttonClickSound.wav");
            //TODO: Scène de Chargement Rapide 4 secondes
            System.out.println("Online game...");
        });


        // Ajout du bouton de retour à un panneau en haut de la fenêtre.
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(backButton);
        frame.add(topPanel, BorderLayout.NORTH);

        // Mise à jour et rafraîchissement de la fenêtre pour afficher les modifications.
        frame.revalidate();
        frame.repaint();
    }


    public static void showChargingScene(JFrame frame) {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setString("Charging...");
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(progressBar, BorderLayout.CENTER);

        // Create a dialog to display the charging scene
        JDialog dialog = new JDialog(frame, "Charging Scene", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Disable closing the dialog
        dialog.getContentPane().add(panel);
        dialog.setSize(200, 100);
        dialog.setLocationRelativeTo(frame);

        Timer timer = new Timer(CHARGING_TIME * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                // Stop the timer
                ((Timer) e.getSource()).stop();
            }
        });
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent windowEvent) {
                timer.start();
            }
        });
        dialog.setVisible(true);
    }


    /**
     * Crée un bouton avec des images personnalisées pour les états normal, survolé et pressé.
     * @param imagePath Chemin de l'image normale.
     * @param width Largeur du bouton.
     * @param hoverCursor Curseur lors du survol.
     * @param defaultCursor Curseur par défaut.
     * @param frame Fenêtre contenant le bouton.
     * @param hoverImagePath Chemin de l'image lors du survol.
     * @param pressedImagePath Chemin de l'image lors du clic.
     * @return Un JButton configuré avec les images et les curseurs spécifiés.
     */
    private static JButton createButton(String imagePath, int width, Cursor hoverCursor, Cursor defaultCursor, JFrame frame, String hoverImagePath, String pressedImagePath) {
        ImageIcon normalIcon = new ImageIcon(imagePath);
        ImageIcon hoverIcon = new ImageIcon(hoverImagePath);
        ImageIcon pressedIcon = new ImageIcon(pressedImagePath);
        double aspectRatio = (double) normalIcon.getIconWidth() / (double) normalIcon.getIconHeight();
        int height = (int) (width / aspectRatio);
        Image image = normalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(image));
        button.setRolloverIcon(new ImageIcon(hoverIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setPressedIcon(new ImageIcon(pressedIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                frame.getContentPane().setCursor(hoverCursor);  // Change le curseur pour tout le contenu de la fenêtre lors du survol.
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                frame.getContentPane().setCursor(defaultCursor);  // Réinitialise le curseur pour tout le contenu de la fenêtre après le survol.
            }
        });

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Joue un fichier sonore spécifié.
     * @param soundFileName Chemin vers le fichier sonore à jouer.
     */
    private static void playSound(String soundFileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFileName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

package main.java.vue;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * Cette classe crée une boîte de dialogue personnalisée avec des options Oui et Non.
 * Elle utilise des images pour les boutons et des curseurs personnalisés pour améliorer l'interface utilisateur.
 */
public class customDialog extends JDialog {
    private Cursor defaultCursor;  // Curseur par défaut
    private Cursor hoverCursor;    // Curseur lors du survol

    /**
     * Constructeur qui initialise la boîte de dialogue avec un cadre parent spécifié.
     * @param parentFrame Le cadre (JFrame) qui est le parent de cette boîte de dialogue.
     */
    public customDialog(JFrame parentFrame) {
        super(parentFrame, "Confirmation", true);  // Appelle le constructeur de JDialog avec modalité

        // Configuration des images pour les curseurs
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image defaultCursorImage = toolkit.getImage("src/main/ressources/defaultCursor.png");
        Image hoverCursorImage = toolkit.getImage("src/main/ressources/hoverCursor.png");
        defaultCursor = toolkit.createCustomCursor(defaultCursorImage, new Point(0, 0), "defaultCursor");
        hoverCursor = toolkit.createCustomCursor(hoverCursorImage, new Point(0, 0), "hoverCursor");

        setCursor(defaultCursor);  // Définit le curseur par défaut pour la boîte de dialogue

        // Adapteur de souris pour changer le curseur lors du survol des boutons
        MouseAdapter buttonHoverAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(hoverCursor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(defaultCursor);
            }
        };

        // Configuration de l'image de fond
        ImageIcon backgroundImageIcon = new ImageIcon("src/main/ressources/fondConv1.png");
        setSize(backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());
        setLayout(new BorderLayout());

        // Création du label qui contiendra l'image de fond
        JLabel background = new JLabel(backgroundImageIcon);
        background.setLayout(new FlowLayout(FlowLayout.CENTER, 20, backgroundImageIcon.getIconHeight() / 2));
        add(background);

        // Configuration du bouton Oui
        JButton yesButton = new JButton(new ImageIcon("src/main/ressources/ouiButton.png"));
        yesButton.setBorder(BorderFactory.createEmptyBorder());
        yesButton.setContentAreaFilled(false);
        yesButton.addMouseListener(buttonHoverAdapter);
        yesButton.addActionListener(e -> {
            playSound("src/main/ressources/buttonClickSound.wav");
            System.exit(0);  // Quitte l'application si cliqué
        });

        // Configuration du bouton Non
        JButton noButton = new JButton(new ImageIcon("src/main/ressources/nonButton.png"));
        noButton.setBorder(BorderFactory.createEmptyBorder());
        noButton.setContentAreaFilled(false);
        noButton.addMouseListener(buttonHoverAdapter);
        noButton.addActionListener(e -> {
            playSound("src/main/ressources/buttonClickSound.wav");
            dispose();  // Ferme la boîte de dialogue si cliqué
        });

        background.add(yesButton);
        background.add(noButton);

        // Positionne la boîte de dialogue au centre du cadre parent
        setLocationRelativeTo(parentFrame);
    }

    /**
     * Joue un son à partir du fichier spécifié.
     * @param soundFileName Le chemin vers le fichier audio.
     */
    private void playSound(String soundFileName) {
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

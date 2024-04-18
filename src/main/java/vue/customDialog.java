package main.java.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class customDialog extends JDialog {
    private Cursor defaultCursor;
    private Cursor hoverCursor;

    public customDialog(JFrame parentFrame) {
        // Configuration de base de la boîte de dialogue
        super(parentFrame, "Confirmation", true);

        // Configuration des curseurs personnalisés
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image defaultCursorImage = toolkit.getImage("src/main/ressources/defaultCursor.png");
        defaultCursor = toolkit.createCustomCursor(defaultCursorImage, new Point(0, 0), "defaultCursor");
        Image hoverCursorImage = toolkit.getImage("src/main/ressources/hoverCursor.png");
        hoverCursor = toolkit.createCustomCursor(hoverCursorImage, new Point(0, 0), "hoverCursor");

        // Affectation du curseur par défaut à la boîte de dialogue
        setCursor(defaultCursor);

        // Création de l'adaptateur de la souris pour le changement de curseur
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

        // Charger l'image de fond et ajuster la taille de la boîte de dialogue
        ImageIcon backgroundImageIcon = new ImageIcon("src/main/ressources/fondConv1.png");
        setSize(backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());
        setLayout(new BorderLayout());

        // Créer un label pour contenir l'image de fond
        JLabel background = new JLabel(backgroundImageIcon);
        background.setLayout(new FlowLayout(FlowLayout.CENTER, 20, backgroundImageIcon.getIconHeight() / 2));
        add(background);

        // Créer et configurer le bouton Oui avec l'image et sans bordure/arrière-plan
        JButton yesButton = new JButton(new ImageIcon("src/main/ressources/ouiButton.png"));
        yesButton.setBorder(BorderFactory.createEmptyBorder());
        yesButton.setContentAreaFilled(false);
        yesButton.addMouseListener(buttonHoverAdapter); // Ajouter l'adaptateur de la souris

        // Créer et configurer le bouton Non avec l'image et sans bordure/arrière-plan
        JButton noButton = new JButton(new ImageIcon("src/main/ressources/nonButton.png"));
        noButton.setBorder(BorderFactory.createEmptyBorder());
        noButton.setContentAreaFilled(false);
        noButton.addMouseListener(buttonHoverAdapter); // Ajouter l'adaptateur de la souris

        // Ajouter des actions aux boutons
        yesButton.addActionListener(e -> {
            // Action pour le bouton Oui
            System.exit(0);
        });

        noButton.addActionListener(e -> {
            // Action pour le bouton Non
            dispose(); // Ferme la boîte de dialogue
        });

        background.add(yesButton);
        background.add(noButton);

        // Centrer la boîte de dialogue par rapport au parent
        setLocationRelativeTo(parentFrame);
    }
}

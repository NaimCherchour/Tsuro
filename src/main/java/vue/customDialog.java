package main.java.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class customDialog extends JDialog {
    public customDialog(JFrame parentFrame) {
        // Configuration de base de la boîte de dialogue
        super(parentFrame, "Confirmation", true);
        
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
        
        // Créer et configurer le bouton Non avec l'image et sans bordure/arrière-plan
        JButton noButton = new JButton(new ImageIcon("src/main/ressources/nonButton.png"));
        noButton.setBorder(BorderFactory.createEmptyBorder());
        noButton.setContentAreaFilled(false);
        
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

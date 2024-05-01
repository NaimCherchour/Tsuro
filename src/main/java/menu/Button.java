package main.java.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button {

    /**
     * Applique un style spécifique au panneau principal en utilisant GridBagLayout.
     * 
     * @param panel   Le panneau à styliser.
     * @param buttons Les boutons à ajouter au panneau.
     */
    public static void mainStyle(JPanel panel, JButton... buttons) {
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
    public static void customizeButtons(JButton... buttons) {
        for (JButton button : buttons) {
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);

            // Utiliser les noms de fichiers fournis pour les icônes de base
            ImageIcon icon = new ImageIcon("src/main/resources/" + button.getActionCommand() + "Button.png");
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(150, 80, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(newImg));

            // Utiliser les noms de fichiers fournis pour les icônes pressées
            ImageIcon pressedIcon = new ImageIcon(
                    "src/main/resources/" + button.getActionCommand() + "ButtonPressed.png");
            Image pressedImg = pressedIcon.getImage();
            Image newPressedImg = pressedImg.getScaledInstance(150, 80, java.awt.Image.SCALE_SMOOTH);
            button.setPressedIcon(new ImageIcon(newPressedImg));

            // Utiliser les noms de fichiers fournis pour les icônes de survol
            ImageIcon rolloverIcon = new ImageIcon(
                    "src/main/resources/" + button.getActionCommand() + "ButtonHover.png");
            Image rolloverImg = rolloverIcon.getImage();
            Image newRolloverImg = rolloverImg.getScaledInstance(150, 80, java.awt.Image.SCALE_SMOOTH);
            button.setRolloverIcon(new ImageIcon(newRolloverImg));

            // Ajouter un MouseAdapter pour changer l'icône lors du clic
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


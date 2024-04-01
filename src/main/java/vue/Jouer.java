package main.java.vue;

import javax.swing.*;
import java.awt.*;

public class Jouer {

    public static void gererClicSurBoutonJouer(JFrame frame) {
        // Supprimer les éléments actuels de la frame
        frame.getContentPane().removeAll();

        // Créer le nouveau panneau pour le choix du mode de jeu
        JPanel panel = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout
        panel.setOpaque(false); // Panneau transparent

        // Ajouter une marge en haut du panneau
        panel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); // 50 pixels de marge en haut

        // Créer une contrainte pour centrer les composants
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Charger les icônes des boutons
        ImageIcon soloIcon = new ImageIcon("src/main/ressources/gameSolo.png");
        ImageIcon localIcon = new ImageIcon("src/main/ressources/gameLocal.png");
        ImageIcon onlineIcon = new ImageIcon("src/main/ressources/gameOnline.png");

        // Redimensionner les icônes
        Image soloImage = soloIcon.getImage().getScaledInstance(220, 100, Image.SCALE_SMOOTH);
        Image localImage = localIcon.getImage().getScaledInstance(220, 100, Image.SCALE_SMOOTH);
        Image onlineImage = onlineIcon.getImage().getScaledInstance(220, 100, Image.SCALE_SMOOTH);

        // Créer les ImageIcon redimensionnés
        ImageIcon resizedSoloIcon = new ImageIcon(soloImage);
        ImageIcon resizedLocalIcon = new ImageIcon(localImage);
        ImageIcon resizedOnlineIcon = new ImageIcon(onlineImage);

        // Créer les boutons avec les ImageIcon redimensionnés
        JButton soloButton = new JButton(resizedSoloIcon);
        JButton localButton = new JButton(resizedLocalIcon);
        JButton onlineButton = new JButton(resizedOnlineIcon);

        // Rendre le fond des boutons transparents
        soloButton.setOpaque(false);
        soloButton.setContentAreaFilled(false);
        soloButton.setBorderPainted(false);

        localButton.setOpaque(false);
        localButton.setContentAreaFilled(false);
        localButton.setBorderPainted(false);

        onlineButton.setOpaque(false);
        onlineButton.setContentAreaFilled(false);
        onlineButton.setBorderPainted(false);

        // Ajouter les boutons au panneau avec la contrainte de centrage
        panel.add(soloButton, gbc);
        gbc.gridy++;
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(localButton, gbc);
        gbc.gridy++;
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(onlineButton, gbc);

        // Ajouter le panneau à la frame
        frame.add(panel, BorderLayout.CENTER);

        // Rafraîchir l'affichage de la frame
        frame.revalidate();
        frame.repaint();
    }
}


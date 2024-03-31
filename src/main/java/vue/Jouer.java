package main.java.vue;

import javax.swing.*;
import java.awt.*;

public class Jouer {

    public static void gererClicSurBoutonJouer(JFrame frame) {
        // Supprimer les éléments actuels de la frame
        frame.getContentPane().removeAll();

        // Créer le nouveau panneau pour le choix du mode de jeu
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(68, 0, 0, 0));
        panel.setOpaque(false);

        // Créer et personnaliser les boutons pour les différents modes de jeu
        JButton soloButton = new JButton(new ImageIcon("src/main/ressources/gameSolo.png")); // Nouvelle image pour le mode solo
        soloButton.setActionCommand("solo");
        JButton localButton = new JButton(new ImageIcon("src/main/ressources/gameLocal.png")); // Nouvelle image pour le mode local
        localButton.setActionCommand("local");
        JButton onlineButton = new JButton(new ImageIcon("src/main/ressources/gameOnline.png")); // Nouvelle image pour le mode en ligne
        onlineButton.setActionCommand("online");

        // Personnaliser les boutons
        Button.customizeButtons(soloButton, localButton, onlineButton);

        // Ajouter les boutons au panneau
        panel.add(soloButton);
        panel.add(localButton);
        panel.add(onlineButton);

        // Ajouter le panneau à la frame
        frame.add(panel, BorderLayout.CENTER);

        // Rafraîchir l'affichage de la frame
        frame.revalidate();
        frame.repaint();
    }
}

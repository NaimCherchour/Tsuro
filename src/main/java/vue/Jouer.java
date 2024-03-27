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
        JButton bot = new JButton(new ImageIcon("src/main/ressources/playButton.png"));
        bot.setActionCommand("play");
        JButton multi = new JButton(new ImageIcon("src/main/ressources/optionsButton.png"));
        multi.setActionCommand("options");
        JButton enligne = new JButton(new ImageIcon("src/main/ressources/quitButton.png"));
        enligne.setActionCommand("quit");

        Button.customizeButtons(bot, multi, enligne);
        Button.mainStyle(panel, bot, multi, enligne);

        // Ajouter le panneau à la frame
        frame.add(panel, BorderLayout.CENTER);

        // Rafraîchir l'affichage de la frame
        frame.revalidate();
        frame.repaint();
    }
}

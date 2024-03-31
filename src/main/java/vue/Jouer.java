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

       
        ImageIcon soloIcon = new ImageIcon("src/main/ressources/gameSolo.png");
        ImageIcon localIcon = new ImageIcon("src/main/ressources/gameLocal.png");
        ImageIcon onlineIcon = new ImageIcon("src/main/ressources/gameOnline.png");
        ImageIcon playIcon = new ImageIcon("src/main/ressources/playButton.png");

        // redimensionner les images
        Image soloImage = soloIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image localImage = localIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image onlineImage = onlineIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image playImage = playIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        // Creation des ImageIcon redimensionnés
        ImageIcon resizedSoloIcon = new ImageIcon(soloImage);
        ImageIcon resizedLocalIcon = new ImageIcon(localImage);
        ImageIcon resizedOnlineIcon = new ImageIcon(onlineImage);
        ImageIcon resizedPlayIcon = new ImageIcon(playImage);

        // Créer les boutons avec les ImageIcon redimensionnés
        JButton soloButton = new JButton(resizedSoloIcon);
        JButton localButton = new JButton(resizedLocalIcon);
        JButton onlineButton = new JButton(resizedOnlineIcon);
       

        // Définir la taille des boutons
        soloButton.setPreferredSize(new Dimension(100, 100));
        localButton.setPreferredSize(new Dimension(100, 100));
        onlineButton.setPreferredSize(new Dimension(100, 100));
        
       

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

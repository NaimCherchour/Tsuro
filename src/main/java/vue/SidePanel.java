package main.java.vue;

import main.java.model.Joueur;
import main.java.model.Tuile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidePanel extends JPanel {
    private final GameBoardUI gameBoardUI;

    public SidePanel(GameBoardUI gameBoardUI) throws IOException {
        this.gameBoardUI = gameBoardUI;
        
        setPreferredSize(new Dimension(200, 600)); // Définir une largeur fixe et ajuster la hauteur automatiquement
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Utiliser BoxLayout pour aligner les composants verticalement
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // Ajouter une marge autour du panneau

        // Boucle pour ajouter trois tuiles avec des boutons de rotation à chaque itération
        for (int i = 0; i < 3; i++) {
            add(Box.createVerticalGlue()); // Ajouter un espace vertical flexible
            // Créer un TuilePanel et l'ajouter au panneau latéral
            TuilePanel tuilePanel = new TuilePanel();
            add(tuilePanel);
            // Ajouter un espace vertical fixe entre chaque TuilePanel et son bouton de rotation
            add(Box.createRigidArea(new Dimension(0, 10))); // Ajuster la hauteur selon les besoins
            JButton bouton = createButtonRotation();
            add(bouton); // Ajouter le bouton au panneau latéral
            add(Box.createRigidArea(new Dimension(0, 10))); // Ajuster la hauteur selon les besoins
            // Créer un bouton de rotation et lui ajouter un ActionListener pour placer la tuile sélectionnée
            JButton bouton2 = createButton(tuilePanel);
            add(bouton2); // Ajouter le bouton au panneau latéral
        }
    }

    private JButton createButtonRotation() {
        JButton bouton = new JButton("Rotate");
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT); // Aligner le bouton au centre horizontalement
        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateTile(); // Appeler la méthode pour faire pivoter la tuile
            }
        });
        return bouton;
    }

    private JButton createButton(TuilePanel tuilePanel) {
        JButton bouton = new JButton("Place");
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT); // Aligner le bouton au centre horizontalement
        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeTile(gameBoardUI.getJoueur(),tuilePanel.getTuile()); // Appeler la méthode pour placer la tuile sélectionnée
            }
        });
        return bouton;
    }

    // Méthode pour placer la tuile sélectionnée sur le plateau
    private void placeTile(Joueur j , Tuile tuile) {
        gameBoardUI.placerTuileSurPlateau(j,tuile); // Appeler la méthode pour placer la tuile dans GameBoardUI
    }

    // Method to rotate the tile and repaint
    private void rotateTile() {
        // Call the rotateTile method in the GameBoardUI class
        gameBoardUI.rotateTile();
    }
}


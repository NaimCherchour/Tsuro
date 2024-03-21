package main.java.vue;

import main.java.model.DeckTuiles;
import main.java.model.Joueur;
import main.java.model.Tuile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidePanel extends JPanel {
    private final GameBoardUI gameBoardUI;
    private final DeckTuiles deckTuiles;


    public SidePanel(GameBoardUI gameBoardUI,DeckTuiles deckTuiles) throws IOException {
        this.gameBoardUI = gameBoardUI;
        this.deckTuiles = deckTuiles;

        setPreferredSize(new Dimension(200, 600)); // Définir une largeur fixe et ajuster la hauteur automatiquement
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Utiliser BoxLayout pour aligner les composants verticalement
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // Ajouter une marge autour du panneau
        refreshDeck();
    }

    private void refreshDeck() throws IOException {
        removeAll();

        for (int i = 0; i < deckTuiles.getSideTuiles().length; i++) {
            Tuile tuile = deckTuiles.getSideTuiles()[i];
            if (tuile != null) {
                add(Box.createVerticalGlue());
                TuilePanel tuilePanel = new TuilePanel(tuile);
                add(tuilePanel);
                add(Box.createRigidArea(new Dimension(0, 10)));
                JButton bouton = createButtonRotation(tuilePanel);
                add(bouton); // Ajouter le bouton au panneau latéral
                add(Box.createRigidArea(new Dimension(0, 10))); // Ajuster la hauteur selon les besoins
                // Créer un bouton de rotation et lui ajouter un ActionListener pour placer la tuile sélectionnée
                JButton bouton2 = createButton(tuilePanel,i);
                add(bouton2); // Ajouter le bouton au panneau latéral
            }
        }
        revalidate();
        repaint();
    }


    private JButton createButtonRotation(TuilePanel tuilePanel) {
        JButton bouton = new JButton("Rotate");
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT); // Aligner le bouton au centre horizontalement
        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateTile(tuilePanel.getTuile()); // Appeler la méthode pour faire pivoter la tuile
            }
        });
        return bouton;
    }

    private JButton createButton(TuilePanel tuilePanel , int index) {
        JButton bouton = new JButton("Place");
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT); // Aligner le bouton au centre horizontalement
        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    placerTuileFromDeck(index); // Appeler la méthode pour placer la tuile sélectionnée
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return bouton;
    }


    private void placerTuileFromDeck(int index ) throws IOException {
        Tuile tuile = deckTuiles.getTuile(index);
        if (tuile != null) {
            refreshDeck();
            gameBoardUI.placerTuileSurPlateau(gameBoardUI.getJoueur(), tuile);
        } else {
            JOptionPane.showMessageDialog(null, "No tiles in hand!");
        }
    }

    // Method to rotate the tile and repaint
    private void rotateTile( Tuile tuile) {
        // Call the rotateTile method in the GameBoardUI class
        tuile.tournerTuile(); // Appeler la méthode pour faire pivoter la tuile
        repaint();
        gameBoardUI.rotateTile(tuile);
    }
}


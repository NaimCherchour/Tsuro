package main.java.vue;

import main.java.model.DeckTuiles;
import main.java.model.Tuile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidePanel extends JPanel {
    private final GameBoardUI gameBoardUI; // Référence à l'interface utilisateur du plateau de jeu
    private final DeckTuiles deckTuiles; // Référence au deck de tuiles


    public SidePanel(GameBoardUI gameBoardUI,DeckTuiles deckTuiles) throws IOException {
        this.gameBoardUI = gameBoardUI;
        this.deckTuiles = deckTuiles;
        setPreferredSize(new Dimension(200, 600));
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
                add(bouton);
                add(Box.createRigidArea(new Dimension(0, 10)));
                JButton bouton2 = createButton(tuilePanel,i);
                add(bouton2);
            }
        }
        revalidate(); // Revalider le panneau pour afficher les modifications
        repaint(); // Redessiner le panneau
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
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
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


    /**
     * Place la tuile sélectionnée sur le plateau de jeu.
     * @param index L'indice de la tuile sélectionnée. soit 0 ou 1 ou 2 selon l'ordre dans le panel
     * @throws IOException
     */

    private void placerTuileFromDeck(int index) throws IOException {
        Tuile tuile = deckTuiles.getTuile(index);
        if (tuile != null) {
            refreshDeck(); // Rafraîchir le deck après avoir placé la tuile
            gameBoardUI.placerTuileSurPlateau(gameBoardUI.getJoueur(), tuile); // Appeler la méthode pour placer la tuile sur le plateau
        } else {
            JOptionPane.showMessageDialog(null, "No tiles placed!");
        }
    }

    /**
     *  Method to rotate the tile and repaint
     * @param tuile The tile to rotate
     */
    private void rotateTile( Tuile tuile) {
        // Call the rotateTile method in the GameBoardUI class
        // TODO : Model Detected
        tuile.tournerTuile(); // Appeler la méthode pour faire pivoter la tuile du MODEL
        repaint();
    }
}


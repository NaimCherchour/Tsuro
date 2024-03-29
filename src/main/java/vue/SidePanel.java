package main.java.vue;

import main.java.model.DeckTuiles;
import main.java.model.Tuile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class SidePanel extends JPanel {
    private final GameBoardUI gameBoardUI;
    private final DeckTuiles deckTuiles;
    private JPanel filtre;


    public SidePanel(GameBoardUI gameBoardUI,DeckTuiles deckTuiles) throws IOException {
        this.gameBoardUI = gameBoardUI;
        this.deckTuiles = deckTuiles;

        filtre = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(0, 0, 120, 120);
                g2d.dispose();
            }
        };

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
                int x = i ;
                tuilePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        filtre.setBounds(0, 0, 120, 120);
                        filtre.setOpaque(false);
                        tuilePanel.setOpaque(false);
                        tuilePanel.add(filtre);
                        tuilePanel.setBackground(new Color(0, 0, 0, 0));
                    }
                    public void mouseExited(MouseEvent e) {
                        tuilePanel.remove(filtre);
                        tuilePanel.setBackground(null);
                    }
                    public void mouseClicked(MouseEvent e) {
                        try {
                            placerTuileFromDeck(x); // Appeler la méthode pour placer la tuile sélectionnée
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                add(tuilePanel);
                add(Box.createRigidArea(new Dimension(0, 10)));
                JButton bouton = createButtonRotation(tuilePanel);
                add(bouton); // Ajouter le bouton au panneau latéral
                add(Box.createRigidArea(new Dimension(0, 10))); // Ajuster la hauteur selon les besoins
                // Créer un bouton de rotation et lui ajouter un ActionListener pour placer la tuile sélectionnée
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

    private void placerTuileFromDeck(int index ) throws IOException {
        Tuile tuile = deckTuiles.getTuile(index);
        if (tuile != null) {
            refreshDeck();
            gameBoardUI.placerTuileSurPlateau(tuile);
        } else {
            JOptionPane.showMessageDialog(null, "No tiles in hand!");
        }
    }

    // Method to rotate the tile and repaint
    private void rotateTile( Tuile tuile) {
        // Call the rotateTile method in the GameBoardUI class
        tuile.tournerTuile(); // Appeler la méthode pour faire pivoter la tuile
        repaint();
    }
}


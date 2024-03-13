package main.java.vue;

import main.java.model.Joueur;
import main.java.model.Tuile;
import main.java.model.Tuiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PlateauUI {

    private static final int GRID_SIZE = 6;
    private static final int TILE_SIZE = 120;
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 800;
    private JPanel filtre;
    private JPanel clickedPanel;
    private int clickedIndex;
    private JPanel Deck;

    public PlateauUI(Joueur joueur) {
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

        DessinateurDeTuile dessinateur;
        try {
            dessinateur = new DessinateurDeTuile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JFrame frame = new JFrame("Plateau de jeu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());

        // Créer un JPanel qui servira de conteneur pour la grille et les boutons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setOpaque(false);

        // Créer le panel de la grille avec un GridBagLayout
        JPanel gridPanel = new JPanel(new GridBagLayout());

        // Définir la taille fixe des cellules de la grille
        Dimension cellSize = new Dimension(TILE_SIZE, TILE_SIZE);

        // Dans la boucle de création de la grille, ajoutez le joueur à la case correspondante
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                final int ligne = row;
                final int colonne = col;
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.setPreferredSize(cellSize);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = row;
                gbc.gridy = col;
                gridPanel.add(panel,gbc);
                
                panel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        filtre.setBounds(0, 0, 120, 120);
                        filtre.setOpaque(false);
                        panel.setOpaque(false);
                        panel.add(filtre);
                        panel.setBackground(new Color(0, 0, 0, 0));
                        }
                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            panel.remove(filtre);
                            panel.setBackground(null);
                        }
                });
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (clickedPanel!=null){
                            gridPanel.remove((JPanel) e.getSource());
                        }
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.gridx = ligne; // Réglez la position en fonction de vos besoins
                        gbc.gridy = colonne; // Réglez la position en fonction de vos besoins
                        gridPanel.add(clickedPanel, gbc);
                        gridPanel.revalidate(); // Actualisez la disposition
                        joueur.supprimerTuile(clickedIndex);
                        Deck.revalidate();
                    }
                });
            }
        }

        // Créer le panel latéral pour les tuiles et les boutons
        Deck = new JPanel(new GridBagLayout());

        for (int i = 0; i < 3; i++) {
            final int index = i; // Variable finale pour stocker l'index de la tuile
            
            JPanel tilePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    dessinateur.dessinerTuile(g, joueur.getTuileJoueur(index), dessinateur.getSpritesSet());
                }
            };
        
            // Ajoutez un écouteur d'événements de clic pour chaque tuilePanel
            tilePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    clickedPanel = (JPanel) e.getSource();
                    // Obtenez l'index de la tuile dans le side panel
                    clickedIndex = index;
                    
                }
            });

            
            tilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            tilePanel.setPreferredSize(cellSize);

            GridBagConstraints ac = new GridBagConstraints();
            ac.gridx = 1;
            ac.gridy = i;
            Deck.add(tilePanel,ac);
            Deck.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 70));
            tilePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    filtre.setBounds(0, 0, 120, 120);
                    filtre.setOpaque(false);
                    tilePanel.setOpaque(false);
                    tilePanel.add(filtre);
                    tilePanel.setBackground(new Color(0, 0, 0, 0));
                    }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    tilePanel.remove(filtre);
                    tilePanel.setBackground(null);
                }
            });
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(Deck, BorderLayout.EAST);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Méthode pour ajouter le joueur à la case de la grille
    private void ajouterJoueurSurPlateau(Joueur joueur, JPanel panel) {
        JoueurUI joueurUI = new JoueurUI(joueur);
        panel.add(joueurUI);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer un nouveau joueur.
            Joueur joueur = new Joueur(3, 4, 2, "Max");

            // Créer la fenêtre du plateau avec le joueur.
            new PlateauUI(joueur);
        });
    }
}

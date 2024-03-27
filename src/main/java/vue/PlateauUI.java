package main.java.vue;

import main.java.controller.PlateauController;
import main.java.model.Joueur;
import main.java.model.PlateauTuiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Cette classe représente l'interface utilisateur du plateau de jeu.
 */

/*
public class PlateauUI {

    private static final int GRID_SIZE = 6;
    private static final int TILE_SIZE = 120;
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 800;
    private JPanel filtre;
    private JPanel clickedPanel;
    private int clickedIndex;
    private JPanel Deck;
    private JPanel gridPanel; // Déclaration de gridPanel comme un champ de classe

    private PlateauController plateauController;

    /**
     * Constructeur de la classe PlateauUI.
     *
     * @param joueur Le joueur à afficher sur le plateau.
     */
/*
    public PlateauUI(Joueur joueur) {

        // Initialisation du plateau et du contrôleur du plateau
        PlateauTuiles plateau = new PlateauTuiles(GRID_SIZE);
        plateauController = new PlateauController(plateau);

        // Initialisation du filtre pour les cases de la grille
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

        // Initialisation de la fenêtre du jeu
        JFrame frame = new JFrame("Plateau de jeu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());

        // Créer un JPanel qui servira de conteneur pour la grille et les boutons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setOpaque(false);

        // Initialisation de gridPanel
        gridPanel = new JPanel(new GridBagLayout());

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
                gridPanel.add(panel, gbc);

                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent evt) {
                        filtre.setBounds(0, 0, 120, 120);
                        filtre.setOpaque(false);
                        panel.setOpaque(false);
                        panel.add(filtre);
                        panel.setBackground(new Color(0, 0, 0, 0));
                    }

                    @Override
                    public void mouseExited(MouseEvent evt) {
                        panel.remove(filtre);
                        panel.setBackground(null);
                    }
                });

                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (clickedPanel != null) {
                            gridPanel.remove((JPanel) e.getSource());
                        //     int ligne = 0;// Calculer la ligne en fonction de la position du clic
                        //     int colonne = 0;// Calculer la colonne en fonction de la position du clic
                        //     plateauController.placerTuile(ligne, colonne, joueurActuel.getTuileJoueur(clickedIndex));
                        int caseWidth =TILE_SIZE /* Largeur d'une case de la grille */; 
        /*
                        int caseHeight =TILE_SIZE /* Hauteur d'une case de la grille */;
                        /*
        
                        int x = e.getX(); // Coordonnée X du clic
                        int y = e.getY(); // Coordonnée Y du clic
        
                        int ligne = y / caseHeight; // Calcul de la ligne en divisant la coordonnée Y par la hauteur d'une case
                        int colonne = x / caseWidth; // Calcul de la colonne en divisant la coordonnée X par la largeur d'une case
                        plateauController.placerTuile(ligne, colonne, joueur.getTuileJoueur(clickedIndex));
                         }
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.gridx = ligne;
                        gbc.gridy = colonne;
                        gridPanel.add(clickedPanel, gbc);
                        gridPanel.revalidate();
                        joueur.supprimerTuile(clickedIndex);
                        Deck.revalidate();
                    }
                });
            }
        }

        // Créer le panel latéral pour les tuiles et les boutons
        Deck = new JPanel(new GridBagLayout());

        for (int i = 0; i < 3; i++) {
            final int index = i;
            JPanel tilePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    dessinateur.dessinerTuile(g, joueur.getTuileJoueur(index), dessinateur.getSpritesSet(), 0, 0);
                }
            };

            tilePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    clickedPanel = (JPanel) e.getSource();
                    clickedIndex = index;
                }
            });

            tilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            tilePanel.setPreferredSize(cellSize);

            GridBagConstraints ac = new GridBagConstraints();
            ac.gridx = 1;
            ac.gridy = i;
            Deck.add(tilePanel, ac);
            Deck.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 70));
            tilePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    filtre.setBounds(0, 0, 120, 120);
                    filtre.setOpaque(false);
                    tilePanel.setOpaque(false);
                    tilePanel.add(filtre);
                    tilePanel.setBackground(new Color(0, 0, 0, 0));
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    tilePanel.remove(filtre);
                    tilePanel.setBackground(null);
                }
            });
        }

        mainPanel.add(gridPanel);
        mainPanel.add(ajouterJoueurSurPlateau(joueur));
        mainPanel.add(Deck, BorderLayout.EAST);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);

    }

    /**
     * Ajoute le joueur sur le plateau de jeu.
     *
     * @param joueur Le joueur à ajouter sur le plateau.
     * @return Une instance de JoueurUI représentant l'interface du joueur.
     */
/*
    private JoueurPanel ajouterJoueurSurPlateau(Joueur joueur) {
        JoueurPanel joueurUI = new JoueurPanel(joueur);
        return joueurUI;
    }

    /**
     * Méthode principale qui lance l'application.
     *
     * @param args Les arguments de la ligne de commande (non utilisés).
     */
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer un nouveau joueur.
            Joueur joueur = new Joueur( "Max");

            // Créer la fenêtre du plateau avec le joueur.
            new PlateauUI(joueur);
        });
    }
}
*/

/*
public Tuile[] GenereDeckJoueur(){
        Random random = new Random();
        Tuiles tuiles = new Tuiles();
        Tuile[]ret = new Tuile[3];
        for(int i=0;i<3;i++){
            Tuile t = tuiles.getTuile(random.nextInt(36) + 1);
            ret[i]=t;
        }
        return ret;
    }
    public Tuile getTuileJoueur(int i){
        if (i>=0 && i<3){
            return deck[i];
        }
        return null;
    }
    public void supprimerTuile(int i){
        Random random = new Random();
        Tuiles tuiles = new Tuiles();
        Tuile t = tuiles.getTuile(random.nextInt(36) + 1);
        deck[i]=t;

    }
 */

package main.java.vue;

import main.java.controller.Controller;
import main.java.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.Observable;


/**
 * Classe représentant l'interface graphique du plateau de jeu.
 * Contient 5 parties : le constructeur, l'esthétique, la gestion des événements, le dessin et la mise à jour.
 */
public class GameBoardUI extends JPanel implements GameObserver {
    private ReadOnlyGame game; // Quelques éléments visibles du model

    // TODO : Vérifier la relation entre le controller et la vue ; Normalement la vue n'a pas de référence vers le controller
    private Controller controller ;
    DessinateurDeTuile dessinateurDeTuile;
    private static final int CELL_SIZE = 120;
    private static final int LEFT_MARGIN = 200;
    private static final int TOP_MARGIN = 50;
    private static  final int BOARD_SIZE = 720;
    private static final int NUMBER_DECK_TILES = 3;
    private JPanel sidePanel;
    private  JPanel filtre ; // esthétique



    // PART1 : CONSTRUCTOR
    public GameBoardUI( Controller controller) throws IOException {
        this.dessinateurDeTuile = new DessinateurDeTuile();
        this.controller = controller; // Référence vers le controller
        this.addMouseListener(controller);
        this.filtre = initFiltre();
        addCellPanels(); // TODO : Revoir la modularité de l'esthétique du filtre et des cellules

        // Créer un nouveau JPanel latéral pour le deck
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(200, 600)); // Définir une largeur fixe et ajuster la hauteur automatiquement
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS)); // Utiliser BoxLayout pour aligner les composants verticalement
        sidePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // Ajouter une marge autour du panneau

        // Ajout du sidePanel à la disposition de GameBoardUI
        setLayout(new BorderLayout());
        add(sidePanel, BorderLayout.EAST); // Ajouter le sidePanel à l'est de GameBoardUI
    }

    // PART2 : AESTHETIC
    private JPanel initFiltre() {
        JPanel filtre = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(0, 0, 120, 120);
                g2d.dispose();
            }
        };
        return filtre;
    }

    private void addCellPanels() {
        for (int i = 0; i < BOARD_SIZE / CELL_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE / CELL_SIZE; j++) {
                JPanel cellPanel = new JPanel();
                cellPanel.setBounds(LEFT_MARGIN + j * CELL_SIZE, TOP_MARGIN + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                cellPanel.setOpaque(false);
                addMouseListeners(cellPanel);
                add(cellPanel);
            }
        }
    }

    private void addMouseListeners(JPanel cellPanel) {
        cellPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                filtre.setBounds(0, 0, 120, 120);
                filtre.setOpaque(false);
                cellPanel.setOpaque(false);
                cellPanel.add(filtre);
                cellPanel.setBackground(new Color(0, 0, 0, 0));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                cellPanel.remove(filtre);
                cellPanel.setBackground(null);
            }
        });
    }



    // PART3 : EVENT HANDLING
    private void refreshDeck() throws IOException {
        sidePanel.removeAll(); // Effacez les composants précédents du sidePanel
        for (int i = 0; i < NUMBER_DECK_TILES; i++) {
            Tuile tuile = game.getDeckTuiles().getSideTuiles()[i];
            if (tuile != null) {
                sidePanel.add(Box.createVerticalGlue());
                // TODO : Revoir cette logique , je pense ca ne devrait pas être ici plutot dans le controller mais comment faire
                // TODO : pour que le controller sache quel tuile a été cliqué
                TuilePanel tuilePanel = new TuilePanel(tuile);
                int x = i;
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
                        super.mouseClicked(e);
                        try {
                            placerTuileFromDeck(x);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                });
                sidePanel.add(tuilePanel);
                sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                JButton bouton = createButtonRotation(tuilePanel);
                sidePanel.add(bouton); // Ajouter le bouton au panneau latéral
                sidePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Ajuster la hauteur selon les besoins
            }
        }
        sidePanel.revalidate(); // Revalider le sidePanel pour mettre à jour les modifications
        sidePanel.repaint(); // Redessiner le sidePanel
    }

    private void placerTuileFromDeck(int index) throws IOException {
        Tuile tuile = this.game.getDeckTuiles().getTuile(index);
        if (tuile != null) {
            refreshDeck();
            controller.handleTilePlacement(tuile);
        } else {
            JOptionPane.showMessageDialog(null, "No tiles in hand!");
        }
    }


    private JButton createButtonRotation(TuilePanel tuilePanel) {
        JButton bouton = new JButton("Rotate");
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT); // Aligner le bouton au centre horizontalement
        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.rotateTile(tuilePanel.getTuile());
            }
        });
        return bouton;
    }



    // PART4 : DRAWING
    private void drawTile(Graphics g, int x, int y) {
        // Example method to draw a tile at a specified position and size
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, CELL_SIZE, CELL_SIZE); // Fill rectangle representing the tile
        g.setColor(Color.BLACK);
        g.drawRect(x, y, CELL_SIZE, CELL_SIZE); // Draw outline of the tile
        // Draw the tile's image
        dessinateurDeTuile.dessinerTuile(g, game.getTuile((y-TOP_MARGIN)/CELL_SIZE, (x-LEFT_MARGIN)/CELL_SIZE), dessinateurDeTuile.getSpritesSet(),x,y);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(LEFT_MARGIN, TOP_MARGIN, BOARD_SIZE, BOARD_SIZE); // Fill the board area with a rectangle
        g.setColor(Color.BLACK);
        g.drawRect(LEFT_MARGIN, TOP_MARGIN, BOARD_SIZE, BOARD_SIZE);



        // Draw tiles on the board
        for (int i = 0; i < BOARD_SIZE/CELL_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE/CELL_SIZE; j++) {
                if (game.getTuile(i, j) != null ) {
                    drawTile(g, LEFT_MARGIN+ j*CELL_SIZE, TOP_MARGIN+i*CELL_SIZE);
                }
                else {
                    g.setColor(Color.BLACK);
                    g.drawRect(LEFT_MARGIN+j*CELL_SIZE, TOP_MARGIN+i*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        int decalage=0;
        for (Joueur joueur : game.getJoueurs()) {
            drawPlayer(g, joueur);

            //on affiche le compteur pour chaque joueur
            int x = 20; //coordonée x pour l'emplacement du compteur
            int y = 75 + decalage; // coordonnée y pour l'emplacement du compteur
            int s = 20;
            int S = 27;
            if (joueur == game.getJoueurs().get(game.getCurrentPlayerIndex())){
                g.fillOval(x-3, y-3, S, S);
            }
            else {
                g.fillOval(x, y, s, s);
            }
            decalage += 30; //espace entre les compteur de chaque joueur de 30
        }
    }

    public void drawPlayer (Graphics g , Joueur j ){
        JoueurPanel joueurUI = new JoueurPanel(j);
        joueurUI.paintComponent(g);
    }


    // PART5 : UPDATE-Observer Pattern
    @Override
    public void update(ReadOnlyGame game) {
        this.game = game;
        try {
            refreshDeck(); // Refresh the deck of tiles car les tuiles ont changé
            //TODO : Revoir la logique de rafraichissement du deck , je ne pense pas que ca doit être la
            //TODO : Plutôt dans la vue et puis on notifie le sidePanel que le deck a changé
        } catch (IOException e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    @Override
    public void playerLost(String joueur) {
        JOptionPane.showMessageDialog(this, joueur + " a perdu ! ");
    }

    @Override
    public void playerWon(String joueur) {
        JOptionPane.showMessageDialog(this, "Félicitations " + joueur + " ! Vous avez remporté la partie !");
    }

    // pour ne pas avoir d'erreur d'exécution
    @Override
    public void update(Observable o, Object arg) {
    }


    /*
    public void afficherClassementFinal() {
        Collections.sort(joueurs, new Comparator<Joueur>() {
            @Override
            public int compare(Joueur joueur1, Joueur joueur2) {
                return joueur1.getNombreTuilesPlacees() - joueur2.getNombreTuilesPlacees();
            }
        });

        // Construction du message de classement
        StringBuilder message = new StringBuilder("Classement final :\n");
        for (int i = 0; i < joueurs.size(); i++) {
            Joueur joueur = joueurs.get(i);
            message.append(i + 1).append(". ").append(joueur.getPrenom()).append(" - ").append(joueur.getNombreTuilesPlacees()).append(" tuiles placées\n");
        }

        // Affichage du classement final dans une boîte de dialogue
        JOptionPane.showMessageDialog(this, message.toString(), "Classement Final", JOptionPane.INFORMATION_MESSAGE);
    }

    */

}




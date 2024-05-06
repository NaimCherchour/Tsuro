package main.java.vue;

import main.java.Main;

import javax.sound.sampled.*;
import java.io.File;

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

import main.java.menu.*;

import main.java.*;

/**
 * Classe représentant l'interface graphique du plateau de jeu.
 * Contient 5 parties : le constructeur, l'esthétique, la gestion des
 * événements, le dessin et la mise à jour.
 */
public class GameBoardUI extends JPanel implements GameObserver {
    private ReadOnlyGame game; // Quelques éléments visibles du model
    private Game games;

    private static int secondsElapsed = 0;
    private Timer timer;

    // TODO : Vérifier la relation entre le controller et la vue ; Normalement la
    // vue n'a pas de référence vers le controller
    DessinateurDeTuile dessinateurDeTuile;
    private static final int CELL_SIZE = 120;
    private static final int LEFT_MARGIN = 200;
    private static final int TOP_MARGIN = 50;
    private static final int BOARD_SIZE = 720;
    private static final int NUMBER_DECK_TILES = 3;
    private JPanel sidePanel;
    private JPanel filtre; // esthétique
    private boolean VisuActif = false; // esthétique
    private Tuile VisuSelect; // esthétique

    /**
     * Gère les clics sur les boutons dans le menu du jeu.
     * 
     * @param frame       La fenêtre principale dans laquelle les éléments du jeu
     *                    sont chargés.
     * @param cursorFrame Une instance de AnimatedCursorFrame contenant les curseurs
     *                    personnalisés.
     */

    // PART1 : CONSTRUCTOR
    public GameBoardUI(JFrame frame,Game game) throws IOException {
        // public GameBoardUI(JFrame frame, AnimatedCursorFrame cursorFrame) throws
        // IOException {
            this.games=game;

            // Dans la partie constructor de la classe GameBoardUI
    
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++; // Incrémenter le compteur de temps écoulé à chaque tick           
                int timeRemaining = 20 - secondsElapsed; // Calculer le temps restant
                repaint(); // Redessiner l'interface pour mettre à jour l'affichage du temps
                                // Changer la couleur du texte en rouge si le temps restant est inférieur ou égal à 5 secondes
                                if (timeRemaining <= 5) {
                                    setForeground(Color.RED);
                                } else {
                                    setForeground(Color.BLACK); // Revenir à la couleur de texte par défaut
                                }
                
                                // Afficher le temps restant dans le terminal
                                System.out.println("Temps restant: " + timeRemaining + " secondes");
                
                                // Vérifier si le temps restant est écoulé
                                if (timeRemaining <= 0) {
                                    // Passer au joueur suivant en appelant la méthode passerAuJoueurSuivant() de Game
                                    games.passerAuJoueurSuivant();
                                    // Actualiser le GameBoard avec la couleur du joueur suivant
                                    repaint();
                                    // Réinitialiser le compteur de temps écoulé
                                    secondsElapsed = 0;
                                } else {
                                    secondsElapsed++; // Incrémenter le compteur de temps écoulé à chaque tick
                                }
                
            }
        });
        timer.start(); // Démarrer le timer

        this.dessinateurDeTuile = new DessinateurDeTuile();
        this.filtre = initFiltre();
        addCellPanels(); // TODO : Revoir la modularité de l'esthétique du filtre et des cellules

        // Créer un nouveau JPanel latéral pour le deck
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(200, 600)); // Définir une largeur fixe et ajuster la hauteur
                                                             // automatiquement
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS)); // Utiliser BoxLayout pour aligner les
                                                                         // composants verticalement
        sidePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // Ajouter une marge autour du panneau

        filtre.setOpaque(true);
        sidePanel.setOpaque(true);
        sidePanel.add(filtre);
        sidePanel.setBackground(new Color(0, 0, 0, 0));
        // Ajout du sidePanel à la disposition de GameBoardUI
        setLayout(new BorderLayout());
        add(sidePanel, BorderLayout.EAST); // Ajouter le sidePanel à l'est de GameBoardUI

        /*
         * Cursor hoverCursor = cursorFrame.getHoverCursor(); // Curseur lors du survol
         * d'un bouton.
         * Cursor defaultCursor = cursorFrame.getDefaultCursor(); // Curseur par défaut.
         * // Création du bouton de retour avec changement d'image au survol et lors du
         * clic.
         * JButton backButton = createButton("src/main/resources/returnButton.png", 75,
         * hoverCursor, defaultCursor, frame,
         * "src/main/resources/returnButtonHovered.png",
         * "src/main/resources/returnButtonPressed.png");
         * backButton.addActionListener(e -> {
         * //playSound("src/main/resources/sound/buttonClickSound.wav");
         * MainMenu.createAndShowGUI(frame); // Assurer que le menu principal gère
         * également correctement le curseur.
         * });
         * // Ajout du bouton de retour à un panneau en haut de la fenêtre.
         * JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
         * topPanel.setOpaque(false);
         * topPanel.add(backButton);
         * add(topPanel, BorderLayout.NORTH); // Ajouter le panneau en haut de
         * GameBoardUI
         * }
         * 
         */

        /*
         * /**
         * Crée un bouton avec des images personnalisées pour les états normal, survolé
         * et pressé.
         * 
         * @param imagePath Chemin de l'image normale.
         * 
         * @param width Largeur du bouton.
         * 
         * @param hoverCursor Curseur lors du survol.
         * 
         * @param defaultCursor Curseur par défaut.
         * 
         * @param frame Fenêtre contenant le bouton.
         * 
         * @param hoverImagePath Chemin de l'image lors du survol.
         * 
         * @param pressedImagePath Chemin de l'image lors du clic.
         * 
         * @return Un JButton configuré avec les images et les curseurs spécifiés.
         * 
         * public static JButton createButton(String imagePath, int width, Cursor
         * hoverCursor, Cursor defaultCursor, JFrame frame, String hoverImagePath,
         * String pressedImagePath) {
         * ImageIcon normalIcon = new ImageIcon(imagePath);
         * ImageIcon hoverIcon = new ImageIcon(hoverImagePath);
         * ImageIcon pressedIcon = new ImageIcon(pressedImagePath);
         * double aspectRatio = (double) normalIcon.getIconWidth() / (double)
         * normalIcon.getIconHeight();
         * int height = (int) (width / aspectRatio);
         * Image image = normalIcon.getImage().getScaledInstance(width, height,
         * Image.SCALE_SMOOTH);
         * JButton button = new JButton(new ImageIcon(image));
         * button.setRolloverIcon(new
         * ImageIcon(hoverIcon.getImage().getScaledInstance(width, height,
         * Image.SCALE_SMOOTH)));
         * button.setPressedIcon(new
         * ImageIcon(pressedIcon.getImage().getScaledInstance(width, height,
         * Image.SCALE_SMOOTH)));
         * 
         * button.addMouseListener(new java.awt.event.MouseAdapter() {
         * 
         * @Override
         * public void mouseEntered(java.awt.event.MouseEvent evt) {
         * frame.getContentPane().setCursor(hoverCursor); // Change le curseur pour tout
         * le contenu de la fenêtre lors du survol.
         * }
         * 
         * @Override
         * public void mouseExited(java.awt.event.MouseEvent evt) {
         * frame.getContentPane().setCursor(defaultCursor); // Réinitialise le curseur
         * pour tout le contenu de la fenêtre après le survol.
         * }
         * });
         * 
         * 
         * 
         * button.setOpaque(false);
         * button.setContentAreaFilled(false);
         * button.setBorderPainted(false);
         * button.setFocusPainted(false);
         * return button;
         */
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
                // TODO : Revoir cette logique , je pense ca ne devrait pas être ici plutot dans
                // le controller mais comment faire
                // TODO : pour que le controller sache quel tuile a été cliqué
                TuilePanel tuilePanel = new TuilePanel(tuile) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g;
                        if (game.getGameState()) {
                            g2d.setColor(
                                    convertirCouleur(game.getJoueurs().get(game.getCurrentPlayerIndex()).getCouleur()));
                        }
                        g2d.setStroke(new BasicStroke(3)); // Définir l'épaisseur de la bordure
                        g2d.drawRoundRect(0, 0, 120 - 1, 120 - 1, 10, 10); // Dessiner une bordure rouge autour de la
                                                                           // tuile
                    }
                };
                tuilePanel.setOpaque(false);
                Joueur tmp = game.getJoueurs().get(game.getCurrentPlayerIndex());
                int ligne = tmp.getLigne();
                int col = tmp.getColonne();
                tuilePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        filtre.setBounds(0, 0, 120, 120);
                        filtre.setOpaque(false);
                        tuilePanel.add(filtre);
                        tuilePanel.setBackground(new Color(0, 0, 0, 0));
                        VisuActif = true;
                        VisuSelect = tuile;
                        repaint();
                        System.out.println("tuile visu bien visible ");
                    }

                    public void mouseClicked(MouseEvent e) {
                        VisuActif = false;
                    }

                    public void mouseExited(MouseEvent e) {
                        tuilePanel.remove(filtre);
                        tuilePanel.setBackground(null);
                        VisuActif = false;

                        repaint();
                        System.out.println("tuile visu bien invisible ");
                    }
                });
                tuilePanel.addMouseListener(this.getMouseListeners()[0]);
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

    private JButton createButtonRotation(TuilePanel tuilePanel) {
        JButton bouton = new JButton("Rotate");
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT); // Aligner le bouton au centre horizontalement
        bouton.putClientProperty("tuilePanel", tuilePanel);
        // the controller is the action listener to rotate
        bouton.addActionListener((ActionListener) this.getMouseListeners()[0]);
        return bouton;
    }

    // PART4 : DRAWING

    private void drawTileVisu(Graphics g, int x, int y, Tuile X) {
        // Example method to draw a tile at a specified position and size

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(0, 0, 0, 80));
        int arcWidth = 10; // Adjust the roundness of the corners as needed
        int arcHeight = 10; // Adjust the roundness of the corners as needed
        g2d.fillRoundRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, arcWidth, arcHeight);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, arcWidth, arcHeight); // Draw outline of the tile
                                                                                            // with rounded corners
        // Draw the tile's image
        dessinateurDeTuile.dessinerTuile(g, X, dessinateurDeTuile.getSpritesSet(), x, y);
    }

    private void drawTile(Graphics g, int x, int y) {
        // Example method to draw a tile at a specified position and size
        g.setColor(new Color(255, 240, 230));
        g.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, 13, 13); // Fill rectangle representing the tile
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, CELL_SIZE, CELL_SIZE, 13, 13); // Draw outline of the tile
        // Draw the tile's image
        dessinateurDeTuile.dessinerTuile(g, game.getTuile((y - TOP_MARGIN) / CELL_SIZE, (x - LEFT_MARGIN) / CELL_SIZE),
                dessinateurDeTuile.getSpritesSet(), x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background color in gradient
        Color startColor = new Color(85, 171, 85); // Pine green
        Color endColor = new Color(0, 158, 255); // Sky blue
        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g.fillRoundRect(LEFT_MARGIN, TOP_MARGIN, BOARD_SIZE, BOARD_SIZE, 10, 10); // Fill the board area with a
                                                                                  // rectangle
        g.setColor(Color.BLACK);
        g.drawRoundRect(LEFT_MARGIN, TOP_MARGIN, BOARD_SIZE, BOARD_SIZE, 10, 10);

        if (VisuActif) {
            Joueur tmp = game.getJoueurs().get(game.getCurrentPlayerIndex());
            int ligne = tmp.getLigne();
            int col = tmp.getColonne();
            drawTileVisu(g, LEFT_MARGIN + col * CELL_SIZE, TOP_MARGIN + ligne * CELL_SIZE, VisuSelect);
        }

        // Draw tiles on the board
        for (int i = 0; i < BOARD_SIZE / CELL_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE / CELL_SIZE; j++) {
                if (game.getTuile(i, j) != null) {
                    drawTile(g, LEFT_MARGIN + j * CELL_SIZE, TOP_MARGIN + i * CELL_SIZE);
                } else {
                    g.setColor(Color.BLACK);
                    g.drawRoundRect(LEFT_MARGIN + j * CELL_SIZE, TOP_MARGIN + i * CELL_SIZE, CELL_SIZE, CELL_SIZE, 10,
                            10);
                }
            }
        }
        int espace = 75;
        for (Joueur joueur : game.getJoueurs()) {
            drawPlayer(g, joueur);
            g.setColor(convertirCouleur(joueur.getCouleur())); // Couleur du texte
            espace = espace + 30;
            if (joueur == game.getJoueurs().get(game.getCurrentPlayerIndex())) {
                g.fillOval(17, espace - 4, 28, 28);
            } else {
                g.fillOval(20, espace, 20, 20);
            }
        }
        if (game.getGameState()) {
            Joueur joueurActuel = game.getJoueurs().get(game.getCurrentPlayerIndex());
            String tourDuJoueur = "Tour du joueur " + joueurActuel.getCouleur().toString();
            Font font = new Font("Arial", Font.BOLD, 16);
            g.setFont(font);
            g.setColor(convertirCouleur(game.getJoueurs().get(game.getCurrentPlayerIndex()).getCouleur())); // Couleur
                                                                                                            // du texte
            g.drawString(tourDuJoueur, 10, 410); // Position du texte
        }

        Font font = new Font("Arial", Font.PLAIN, 14);
        g.setFont(font);
        
        // Calculer le temps restant
        int timeRemaining = 20 - secondsElapsed;
        
        // Changer la couleur du texte en rouge si le temps restant est inférieur ou égal à 5 secondes
        if (timeRemaining <= 5) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK); // Revenir à la couleur de texte par défaut
        }
        
        int x = getWidth() - 200; // Position en X pour afficher le chronomètre
        int y = 20; // Position en Y pour afficher le chronomètre
        g.drawString("Temps restant: " + timeRemaining + " secondes", x, y); // Afficher le temps restant
        
    }

    public void drawPlayer(Graphics g, Joueur j) {
        JoueurPanel joueurUI = new JoueurPanel(j);
        joueurUI.paintComponent(g);
    }

    private Color convertirCouleur(Joueur.Couleur couleur) {
        return switch (couleur) {
            case NOIR -> Color.BLACK;
            case ROUGE -> Color.RED;
            case BLEU -> Color.BLUE;
            case VERT -> Color.GREEN;
            case JAUNE -> Color.YELLOW;
            case ORANGE -> Color.ORANGE;
            case ROSE -> new Color(220, 12, 253); // rose
            case CYAN -> Color.CYAN;
            case VIOLET -> new Color(128, 0, 128); // Violet
        };
    }

    // PART5 : UPDATE-Observer Pattern
    @Override
    public void update(ReadOnlyGame game) {
        this.game = game;
        try {
            refreshDeck(); // Refresh the deck of tiles car les tuiles ont changé
            // TODO : Revoir la logique de rafraichissement du deck , je ne pense pas que ca
            // doit être la
            // TODO : Plutôt dans la vue et puis on notifie le sidePanel que le deck a
            // changé
        } catch (IOException e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    public static void resetSecondsElapsed() {
        secondsElapsed = 0;
    }

    @Override
    public void playerLost(String playerName) {
        // Créer une icône pour afficher avec le message
        ImageIcon icon = new ImageIcon("path/to/your/icon.png");
    
        // Définir le message de joueur perdant
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #FF5733;'>Oh non !</h1>"
                + "<p>Dommage, " + playerName + " a perdu la partie.</p>"
                + "<p>Mais ne vous inquiétez pas, il y aura d'autres occasions de gagner !</p>"
                + "</div></html>";
    
        // Afficher le message avec une boîte de dialogue personnalisée
        JOptionPane.showMessageDialog(this, message, "Joueur Éliminé", JOptionPane.INFORMATION_MESSAGE, icon);
    }
    
    @Override
    public void playerWon(String playerName) {
        // Créer une icône pour afficher avec le message
        ImageIcon icon = new ImageIcon("path/to/your/icon.png");
    
        // Définir le message de joueur gagnant
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #32CD32;'>Félicitations " + playerName + " !</h1>"
                + "<p>Vous avez remporté la partie avec succès !</p>"
                + "<p>Continuez ainsi et profitez de votre victoire !</p>"
                + "</div></html>";
    
        // Afficher le message avec une boîte de dialogue personnalisée
        JOptionPane.showMessageDialog(this, message, "Joueur Gagnant", JOptionPane.INFORMATION_MESSAGE, icon);
    }
    
    @Override
    public void gameWinnersTie() {
        // Créer une icône pour afficher avec le message
        ImageIcon icon = new ImageIcon("path/to/your/icon.png");
    
        // Définir le message d'égalité
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #FFD700;'>Égalité !</h1>"
                + "<p>La partie s'est terminée sur une égalité.</p>"
                + "<p>Vous êtes tous de formidables compétiteurs !</p>"
                + "</div></html>";
    
        // Afficher le message avec une boîte de dialogue personnalisée
        JOptionPane.showMessageDialog(this, message, "Égalité", JOptionPane.INFORMATION_MESSAGE, icon);
    }
    

    @Override
    public void gameFinish() {
        // Créer une icône pour afficher avec le message
        ImageIcon icon = new ImageIcon("path/to/your/icon.png");
    
        // Définir le message de fin de jeu
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #FF5733;'>La partie est terminée !</h1>"
                + "<p>Merci d'avoir joué.</p>"
                + "<p>Aurevoir et à bientôt !</p>"
                + "</div></html>";
    
        // Afficher le message avec une boîte de dialogue personnalisée
        JOptionPane.showMessageDialog(this, message, "Fin de la partie", JOptionPane.INFORMATION_MESSAGE, icon);
    }
    

    // pour ne pas avoir d'erreur d'exécution
    @Override
    public void update(Observable o, Object arg) {
    }

    /*
     * public void afficherClassementFinal() {
     * Collections.sort(joueurs, new Comparator<Joueur>() {
     * 
     * @Override
     * public int compare(Joueur joueur1, Joueur joueur2) {
     * return joueur1.getNombreTuilesPlacees() - joueur2.getNombreTuilesPlacees();
     * }
     * });
     * 
     * // Construction du message de classement
     * StringBuilder message = new StringBuilder("Classement final :\n");
     * for (int i = 0; i < joueurs.size(); i++) {
     * Joueur joueur = joueurs.get(i);
     * message.append(i +
     * 1).append(". ").append(joueur.getPrenom()).append(" - ").append(joueur.
     * getNombreTuilesPlacees()).append(" tuiles placées\n");
     * }
     * 
     * // Affichage du classement final dans une boîte de dialogue
     * JOptionPane.showMessageDialog(this, message.toString(), "Classement Final",
     * JOptionPane.INFORMATION_MESSAGE);
     * }
     * 
     */

}

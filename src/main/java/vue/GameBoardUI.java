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
import main.java.controller.Action;
import main.java.menu.*;


/**
 * Classe représentant l'interface graphique du plateau de jeu.
 * Contient 5 parties : le constructeur, l'esthétique, la gestion des
 * événements, le dessin et la mise à jour.
 */
public class GameBoardUI extends JPanel implements GameObserver {
    private ReadOnlyGame game; // Quelques éléments visibles du model

    private String username;
    private static int secondsElapsed = 0;
    private Timer timer;
    private Image backgroundImage;
    private Image fondGameboard;

    // TODO : Vérifier la relation entre le controller et la vue ; Normalement la
    // vue n'a pas de référence vers le controller
    DessinateurDeTuile dessinateurDeTuile;
    private static final int CELL_SIZE = 120;
    private static final int LEFT_MARGIN = 200;
    private static final int TOP_MARGIN = 50;
    private static final int BOARD_SIZE = 720;
    private static final int NUMBER_DECK_TILES = 3;
    private JButton saveButton ;
    private Boolean loadedGame ;
    private int indexGame; // for loaded game
    private JPanel sidePanel;
    private JPanel northPanel;
    private JPanel filtre; // esthétique
    private boolean VisuActif = false; // esthétique
    private Tuile VisuSelect; // esthétique

    /**
     * Gère les clics sur les boutons dans le menu du jeu.
     * 
     * cursorFrame Une instance de AnimatedCursorFrame contenant les curseurs
     * personnalisés.
     */

    // PART1 : CONSTRUCTOR
    public GameBoardUI(String username, JFrame frame , boolean loadedGame) throws IOException {
        this.username = username ;
        this.loadedGame = loadedGame;
        // Charger l'image de fond
        backgroundImage = new ImageIcon("src/main/resources/fondPlateau.png").getImage();
        fondGameboard = new ImageIcon("src/main/resources/fondGameBoard.png").getImage();
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

        northPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(100, 50));

        JButton returnButton = Option.createReturnButton(); // static method in Option in Menu
        returnButton.setSize(new Dimension(100,50));
        returnButton.setPreferredSize(new Dimension(100,50));
        returnButton.setBackground(new Color(0, 0, 0, 0));
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Option.playSound();
                if ( !loadedGame ){
                    Jouer.gererClicSurBoutonJouer(frame, null, username);  // Assurer que le menu principal gère également correctement le curseur.
                } else {
                    ProfilePage retAuProfile = new ProfilePage(frame,username);
                }
            }
        });
        createSaveButton(username);

        northPanel.setBackground(new Color(0, 0, 0, 0));
        northPanel.add(returnButton);
        northPanel.add(saveButton);

        // Ajout du sidePanel à la disposition de GameBoardUI
        setLayout(new BorderLayout());
        add(sidePanel, BorderLayout.EAST); // Ajouter le sidePanel à l'est de GameBoardUI
        add(northPanel, BorderLayout.WEST);
    }

    public GameBoardUI (String username, JFrame frame , boolean loadedGame, int indexGame) throws IOException {
        this(username,frame,loadedGame);
        this.indexGame = indexGame;
        northPanel.remove(saveButton);
        createSaveButton(username);
        northPanel.add(saveButton,BorderLayout.WEST);
    }

    private void createSaveButton(String username) {
        saveButton = createCustomButton("Sauvegarder",new Color(70, 130, 180),Color.BLACK,new Font("Arial", Font.BOLD, 16));
        saveButton.setSize(new Dimension(150,30));
        saveButton.setPreferredSize(new Dimension(150,30));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.putClientProperty("Save", username);
        if (loadedGame){
            saveButton.putClientProperty("IndexSavedGame",indexGame);
            System.out.println("Game Board "+indexGame);
            saveButton.setActionCommand(Action.SAVE_LOADED_GAME.getAction()); //pour ne pas repeter la sauvegarde
        } else {
            saveButton.setActionCommand(Action.SAVE.getAction()); // Set action command
        }
    }

    static JButton createCustomButton(String text, Color background, Color foreground, Font font) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
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
                            g2d.setColor(convertirCouleur(game.getJoueurs().get(game.getCurrentPlayerIndex()).getCouleur()));
                        }
                        g2d.setStroke(new BasicStroke(8)); // Définir l'épaisseur de la bordure
                        g2d.drawRoundRect(0, 0, 120 - 1, 120 - 1, 10, 10); // Dessiner une bordure rouge autour de la
                                                                           // tuile
                    }
                };
                tuilePanel.setOpaque(false);
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

        // Définir la taille personnalisée du bouton
        bouton.setPreferredSize(new Dimension(200, 50)); // Largeur : 200 pixels, Hauteur : 50 pixels
        bouton.setBackground(new Color(245, 245, 220)); // Beige

        bouton.putClientProperty("tuilePanel", tuilePanel);
        bouton.setActionCommand(Action.ROTATE.getAction()); // Set action command
        // the controller is the action listener to rotate
        bouton.addActionListener((ActionListener) this.getMouseListeners()[0]);
        return bouton;
    }

    // PART4 : DRAWING

    private void drawTileVisu(Graphics g, int x, int y, Tuile X) {
        // Example method to draw a tile at a specified position and size

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(22, 52, 80));
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
        g.setColor(new Color(52, 82, 110));
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Dessiner l'image de fond du plateau de jeu
        if (fondGameboard != null) {
            g.drawImage(fondGameboard, 0, 0, getWidth(), getHeight(), this);
        }

        // Dessiner le rectangle du plateau
        g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g.fillRoundRect(LEFT_MARGIN, TOP_MARGIN, BOARD_SIZE, BOARD_SIZE, 10, 10);
        g.drawRoundRect(LEFT_MARGIN, TOP_MARGIN, BOARD_SIZE, BOARD_SIZE, 10, 10);
        // Dessiner l'image de fond
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, LEFT_MARGIN, TOP_MARGIN, BOARD_SIZE, BOARD_SIZE, this);
        }

        // Dessiner la tuile de visualisation si active
        if (VisuActif) {
            if (game.getGameState()) {
                Joueur tmp = game.getJoueurs().get(game.getCurrentPlayerIndex());
                int ligne = tmp.getLigne();
                int col = tmp.getColonne();
                drawTileVisu(g, LEFT_MARGIN + col * CELL_SIZE, TOP_MARGIN + ligne * CELL_SIZE, VisuSelect);
            }
        }

        // Dessiner les tuiles sur le plateau
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

        // Dessiner les joueurs
        int espace = 75;
        for (Joueur joueur : game.getJoueurs()) {
            drawPlayer(g, joueur);
            g.setColor(convertirCouleur(joueur.getCouleur()));
            espace += 30;
            if (joueur == game.getJoueurs().get(game.getCurrentPlayerIndex())) {
                g.fillOval(30, espace - 4, 28, 28);
            } else {
                g.fillOval(20, espace, 20, 20);
            }
        }

        // Dessiner l'information du tour du joueur
        if (game.getGameState()) {
            Joueur joueurActuel = game.getJoueurs().get(game.getCurrentPlayerIndex());
            String tourDuJoueur = "Tour du joueur " + joueurActuel.getCouleur().toString();
            Font font = new Font("Arial", Font.BOLD, 16);
            g.setFont(font);
            g.setColor(convertirCouleur(game.getJoueurs().get(game.getCurrentPlayerIndex()).getCouleur()));
            g.drawString(tourDuJoueur, 480, 27);
        }

        if(game.getGameModeInt() == 2){
            // Dessiner le chronomètre
            Font font = new Font("Arial", Font.BOLD, 16);
            g.setFont(font);
            int timeRemaining = 20 - secondsElapsed;
            if (timeRemaining <= 5) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLACK);
            }
            int x = getWidth() - 260;
            int y = 20;
            g.drawString("Temps restant: " + timeRemaining + " secondes", x, y);
        }

        if ( !game.getGameState()){
            saveButton.setVisible(false);
        }
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
        saveButton.addActionListener((ActionListener) this.getMouseListeners()[0]);
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


    @Override
    public void playerLost(String playerName) {

        // Définir le message de joueur perdant
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #FF5733;'>Oh non !</h1>"
                + "<p>Dommage, " + playerName + " a perdu la partie.</p>"
                + "<p>Mais ne vous inquiétez pas, il y aura d'autres occasions de gagner !</p>"
                + "</div></html>";

        // Afficher le message avec une boîte de dialogue personnalisée
        JOptionPane.showMessageDialog(this, message, "Joueur Éliminé", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void playerWon(String playerName) {

        // Définir le message de joueur gagnant
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #32CD32;'>Félicitations " + playerName + " !</h1>"
                + "<p>Vous avez remporté la partie avec succès !</p>"
                + "<p>Continuez ainsi et profitez de votre victoire !</p>"
                + "</div></html>";

        // Afficher le message avec une boîte de dialogue personnalisée
        JOptionPane.showMessageDialog(this, message, "Joueur Gagnant", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void gameWinnersTie() {

        // Définir le message d'égalité
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #FFD700;'>Égalité !</h1>"
                + "<p>La partie s'est terminée sur une égalité.</p>"
                + "<p>Vous êtes tous de formidables compétiteurs !</p>"
                + "</div></html>";

        // Afficher le message avec une boîte de dialogue personnalisée
        JOptionPane.showMessageDialog(this, message, "Égalité", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void gameFinish() {

        if ( game.getGameModeInt() == 2) {
            timer.stop();
        }
        // Définir le message de fin de jeu
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #FF5733;'>La partie est terminée !</h1>"
                + "<p>Merci d'avoir joué.</p>"
                + "<p>Aurevoir et à bientôt !</p>"
                + "</div></html>";

        // Afficher le message avec une boîte de dialogue personnalisée
        JOptionPane.showMessageDialog(this, message, "Fin de la partie", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void alertSavedGame () {
        saveButton.setActionCommand("Nothing");
        JOptionPane.showMessageDialog(GameBoardUI.this, "La partie a été sauvegardée avec succès !");
        northPanel.remove(saveButton);
        indexGame = game.getNumberOfSavedGames(username)-1;
        saveButton.setVisible(false);
        loadedGame = true ;
        createSaveButton(username);
        northPanel.add(saveButton);
    }

    @Override
    public void alertUpdateSavedGame(){
        saveButton.setActionCommand("Nothing");
        JOptionPane.showMessageDialog(GameBoardUI.this, "La partie a été mise à jour avec succès !");
        northPanel.remove(saveButton);
        loadedGame = true ;
        createSaveButton(username);
        northPanel.add(saveButton);
    }


    @Override
    public void startTurnTimer() {
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
                    // Réinitialiser le compteur de temps écoulé
                    secondsElapsed = 0;
                        // on passe par le controller pour placer une tuile automatique
                    play();


                }
    
                // Redessiner l'interface pour mettre à jour l'affichage du temps
                repaint(); 
    
                // Changer la couleur du texte en rouge si le temps restant est inférieur ou égal à 5 secondes
                if (timeRemaining <= 5) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK); // Revenir à la couleur de texte par défaut
                }
            }
        });
        timer.start(); // Démarrer le timer
    }

    private void play(){
        ((Controller) this.getMouseListeners()[0]).handleTilePlacement(null) ;
    }

    @Override
    public void resetTimer(){
        secondsElapsed=0;
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

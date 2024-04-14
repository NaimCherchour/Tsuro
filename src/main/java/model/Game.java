package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

public class Game extends Observable implements ReadOnlyGame {
    private PlateauTuiles plateau; // Le plateau de jeu
    private List<Joueur> joueurs; // La liste des joueurs
    private static int NB_JOUEURS; // Le nombre de joueurs dans la partie
    private int currentPlayerIndex; // L'indice du joueur courant
    private final DeckTuiles deckTuiles; // Le deck de tuiles
    private List<GameObserver> observers; // Les observateurs de la partie


    public DeckTuiles getDeckTuiles() {
        return deckTuiles;
    }
    public PlateauTuiles getPlateau() {
        return plateau;
    }

    @Override
    public Tuile getTuile(int i, int j) {
        return plateau.getTuile(i, j);
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }
    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }


    public Game(int taillePlateau) {
        this.plateau = new PlateauTuiles(taillePlateau);
        this.deckTuiles = new DeckTuiles();
        this.observers = new ArrayList<>();
        initializeGame();
    }
    private void initializeGame() {
        Object[] options = {"1v1 against Bot", "Multiplayer (2-8 players)"};
        int response = JOptionPane.showOptionDialog(null, "Choose the game mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (response == 0) {
            initializePlayers(2, true);
        } else {
            int numberOfPlayers = getNumberOfPlayersFromUser();
            initializePlayers(numberOfPlayers, false);
        }
    }

    private int getNumberOfPlayersFromUser() {
        while (true) {
            String input = JOptionPane.showInputDialog("Enter the number of players (2-8):");
            try {
                int numberOfPlayers = Integer.parseInt(input);
                if (numberOfPlayers >= 2 && numberOfPlayers <= 8) {
                    return numberOfPlayers;
                } else {
                    JOptionPane.showMessageDialog(null, "Enter a number between 2 and 8.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Enter a valid number.");
            }
        }
    }


    private void initializePlayers(int numberOfPlayers, boolean vsBot) {
        joueurs = new ArrayList<>();
        if (vsBot) {
            joueurs.add(new Joueur("Human Player", joueurs));
            joueurs.add(new BotTsuro("Bot", joueurs));
            currentPlayerIndex = 0;
        } else {
            for (int i = 0; i < numberOfPlayers; i++) {
                joueurs.add(new Joueur("Player " + (i + 1), joueurs));
            }
            currentPlayerIndex = 0;
        }
    }


    public void jouerUnTour(Tuile tuile) {
        Joueur joueurCourant = joueurs.get(currentPlayerIndex);

        if (!plateau.placerTuile(tuile, joueurCourant, joueurs) || !joueurCourant.isAlive()) {
            // Le joueur courant perd son tour ou meurt
            joueurs.remove(joueurCourant);
            notifyObserversPlayerLost(joueurCourant.getPrenom());
            if (joueurs.size() == 1) {
                // S'il reste un seul joueur, il est le gagnant
                notifyObserversPlayerWon(joueurs.get(0).getPrenom());
                return;
            }
        } else {
            // Le mouvement est valide et le joueur est toujours en vie
            System.out.println("Position après mouvement: COL" + joueurCourant.getColonne() + " LIGN" + joueurCourant.getLigne() + " ENTR" + joueurCourant.getPointEntree());
        }

        // Passer au joueur suivant
        nextPlayer();

        // Vérifier si le prochain joueur est un bot et jouer son tour automatiquement
        if (joueurs.get(currentPlayerIndex) instanceof BotTsuro) {
            BotTsuro bot = (BotTsuro) joueurs.get(currentPlayerIndex);
            BotTsuro.Mouvement success = bot.choisirEtAppliquerMouvement(this); // Le bot choisit et joue son mouvement
            if (success == null) {
                System.out.println("Le bot ne peut pas jouer de mouvement valide.");
            }
            // Passer automatiquement au joueur humain suivant après que le bot ait joué
            nextPlayer();
        }
    }

    private void currentPlayerIndexAct(){
        if (currentPlayerIndex >= NB_JOUEURS ){
            currentPlayerIndex = 0;
        }
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % joueurs.size();
    }
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Méthode pour notifier les observateurs de la partie pour mettre à jour la vue avec les nouvelles données
     * mais en ReadOnly
     */
    @Override
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(this);
        }
    }

    /**
     * Méthode pour notifier les observateurs de la partie que le joueur a perdu
     * @param playerName
     */
    private void notifyObserversPlayerLost(String playerName) {
        for (GameObserver observer : observers) {
            observer.playerLost(playerName);
        }
    }

    /**
     * Méthode pour notifier les observateurs de la partie que le joueur a gagné
     * @param playerName
     */
    private void notifyObserversPlayerWon(String playerName) {
        for (GameObserver observer : observers) {
            observer.playerWon(playerName);
        }
    }



}
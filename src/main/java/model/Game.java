package main.java.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class Game extends Observable implements ReadOnlyGame {
    private PlateauTuiles plateau; // Le plateau de jeu
    private static final int TAILLE_PLATEAU = 6 ;
    private List<Joueur> joueurs; // La liste des joueurs
    private static int NB_JOUEURS; // Le nombre de joueurs dans la partie
    private int currentPlayerIndex; // L'indice du joueur courant
    private final DeckTuiles deckTuiles; // Le deck de tuiles
    private List<GameObserver> observers; // Les observateurs de la partie
    private boolean gameState ; // true for Playing and false for the end of the Game


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

    @Override
    public boolean getGameState() {
        return gameState;
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


    public Game(int type, int numberOfPlayers) {
        this.plateau = new PlateauTuiles(TAILLE_PLATEAU);
        this.deckTuiles = new DeckTuiles();
        this.observers = new ArrayList<>();
        initializeGame(type,numberOfPlayers);
        gameState = true ;
    }

    private void initializeGame(int type,int numberOfPlayers) {
        if (type == 0) {
            initializePlayers(2, true);
        } else {
            initializePlayers(numberOfPlayers, false);
        }
    }

    private void initializePlayers(int numberOfPlayers, boolean vsBot) {
        joueurs = new ArrayList<>();
        if (vsBot) {
            joueurs.add(new Joueur("Human Player", joueurs));
            joueurs.add(new BotTsuro("Bot", joueurs));
            NB_JOUEURS = 2 ;
            currentPlayerIndex = 0;
        } else {
            for (int i = 0; i < numberOfPlayers; i++) {
                joueurs.add(new Joueur("Player " + (i + 1), joueurs));
            }
            NB_JOUEURS = numberOfPlayers ;
            currentPlayerIndex = 0;
        }
    }



    public void jouerUnTour(Tuile tuile) {
        if (!joueurs.isEmpty()){
            verifierJoueursElimines();
            if (!joueurs.isEmpty()) {
                System.out.println("Nombre de Joueurs" + NB_JOUEURS);
                Joueur joueurCourant = joueurs.get(currentPlayerIndex);
                if (isCurrentPlayerBot()) { // Check if the current player is a bot
                    try {
                        playBotTurn((BotTsuro) joueurCourant); // Call the method to play the bot's turn
                        if (!joueurCourant.isAlive()) { // Check if the bot is still alive after its turn
                            joueurs.remove(joueurCourant); // Remove the bot from the player list
                            NB_JOUEURS--;
                            currentPlayerIndexAct();
                            notifyObservers();
                            notifyObserversPlayerLost(joueurCourant.getPrenom()); // Notify observers of bot's loss
                            verifierJoueursElimines();
                        } else {
                            nextPlayer(); // Move to the next player's turn
                            jouerUnTour(null); // pour le bot
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (tuile != null) { // Ensure the tile is not null (for human player's turn)
                        if (!plateau.placerTuile(tuile, joueurCourant, joueurs) || !joueurCourant.isAlive()) {
                            Joueur joueurPerdant = joueurs.get(currentPlayerIndex);
                            joueurs.remove(joueurPerdant);
                            NB_JOUEURS--;
                            currentPlayerIndexAct();
                            notifyObservers();
                            notifyObserversPlayerLost(joueurPerdant.getPrenom());
                            verifierJoueursElimines();
                            System.out.println("COL" + joueurCourant.getColonne() + "LIGN" + joueurCourant.getLigne() + "ENTR" + joueurCourant.getPointEntree());
                        } else {
                            System.out.println("COL" + joueurCourant.getColonne() + "LIGN" + joueurCourant.getLigne() + "ENTR" + joueurCourant.getPointEntree());
                            nextPlayer();
                            jouerUnTour(null);
                        }
                    }
                }
            }
        }
    }


    /**
     * -> Élimine les joueurs qui ont perdu à chaque placement de Tuile
     *    Si il y'a plus de joueurs on arrête le jeu ou on déclare égalité s'il y'a
     *    Si il reste 1 Joueur, on le déclare gagnant
     */
    private void verifierJoueursElimines() {
        Iterator<Joueur> iterator = joueurs.iterator();
        while (iterator.hasNext()) {
            Joueur joueur = iterator.next();
            if (!joueur.isAlive()) {
                iterator.remove();
                NB_JOUEURS--;
                currentPlayerIndexAct();
                if(joueurs.isEmpty()){ // pour éviter une erreur de length = 0 quand on appelle notifyObservers
                                       // ceci reste correct , juste inconvénient c'est la chronologie des actions
                    gameState = false ; }
                notifyObservers();
                notifyObserversPlayerLost(joueur.getPrenom());
            }
        }
        if(joueurs.isEmpty()){
            gameState = false ;
            if (plateau.isTie()) { //égalité
                notifyObserversWinnersTie();
                notifyGameEnd();
             }
        }
        if(joueurs.size() == 1 ){
                Joueur j = joueurs.get(0);
                System.out.println("REMOVING THE LAST PLAYER");
                notifyObserversPlayerWon(j.getPrenom());
                joueurs.remove(j);
                gameState = false ;
                notifyGameEnd();
        }
    }


    public void playBotTurn(BotTsuro bot) throws IOException {
        bot.choisirEtAppliquerMouvement(this);
    }

    public boolean isCurrentPlayerBot() {
        if (joueurs.isEmpty()) {
            return false;
        } else {
            Joueur currentPlayer = joueurs.get(currentPlayerIndex);
            return currentPlayer instanceof BotTsuro;
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

    /**
     * Méthode pour notifier que la partie est finie
     */
    private void notifyGameEnd (){
        for (GameObserver observer : observers) {
            observer.gameFinish();
        }
    }

    private void notifyObserversWinnersTie() {
        for (GameObserver observer : observers) {
            observer.gameWinnersTie();
        }
    }



}
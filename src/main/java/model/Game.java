package main.java.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

// import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;


import main.java.vue.GameBoardUI;

public class Game implements ReadOnlyGame,Serializable {

    private static final long serialVersionUID = 1L;
    private PlateauTuiles plateau; // Le plateau de jeu
    private static final int TAILLE_PLATEAU = 6 ;
    private List<Joueur> joueurs; // La liste des joueurs
    private static int NB_JOUEURS; // Le nombre de joueurs dans la partie
    private int currentPlayerIndex; // L'indice du joueur courant
    private final DeckTuiles deckTuiles; // Le deck de tuiles
    private transient List<GameObserver> observers; // Les observateurs de la partie
    private boolean gameState ; // true for Playing and false for the end of the Game
    private int type; // bot or normal
    
    
   

    public enum GameMode {
        CLASSIC,
        LONGEST_PATH,
        TIMER 
    }
    private GameMode gameMode; // La variante du jeu


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

    public void setObservers( List<GameObserver> obv){
        this.observers = obv ;
    }



    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    public GameMode getGameMode() {
        return gameMode;
    }


    public Game(int type, int numberOfPlayers,int variante) {
        this.plateau = new PlateauTuiles(TAILLE_PLATEAU);
        this.deckTuiles = new DeckTuiles();
        this.observers = new ArrayList<>();
        Joueur.setIndexCouleur(1); //à chaque game partie , l'index de couleurs est réinitialisé
        this.type = type ;
        initializeGame(type,numberOfPlayers);
        gameState = true ;
        if ( variante == 0 ){
            gameMode = GameMode.CLASSIC;
        } else if ( variante == 1) {
            gameMode = GameMode.LONGEST_PATH;
        } else if (variante == 2) {
            gameMode = GameMode.TIMER; // Nouveau mode de jeu
            notifyObserverTimerStart();       
         }
    }

    public void play(Tuile tuile) {
        switch (gameMode) {
            case CLASSIC:
                jouerUnTour(tuile);
                break;
            case LONGEST_PATH:
                //jouerUnTourLongestPath(tuile);
                break;
            case TIMER:
                jouerUnTourTimer(tuile); // Nouvelle méthode pour gérer le mode TIMER
                break;
            default:
                throw new IllegalStateException("Mode de jeu non géré: " + gameMode);
        }
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
            joueurs.add(new Joueur("Joueur Local", joueurs));
            joueurs.add(new BotTsuro("Bot", joueurs));
            NB_JOUEURS = 2 ;
            currentPlayerIndex = 0;
        } else {
            for (int i = 0; i < numberOfPlayers; i++) {
                joueurs.add(new Joueur("Joueur " + (i + 1), joueurs));
            }
            NB_JOUEURS = numberOfPlayers ;
            currentPlayerIndex = 0;
        }
    }


    // on l'appelle uniquement si le timer est <= à 0

    public void jouerUnTourTimer(Tuile tuile) {
        if ( tuile == null ) {
            if (!(this.getJoueurs().get(getCurrentPlayerIndex()) instanceof BotTsuro)) {
                Random rand = new Random();
                Tuile tuileAleatoire = this.getDeckTuiles().getTuile(rand.nextInt(3));
                jouerUnTour(tuileAleatoire);
            }
        }else {
            jouerUnTour(tuile);
        }
    }


    public Joueur getCurrentPlayer() {
        return joueurs.get(currentPlayerIndex);
    }

    public int getGameMode(){
        switch (gameMode) {
            case CLASSIC:
                return 0;
                break;
            case LONGEST_PATH:
                return 1;
                break;
            case TIMER:
                return 2;
                break;
            default:
                throw new IllegalStateException("Mode de jeu non géré: " + gameMode);
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
                        if (tuile.getId() == 19 || tuile.getId() == 27){
                            nextPlayer();
                            jouerUnTour(null);

                        }
                        else if (!plateau.placerTuile(tuile, joueurCourant, joueurs) || !joueurCourant.isAlive()) {
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
        if(joueurs.size()>0){
        currentPlayerIndex = (currentPlayerIndex + 1) % joueurs.size();
        }
    }
    public void passerAuJoueurSuivant() {
        nextPlayer();
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

    public void notifyObserverTimerStart(){
        for (GameObserver observer : observers){
            observer.startTurnTimer();
        }
    }

    // Méthode pour sauvegarder l'état du jeu
    public void sauvegarderEtatJeu(String profileName) {
        String savedGamePath = "src/main/resources/sauvegarde/" + profileName + "_" + getNumberOfSavedGames(profileName) + ".ser";
        try (FileOutputStream fileOut = new FileOutputStream(savedGamePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Serialize and save the game state
            objectOut.writeObject(this);

            // Update profile with the new saved game path
            ProfileManager profileManager = new ProfileManager();
            Profile userProfile = profileManager.getProfile(profileName);
            if (userProfile != null) {
                userProfile.addSavedGame(savedGamePath);
                profileManager.saveProfile(userProfile);
            }

            System.out.println("Game state saved successfully in: " + savedGamePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNumberOfSavedGames(String profileName) {
        ProfileManager profileManager = new ProfileManager();
        Profile userProfile = profileManager.getProfile(profileName);
        return userProfile != null ? userProfile.getSavedGames().size() : 0;
    }



    public static Game chargerEtatJeu(String fileName) {
        File file = new File(fileName);
        System.out.println("enter1");
        if (!file.exists()) {
            System.out.println("enter2");
            System.out.println("Le fichier de sauvegarde pour le profil \"" + fileName + "\" n'existe pas.");
            return null;
        }

        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            System.out.println("enter3");
            // Désérialisez et chargez l'objet Game
            Game game = (Game) objectIn.readObject();
            System.out.println("L'état du jeu a été chargé avec succès à partir de " + fileName);
            return game;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
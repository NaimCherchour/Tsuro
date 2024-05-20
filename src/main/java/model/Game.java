package main.java.model;

import java.io.*;
import java.util.*;

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

    private List<Joueur> deadPlayers = new ArrayList<>();


    public enum GameMode {
        CLASSIC,
        LONGEST_PATH,
        TIMER
    }
    private GameMode gameMode; // La variante du jeu

    private static int maxCompteur=0;

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

    public List<Joueur> getJoueurs(){
        return joueurs;
    }

    public List<ReadOnlyJoueur> getJoueursRO() {
        List<ReadOnlyJoueur> readOnlyJoueurs = new ArrayList<>();
        for (Joueur joueur : joueurs) {
            readOnlyJoueurs.add((ReadOnlyJoueur) joueur); // Supposez qu'il existe un constructeur qui prend un Joueur
        }
        return Collections.unmodifiableList(readOnlyJoueurs);
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


    @Override
    public int getGameModeInt(){
        switch (gameMode) {
            case CLASSIC:
                return 0;
            case LONGEST_PATH:
                return 1;
            case TIMER:
                return 2;
            default:
                throw new IllegalStateException("Mode de jeu non géré: " + gameMode);
        }
    }


    public Game(int type, int numberOfPlayers,int variante) {
        this.plateau = new PlateauTuiles(TAILLE_PLATEAU);
        this.deckTuiles = new DeckTuiles();
        this.observers = new ArrayList<>();
        Joueur.setIndexCouleur(1); //à chaque game partie, l'index de couleurs est réinitialisé
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
                jouerUnTour(tuile);
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
            notifyObserverTimerReset();
        }
    }

    public void jouerUnTour(Tuile tuile) {
        if (!joueurs.isEmpty()){
            verifierJoueursElimines();
            if (!joueurs.isEmpty()) {
                Joueur joueurCourant = joueurs.get(currentPlayerIndex);
                if (isCurrentPlayerBot()) { // Check if the current player is a bot
                    try {
                        playBotTurn((BotTsuro) joueurCourant); // Call the method to play the bot's turn
                        if ( gameMode == GameMode.LONGEST_PATH ){
                            if( joueurCourant.getCompteur() > maxCompteur){
                                maxCompteur = joueurCourant.getCompteur();
                            }

                        }
                        if (!joueurCourant.isAlive()) { // Check if the bot is still alive after its turn
                            joueurs.remove(joueurCourant); // Remove the bot from the player list
                            deadPlayers.add(joueurCourant);
                            NB_JOUEURS--;
                            currentPlayerIndexAct();
                            notifyObservers();
                            if (!joueurs.isEmpty()){
                                notifyObserversPlayerLost(joueurCourant.getPrenom()); // Notify observers of bot's loss
                            }
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
                        boolean repeat = false ;
                        // tuile magique
                        if (tuile.getId() == 19 || tuile.getId() == 27){
                            if ( gameMode == GameMode.LONGEST_PATH) {
                                repeat = true ;
                            }
                            else {
                                nextPlayer();
                                jouerUnTour(null);
                                return;
                            }
                        } if (!plateau.placerTuile(tuile, joueurCourant, joueurs) || !joueurCourant.isAlive()) {
                            if ( gameMode == GameMode.LONGEST_PATH ) {
                                if( joueurCourant.getCompteur() > maxCompteur){
                                    maxCompteur = joueurCourant.getCompteur();
                                }
                            }
                            Joueur joueurPerdant = joueurs.get(currentPlayerIndex);
                            joueurs.remove(joueurPerdant);
                            deadPlayers.add(joueurPerdant);
                            NB_JOUEURS--;
                            currentPlayerIndexAct();
                            notifyObservers();
                            if ( !joueurs.isEmpty()) {
                                notifyObserversPlayerLost(joueurPerdant.getPrenom());
                            }
                            verifierJoueursElimines();
                            jouerUnTour(null);
                        } else {
                            if ( gameMode == GameMode.LONGEST_PATH) {
                                if (joueurCourant.getCompteur() > maxCompteur) {
                                    maxCompteur = joueurCourant.getCompteur();
                                }
                            }
                            if ( !repeat ) {
                                nextPlayer();
                            }
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
                if (gameMode == GameMode.LONGEST_PATH ) {
                    Joueur j = checkWinner();
                    if ( j != null) {
                    notifyObserversPlayerWon(j.getPrenom());
                    }
                } else {
                notifyObserversWinnersTie();
                notifyGameEnd();
                }
            } else {
            if (gameMode == GameMode.LONGEST_PATH ) {
                Joueur jW = checkWinner();
                if ( jW != null ){
                    notifyObserversPlayerWon(jW.getPrenom());
                }
                gameState = false ;
                notifyGameEnd();
            }
            }
        }
        if (!(gameMode == GameMode.LONGEST_PATH)) {
            if(joueurs.size() == 1 ){
                Joueur j = joueurs.get(0);
                //System.out.println("REMOVING THE LAST PLAYER");
                notifyObserversPlayerWon(j.getPrenom());
                joueurs.remove(j);
                gameState = false ;
                notifyGameEnd();
            }
        }
    }

    private Joueur checkWinner() {
        for(Joueur j : deadPlayers){
            if ( j.getCompteur() >= maxCompteur) {
                return j;
            }
        }
        return deadPlayers.get(deadPlayers.size() - 1);
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


    public static int getMaxCompteur(){
        return maxCompteur;
    }

    public static void setMaxCompteur(int maxCompteur){
        Game.maxCompteur = maxCompteur;
    }


    private void currentPlayerIndexAct(){
        if (currentPlayerIndex >= NB_JOUEURS ){
            currentPlayerIndex = 0;
        }
    }
    private void nextPlayer() {
        if(!joueurs.isEmpty()){
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

    private void notifyObserverTimerReset(){
        for (GameObserver observer : observers){
            observer.resetTimer();
        }
    }

    private void notifyObserversGameSaved(){
        for (GameObserver observer : observers){
            observer.alertSavedGame();
        }
    }

    private void notifyObserversGameUpdated(){
        for (GameObserver observer : observers){
            observer.alertUpdateSavedGame();
        }
    }


    // Méthode pour sauvegarder l'état du jeu
    public void sauvegarderEtatJeu(String profileName) {
        int numberOfSavedGames = getNumberOfSavedGames(profileName);
        String savedGamePath = "src/main/resources/sauvegarde/" + profileName + "_" + numberOfSavedGames + ".ser";

        try (FileOutputStream fileOut = new FileOutputStream(savedGamePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Serialize and save the game state
            objectOut.writeObject(this);

            // Update profile with the new saved game path
            Profile userProfile = ProfileManager.getProfile(profileName);
            if (userProfile != null) {
                userProfile.addSavedGame(savedGamePath);
                ProfileManager.saveProfile(userProfile);
            }
            notifyObserversGameSaved();
            //System.out.println("Game state saved successfully in: " + savedGamePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getNumberOfSavedGames(String profileName) {
        Profile userProfile = ProfileManager.getProfile(profileName);
        return userProfile != null ? userProfile.getSavedGames().size() : 0;
    }

    public void sauvegarderEtatJeuCharge(String profileName, int savedGameIndex) {
        String savedGamePath = "src/main/resources/sauvegarde/" + profileName + "_" + savedGameIndex + ".ser";
        File existingFile = new File(savedGamePath);
        if (existingFile.exists()) {
            existingFile.delete(); // Supprimer le fichier existant
            // Supprimer l'ancien chemin de sauvegarde de la liste des jeux sauvegardés dans le profil de l'utilisateur
            Profile userProfile = ProfileManager.getProfile(profileName);
            if (userProfile != null) {
                userProfile.removeSavedGame(savedGamePath);
                ProfileManager.saveProfile(userProfile);
            }
        }
        try (FileOutputStream fileOut = new FileOutputStream(savedGamePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Serialize and save the updated game state
            objectOut.writeObject(this);

            // Mettre à jour le profil avec le nouveau chemin de sauvegarde
            Profile userProfile = ProfileManager.getProfile(profileName);
            if (userProfile != null) {
                userProfile.addSavedGame(savedGamePath);
                ProfileManager.saveProfile(userProfile);
            }

            // Notify observers and print success message
            notifyObserversGameUpdated();
            //System.out.println("Updated Game state saved successfully in: " + savedGamePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    public static Game chargerEtatJeu(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            //System.out.println("Le fichier de sauvegarde pour le profil \"" + fileName + "\" n'existe pas.");
            return null;
        }

        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            // Désérialisez et chargez l'objet Game
            Game game = (Game) objectIn.readObject();
            //System.out.println("L'état du jeu a été chargé avec succès à partir de " + fileName);
            return game;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
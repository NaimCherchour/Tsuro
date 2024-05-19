package main.java;

import main.java.controller.Controller;
import main.java.model.Game;
import main.java.model.ReadOnlyGame;
import main.java.vue.GameBoardUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main class to run the game
 */

public class Main {

    /**
     * Creates the Model(Game),Controller and view(GameBoardUI), MVC :
     * Controller is the mouse listener of the view
     * Controller knows the Model and updates it according to the view actions
     * View observes the Model ( version ReadOnly )
     * Model notifies the view if there's an update
     */

    public static void initializeAndRunGame(JFrame frame,String filePath, String username) {
        SwingUtilities.invokeLater(() -> {
                Game savedGame = Game.chargerEtatJeu(filePath);
                if (savedGame != null) {
                    // Charger la sauvegarde et lancer le jeu à partir de là
                    savedGame.setObservers(new ArrayList<>());
                    int index = extractFileNumber(filePath);
                    System.out.println("INDEXXXX" + index);
                    runLoadedGame(frame,savedGame,username,index);
                    return;
                }
        });
    }

    /**
     * Afin d'extraire le nombre du fichier qui a été chargé et pouvoir le mettre à jour
     * @param filePath
     * @return
     */
    private static int extractFileNumber(String filePath) {
        // Extraire le numéro de fichier de la chaîne filePath
        int lastIndex = filePath.lastIndexOf("_");
        int extensionIndex = filePath.lastIndexOf(".");
        String fileNumberString = filePath.substring(lastIndex + 1, extensionIndex);
        return Integer.parseInt(fileNumberString);
    }


    /**
     * Load the Game that has been saved and launch the game
     * @param game
     * @param username
     */
    private static void runLoadedGame(JFrame frame,Game game,String username, int indexGame) {
        boolean loadedGame = true ;

        Controller controller;
        try {
            controller = new Controller(game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameBoardUI gameBoardUI = null;
        try {
            gameBoardUI = new GameBoardUI(username,frame,loadedGame,indexGame);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameBoardUI.addMouseListener(controller);
        game.addObserver(gameBoardUI);
        gameBoardUI.update((ReadOnlyGame) game);
        if ( game.getGameModeInt() == 2 ) {
            game.notifyObserverTimerStart();
        }
        modifyFrame(frame,gameBoardUI);
    }

    private static void modifyFrame(JFrame frame, GameBoardUI gameBoardUI) {
        frame.getContentPane().removeAll();
        frame.setSize(1200, 850);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(gameBoardUI, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     *
     * @param type ; 0 for solo and 1 for multiplayer
     * @param finalNumberOfPlayers
     * @param variante ; classic or longest Path or Timer
     * @param username
     */

    private static void runGame(JFrame frame,int type, int finalNumberOfPlayers, int variante,String username) {
        boolean loadedGame = false ;
        Game game = new Game(type, finalNumberOfPlayers, variante);
        // Logique pour lancer le jeu à partir de l'état initial du jeu
        Controller controller = null;
        try {
            controller = new Controller(game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameBoardUI gameBoardUI = null;
        try {
            gameBoardUI = new GameBoardUI(username,frame,loadedGame);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameBoardUI.addMouseListener(controller);
        game.addObserver(gameBoardUI);
        gameBoardUI.update((ReadOnlyGame) game);
        if ( variante == 2 ) {
            game.notifyObserverTimerStart();
        }

        modifyFrame(frame,gameBoardUI);
    }


    public static void solo(JFrame frame, int variante, String username) {
        runGame(frame,0, 1,variante ,username);
    }

    public static void multiPlayer(JFrame frame,int finalNumberOfPlayers,int variante, String username) {
        runGame(frame,1, finalNumberOfPlayers,variante,username);
    }

}

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
     * Creates the Model(Game),Controler and view(GameBoardUI), MVC :
     * Controller is the mouselistener of the view
     * Controller knows the Model and updates it according to the view actions
     * View observes the Model ( version ReadOnly )
     * Model notifies the view if there's an update
     */

    public static void initializeAndRunGame(String filePath, String username) {
        SwingUtilities.invokeLater(() -> {
                Game savedGame = Game.chargerEtatJeu(filePath);
                if (savedGame != null) {
                    // Charger la sauvegarde et lancer le jeu à partir de là
                    savedGame.setObservers(new ArrayList<>());
                    runLoadedGame(savedGame,username);
                    return;
                }
        });
    }


    /**
     * Load the Game that has been saved and laucnh the game
     * @param game
     * @param username
     */
    private static void runLoadedGame(Game game,String username) {
        Controller controller = null;
        try {
            controller = new Controller(game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameBoardUI gameBoardUI = null;
        try {
            gameBoardUI = new GameBoardUI(username,game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameBoardUI.addMouseListener(controller);
        game.addObserver(gameBoardUI);
        gameBoardUI.update((ReadOnlyGame) game);

        JFrame frame = new JFrame("Tsuro Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
     * @param variante ; classic or longest Path
     * @param username
     */

    private static void runGame(int type, int finalNumberOfPlayers, int variante,String username) {
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
            gameBoardUI = new GameBoardUI(username,game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameBoardUI.addMouseListener(controller);
        game.addObserver(gameBoardUI);
        gameBoardUI.update((ReadOnlyGame) game);

        JFrame frame = new JFrame("Tsuro Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 850);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(gameBoardUI, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static void solo(int variante, String username) {
        runGame(0, 1,variante ,username);
    }

    public static void multiPlayer(int finalNumberOfPlayers,int variante, String username) {
        runGame(1, finalNumberOfPlayers,variante,username);
    }

}

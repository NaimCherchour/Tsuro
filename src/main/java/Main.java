package main.java;

import main.java.controller.Controller;
import main.java.model.Game;
import main.java.model.ReadOnlyGame;
import main.java.vue.GameBoardUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
     * @param type ; 0 for solo and 1 for multiplayer
     * @param finalNumberOfPlayers
     */
    private static void initializeAndRunGame(int type, int finalNumberOfPlayers) {
        SwingUtilities.invokeLater(() -> {
            // model
            Game game = new Game(type, finalNumberOfPlayers);
            // controller
            Controller controller = null;
            try {
                controller = new Controller(game);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // view
            GameBoardUI gameBoardUI = null;
            try {
                gameBoardUI = new GameBoardUI(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            gameBoardUI.addMouseListener(controller);
            game.addObserver(gameBoardUI); // View observes the game
            gameBoardUI.update((ReadOnlyGame) game); // first update to show the view

            JFrame frame = new JFrame("Tsuro Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 850);
            frame.setResizable(false);

            frame.setLayout(new BorderLayout());
            frame.add(gameBoardUI, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void solo() {
        initializeAndRunGame(0, 1);
    }

    public static void multiPlayer(int finalNumberOfPlayers) {
        initializeAndRunGame(1, finalNumberOfPlayers);
    }

}

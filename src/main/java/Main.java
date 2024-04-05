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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // model
            Game game = new Game(6, 2);
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
                gameBoardUI = new GameBoardUI(controller);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            game.addObserver(gameBoardUI); // View observes the game
            gameBoardUI.update((ReadOnlyGame) game); // first update to show the view

            //gameBoardUI.addMouseListener(controller); // Controller listens to the view , already in the view ??
            //gameBoardUI.update((ReadOnlyGame) game);




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

}

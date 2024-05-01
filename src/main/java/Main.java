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
        // choisir le type et le nombre de joueur
        Object[] options = {"1v1 against Bot", "Multiplayer (2-8 players)"};
        int type = JOptionPane.showOptionDialog(null, "Choose the game mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (type == -1) {
            System.exit(0); // Exit the program if we click on X
        }

        int numberOfPlayers = 0 ;
        if ( type == 1) {
            while (true) {
                String input = JOptionPane.showInputDialog("Enter the number of players (2-8):");
                if (input == null) {
                    System.exit(0);
                }
                try {
                    numberOfPlayers = Integer.parseInt(input);
                    if ( !(numberOfPlayers >= 2 && numberOfPlayers <= 8) ) {
                        JOptionPane.showMessageDialog(null, "Enter a number between 2 and 8.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Enter a valid number.");
                }
            }
        }

        int finalNumberOfPlayers = numberOfPlayers;
        SwingUtilities.invokeLater(() -> {
            // model
            Game game =new Game(type, finalNumberOfPlayers);
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
                gameBoardUI = new GameBoardUI();
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

}

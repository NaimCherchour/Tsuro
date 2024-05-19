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
 * Classe principale pour exécuter le jeu.
 */

public class Main {

    /**
     * Crée le modèle (Game), le contrôleur et la vue (GameBoardUI) en suivant le modèle MVC :
     * - Le contrôleur est l'écouteur de la souris de la vue.
     * - Le contrôleur connaît le modèle et le met à jour en fonction des actions de la vue.
     * - La vue observe le modèle (version ReadOnly).
     * - Le modèle notifie la vue en cas de mise à jour.
     *
     * @param frame    la fenêtre principale de l'application
     * @param filePath le chemin du fichier pour charger une partie sauvegardée
     * @param username le nom d'utilisateur du joueur
     */
    public static void initializeAndRunGame(JFrame frame, String filePath, String username) {
        SwingUtilities.invokeLater(() -> {
            Game savedGame = Game.chargerEtatJeu(filePath);
            if (savedGame != null) {
                // Charger la partie sauvegardée et démarrer à partir de là
                savedGame.setObservers(new ArrayList<>());
                int index = extractFileNumber(filePath);
                runLoadedGame(frame, savedGame, username, index);
                return;
            }
        });
    }

    /**
     * Extrait le numéro du chemin de fichier pour pouvoir le mettre à jour.
     *
     * @param filePath le chemin du fichier contenant le numéro
     * @return le numéro extrait
     */
    private static int extractFileNumber(String filePath) {
        // Extraire le numéro du fichier de la chaîne de chemin de fichier
        int lastIndex = filePath.lastIndexOf("_");
        int extensionIndex = filePath.lastIndexOf(".");
        String fileNumberString = filePath.substring(lastIndex + 1, extensionIndex);
        return Integer.parseInt(fileNumberString);
    }

    /**
     * Charge la partie qui a été sauvegardée et lance le jeu.
     *
     * @param frame    la fenêtre principale de l'application
     * @param game     la partie chargée
     * @param username le nom d'utilisateur du joueur
     * @param indexGame l'index de la partie en cours de chargement
     */
    private static void runLoadedGame(JFrame frame, Game game, String username, int indexGame) {
        boolean loadedGame = true;

        Controller controller;
        try {
            controller = new Controller(game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameBoardUI gameBoardUI;
        try {
            gameBoardUI = new GameBoardUI(username, frame, loadedGame, indexGame);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameBoardUI.addMouseListener(controller);
        game.addObserver(gameBoardUI);
        gameBoardUI.update((ReadOnlyGame) game);
        if (game.getGameModeInt() == 2) {
            game.notifyObserverTimerStart();
        }
        modifyFrame(frame, gameBoardUI);
    }

    /**
     * Modifie la fenêtre JFrame pour inclure l'interface utilisateur du plateau de jeu.
     *
     * @param frame      la fenêtre principale de l'application
     * @param gameBoardUI l'interface utilisateur du plateau de jeu à ajouter à la fenêtre
     */
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
     * Exécute le jeu avec les paramètres spécifiés.
     *
     * @param frame                la fenêtre principale de l'application
     * @param type                 0 pour solo et 1 pour multijoueur
     * @param finalNumberOfPlayers le nombre final de joueurs
     * @param variante             la variante du jeu (classique, plus long chemin ou minuterie)
     * @param username             le nom d'utilisateur du joueur
     */

    private static void runGame(JFrame frame,int type, int finalNumberOfPlayers, int variante,String username) {
        boolean loadedGame = false ;
        Game game = new Game(type, finalNumberOfPlayers, variante);
        // Logique pour démarrer le jeu à partir de l'état initial
        Controller controller;
        try {
            controller = new Controller(game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameBoardUI gameBoardUI;
        try {
            gameBoardUI = new GameBoardUI(username, frame, loadedGame);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameBoardUI.addMouseListener(controller);
        game.addObserver(gameBoardUI);
        gameBoardUI.update((ReadOnlyGame) game);
        if (variante == 2) {
            game.notifyObserverTimerStart();
        }

        modifyFrame(frame, gameBoardUI);
    }

    /**
     * Démarre une partie solo.
     *
     * @param frame    la fenêtre principale de l'application
     * @param variante la variante du jeu (classique, plus long chemin ou minuterie)
     * @param username le nom d'utilisateur du joueur
     */
    public static void solo(JFrame frame, int variante, String username) {
        runGame(frame, 0, 1, variante, username);
    }

    /**
     * Démarre une partie multijoueur.
     *
     * @param frame                la fenêtre principale de l'application
     * @param finalNumberOfPlayers le nombre final de joueurs
     * @param variante             la variante du jeu (classique, plus long chemin ou minuterie)
     * @param username             le nom d'utilisateur du joueur
     */
    public static void multiPlayer(JFrame frame, int finalNumberOfPlayers, int variante, String username) {
        runGame(frame, 1, finalNumberOfPlayers, variante, username);
    }

}

package Test.java.controller;

import main.java.model.*;
import main.java.controller.Controller;

import org.junit.Test;
import java.io.IOException;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ControllerTest {

    // Test de la méthode placerTuileFromDeck() du contrôleur
    @Test
    public void testPlacerTuileFromDeck() throws IOException {
        // Initialisation du jeu avec 1 joueur, 5 lignes et 0 colonnes (ou autre paramètre)
        Game game = new Game(1, 5, 0);
        // Création du contrôleur avec le jeu initialisé
        Controller controller = new Controller(game);
        // Tuile factice pour le test
        Tuile tuile = new Tuile(1, new int[]{5, 6, 4, 7, 2, 0, 1, 3});
        // Obtention de la position initiale du joueur (ligne et colonne)
        int X = game.getJoueurs().get(0).getLigne();
        int Y = game.getJoueurs().get(0).getColonne();
        // Appel de la méthode pour placer la tuile
        controller.handleTilePlacement(tuile);

        // Récupération de la tuile placée sur le plateau à la position du joueur
        Tuile placedTile = game.getPlateau().getTuile(X, Y);
        // Vérification que la tuile placée est bien celle créée pour le test
        assertEquals(tuile, placedTile);
    }

    // Test de la méthode rotateTile() du contrôleur
    @Test
    public void testRotateTile() throws IOException {
        // Initialisation du jeu avec 0 joueur, 5 lignes et 0 colonnes (ou autre paramètre)
        Game game = new Game(0, 5, 0);
        // Création du contrôleur avec le jeu initialisé
        Controller controller = new Controller(game);
        // Création d'une tuile factice pour le test avec un ID et une séquence de valeurs
        Tuile tuile = new Tuile(1, new int[]{5, 6, 4, 7, 2, 0, 1, 3});

         int orientationInitiale = tuile.getRotation();
         // pivoter la tuile
         controller.rotateTile(tuile);

         // pas la meme rotation
         assertNotEquals(orientationInitiale, tuile.getRotation());

    }
}

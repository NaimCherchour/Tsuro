package Test.java.controller;

import main.java.model.*;
import main.java.controller.Controller;

import org.junit.Test;
import java.io.IOException;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ControllerTest {

    @Test
    public void testPlacerTuileFromDeck() throws IOException {
        Game game = new Game(1,5);
        Controller controller = new Controller(game);
        // Tuile factice pour le test
        Tuile tuile = new Tuile(1, new int[]{5, 6, 4, 7, 2, 0, 1, 3});
        int X = game.getJoueurs().get(0).getLigne();
        int Y = game.getJoueurs().get(0).getColonne();
        controller.handleTilePlacement(tuile);

        Tuile placedTile = game.getPlateau().getTuile(X,Y);
        assertEquals(tuile, placedTile);
    }

    @Test
    public void testRotateTile() throws IOException {
        Game game = new Game(0,5);
        Controller controller = new Controller(game);
        Tuile tuile = new Tuile(1, new int[]{5, 6, 4, 7, 2, 0, 1, 3});

         int orientationInitiale = tuile.getRotation();
         // pivoter la tuile
         controller.rotateTile(tuile);

         // pas la meme rotation
         assertNotEquals(orientationInitiale, tuile.getRotation());

    }
}

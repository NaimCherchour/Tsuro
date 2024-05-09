package Test.java.model;

import main.java.model.*;


import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;


public class GameTest {

    @Test
    public void testGameInitializationWithTwoPlayers() {
        Game game = new Game(0, 2);

        assertNotNull(game.getPlateau());

        assertNotNull(game.getDeckTuiles());

        List<Joueur> joueurs = game.getJoueurs();
        assertNotNull(joueurs);
        assertEquals(2, joueurs.size());
    }

    @Test
    public void testGameInitializationWithThreePlayers() {
        Game game = new Game(1, 3);

        assertNotNull(game.getPlateau());

        assertNotNull(game.getDeckTuiles());

        List<Joueur> joueurs = game.getJoueurs();
        assertNotNull(joueurs);
        assertEquals(3, joueurs.size());
    }

    @Test
    public void testNextPlayer() {
        Game game = new Game(0, 2);
        List<Joueur> joueurs = game.getJoueurs();
        int currentPlayerIndex = game.getCurrentPlayerIndex();

        assertEquals(joueurs.get(currentPlayerIndex), joueurs.get(0));

        game.passerAuJoueurSuivant();
        currentPlayerIndex = game.getCurrentPlayerIndex();

        assertEquals(joueurs.get(currentPlayerIndex), joueurs.get(1));
    }

    @Test
    public void testGameInitializationWithBotAndHumanPlayer() {
        Game game = new Game(0, 2);
        List<Joueur> joueurs = game.getJoueurs();

        assertNotNull(joueurs.get(0));

        assertTrue(joueurs.get(1) instanceof BotTsuro);
    }

    @Test
    public void testIsCurrentPlayerBot() {
        Game game = new Game(0, 2);
        List<Joueur> joueurs = game.getJoueurs();

        assertFalse(game.isCurrentPlayerBot());

        game.passerAuJoueurSuivant();

        assertTrue(game.isCurrentPlayerBot());
    }
}


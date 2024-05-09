package Test.java.model;

import main.java.model.*;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class BotTsuroTest {

    @Test
    public void testChoisirEtAppliquerMouvement() {
        Game game = new Game(0, 2);
        BotTsuro bot = new BotTsuro("Bot", game.getJoueurs());

        int X = bot.getLigne();
        int Y = bot.getColonne();
        bot.choisirEtAppliquerMouvement(game);

        assertNotNull(game.getPlateau().getTuile(X,Y));
    }

    @Test
    public void testEvaluerMouvement() {
        // Créer un jeu avec un bot
        Game game = new Game(0, 2);
        BotTsuro bot = new BotTsuro("Bot", game.getJoueurs());

        bot.setPointEntree(4);
        Tuile tuileTest = new Tuile(1, new int[]{1, 0, 3, 2, 7, 6, 5, 4});
        PlateauTuiles plateauTest = new PlateauTuiles(6);

        // Vérifier l'évaluation du mouvement
        int score = bot.evaluerMouvement(tuileTest, plateauTest, game.getJoueurs());
        assertEquals(1000, score);
    }

    // Test pour vérifier si le coup est gagnant pour le bot
    @Test
    public void testEstCoupGagnant() {
        // Créer un jeu avec un bot
        Game game = new Game(0, 2);
        BotTsuro bot = new BotTsuro("Bot", game.getJoueurs());

        bot.setPointEntree(5); // 5 vers 6 ; correct
        Tuile tuileTest = new Tuile(1, new int[]{1, 0, 3, 2, 7, 6, 5, 4});
        PlateauTuiles plateauTest = new PlateauTuiles(6);
        List<Joueur> joueursTest = new ArrayList<>(game.getJoueurs());

        // Vérifier si le coup est gagnant
        boolean isWinningMove = bot.estCoupGagnant(new BotTsuro.Mouvement(tuileTest, 0, 0, 0, 0, 0), plateauTest, joueursTest);
        assertTrue(isWinningMove);
    }

    /*
    @Test
    public void testEstCoupPerdantPourAdversaire() {
        Game game = new Game(0, 2);
        BotTsuro bot = new BotTsuro("Bot", game.getJoueurs());

        Tuile tuileTest = new Tuile(1, new int[]{1, 0, 3, 2, 7, 6, 5, 4});
        PlateauTuiles plateauTest = new PlateauTuiles(6);
        Joueur adversaireTest = new Joueur("Adversaire", game.getJoueurs());
        List<Joueur> joueursTest = new ArrayList<>(game.getJoueurs());
        joueursTest.add(adversaireTest);

        boolean isLosingMove = bot.estCoupPerdantPourAdversaire(new BotTsuro.Mouvement(tuileTest, 0, 0, 0, 0, 0), plateauTest, adversaireTest, joueursTest);
        assertFalse(isLosingMove); // Le coup ne devrait pas être perdant pour l'adversaire
    } */

    @Test
    public void testEstCoupPerdant() {
        // Créer un jeu avec un bot
        Game game = new Game(0, 2);
        BotTsuro bot = new BotTsuro("Bot", game.getJoueurs());

        bot.setPointEntree(0); // 0 vers 1 , on perd
        Tuile tuileTest = new Tuile(1, new int[]{1, 0, 3, 2, 7, 6, 5, 4});
        PlateauTuiles plateauTest = new PlateauTuiles(6);

        boolean isLosingMove = bot.estCoupPerdant(new BotTsuro.Mouvement(tuileTest, 0, 0, 0, 0, 0), plateauTest);
        assertTrue(isLosingMove);
    }
}


package Test.java.model;

import static org.junit.Assert.*;

import main.java.model.Joueur;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class JoueurTest {

    private Joueur joueur;

    @Before
    public void setUp() {
        // Initialisation du joueur avant chaque test
        List<Joueur> joueurs = new ArrayList<>();
        joueur = new Joueur("Naim", joueurs);
    }

    @Test
    public void testCreationJoueur() {
        assertNotNull(joueur);
        assertEquals("Naim", joueur.getPrenom());
        assertNotNull(joueur.getCouleur());
    }

    @Test
    public void testPositionInitialeJoueur() {
        assertTrue(joueur.getLigne() >= 0 && joueur.getLigne() <= 5);
        assertTrue(joueur.getColonne() >= 0 && joueur.getColonne() <= 5);
        assertTrue(joueur.getPointEntree() >= 0 && joueur.getPointEntree() <= 7);
    }

    @Test
    public void testIncrementationCompteur() {
        assertEquals(0, joueur.getCompteur());
        joueur.incrementerCompteur();
        assertEquals(1, joueur.getCompteur());
    }

}

package Test.java.model;

import static org.junit.Assert.*;

import main.java.model.Joueur;
import main.java.model.PlateauTuiles;
import main.java.model.Tuile;


import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class PlateauTuilesTest {

    @Test
    public void testPlacerTuile() {
        PlateauTuiles plateau = new PlateauTuiles(6);
        Tuile tuile = new Tuile(3, new int[]{1, 0, 4, 6, 2, 7, 3, 5});
        Joueur joueur = new Joueur("Naim", new ArrayList<>());

        int X = joueur.getLigne();
        int Y = joueur.getColonne();

        assertTrue(plateau.placerTuile(tuile, joueur, new ArrayList<>()));
        // Vérifier que la tuile est correctement placée sur le plateau
        assertEquals(tuile, plateau.getTuile(X,Y));
    }

    @Test
    public void testIsEmpty() {
        PlateauTuiles plateau = new PlateauTuiles(6);
        assertTrue(plateau.isEmpty(0, 0)); // La case est vide initialement
        Tuile tuile =new Tuile(4, new int[]{2, 5, 0, 7, 6, 1, 4, 3});
        Joueur j = new Joueur("Dejan", new ArrayList<>());
        int X = j.getLigne();
        int Y = j.getColonne();
        plateau.placerTuile(tuile, j , new ArrayList<>());
        assertFalse(plateau.isEmpty(X,Y));
    }

    @Test
    public void testActualiserPosJ() {
        PlateauTuiles plateau = new PlateauTuiles(6);
        Tuile tuile = new Tuile(4, new int[]{2, 5, 0, 7, 6, 1, 4, 3});
        Joueur joueur = new Joueur("Naim", new ArrayList<>());
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(joueur);

        plateau.placerTuile(tuile, joueur, joueurs);

        // Actualisation de la position des joueurs après le placement d'une tuile
        assertEquals(1, joueur.getCompteur());
    }

    @Test
    public void testEnleverTuile() {
        PlateauTuiles plateau = new PlateauTuiles(6);
        Tuile tuile = new Tuile(6, new int[]{1, 0, 5, 7, 6, 2, 4, 3});

        Joueur j =  new Joueur("Naim1", new ArrayList<>()) ;
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(j);

        int X = j.getLigne();
        int Y = j.getColonne();
        plateau.placerTuile(tuile,j,joueurs);

        // Enlever la tuile du plateau
        plateau.enleverTuile(X, Y);

        // Vérifier que la case est vide après avoir enlevé la tuile
        assertTrue(plateau.isEmpty(0, 0));
    }

    @Test
    public void testJoueurPerdu() {
        PlateauTuiles plateau = new PlateauTuiles(6);
        Joueur joueur = new Joueur("Naim2", new ArrayList<>());

        // Tester si un joueur est perdu lorsqu'il sort du plateau
        joueur.setLigne(-1);
        assertTrue(plateau.joueurPerdu(joueur));

        joueur.setLigne(6);
        assertTrue(plateau.joueurPerdu(joueur));

        joueur.setLigne(0);
        joueur.setColonne(-1);
        assertTrue(plateau.joueurPerdu(joueur));

        joueur.setColonne(6);
        assertTrue(plateau.joueurPerdu(joueur));

        // Tester le cas où le joueur est toujours sur le plateau
        joueur.setLigne(3);
        joueur.setColonne(3);
        assertFalse(plateau.joueurPerdu(joueur));
    }

    @Test
    public void testReinitializePlateau() {
        PlateauTuiles plateau = new PlateauTuiles(6);
        Tuile tuile = new Tuile(7, new int[]{2, 4, 0, 6, 1, 7, 3, 5});
        plateau.placerTuile(tuile, new Joueur("Naim3", new ArrayList<>()), new ArrayList<>());

        // Réinitialiser le plateau
        plateau.reinitializePlateau();

        // Vérifier que toutes les cases du plateau sont vides
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                assertTrue(plateau.isEmpty(i, j));
            }
        }
    }
}


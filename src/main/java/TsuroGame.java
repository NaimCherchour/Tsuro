package main.java;

import main.java.menu.Accueil;
//import main.java.menu.MainMenu;

import javax.swing.*;

public class TsuroGame {

    private static Accueil accueil;

    // Le jeu se lance desormais depuis cette classe.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            accueil = new Accueil();
            accueil.show();
        });
    }
}

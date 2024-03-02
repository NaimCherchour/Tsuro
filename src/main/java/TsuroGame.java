package main.java;

import main.java.vue.Accueil;
//import main.java.vue.MainMenu;

import javax.swing.*;

public class TsuroGame {

    private static Accueil accueil;
    //private static MainMenu mainMenu;


    // Le jeu se lance desormais depuis cette classe.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            accueil = new Accueil();
            accueil.show();
        });
    }
}

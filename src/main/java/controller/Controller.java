package main.java.controller;


import main.java.model.Game;
import main.java.model.Tuile;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Controller implements MouseListener {

    //TODO: À REVOIR LA RELATION ENTRE LE CONTROLEUR ET LA VUE

    private Game game; // C'est le model ; La classe Game représente une partie du jeu

    // Constructeur
    public Controller(Game game) throws IOException {
            this.game =  game;
    }

    // Méthode pour gérer le placement d'une tuile sur le plateau
    public void handleTilePlacement(Tuile tuile) {
            game.jouerUnTour(tuile); // Placer la tuile sur le plateau et mettre à jour la position des joueurs
            game.notifyObservers(); // Alerter les observateurs que le model Game a changé
    }

    // Méthode pour gérer la rotation d'une tuile
    public void rotateTile(Tuile tuile) {
        tuile.tournerTuile(); // Appeler la méthode pour faire pivoter la tuile
        game.notifyObservers(); // Alerter les observateurs que le model Game a changé
    }

    // TODO: Les écouteurs d'événements de souris sont censés être implémentés ici et non pas dans la vue
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}

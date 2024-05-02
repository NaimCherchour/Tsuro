package main.java.controller;

import main.java.model.Game;
import main.java.model.Tuile;
import main.java.vue.TuilePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Controller implements MouseListener, ActionListener {
    private Game game; // C'est le model ; La classe Game représente une partie du jeu

    // Constructeur
    public Controller(Game game) throws IOException {
            this.game =  game;
    }

    public void placerTuileFromDeck(Tuile tuile){
        int index = 0;
        for (int i = 0; i < game.getDeckTuiles().getSideTuiles().length; i++) {
            if (game.getDeckTuiles().getSideTuiles()[i].equals(tuile)){
                index = i ;
            }
        }
        Tuile placedTile = this.game.getDeckTuiles().getTuile(index);
        if (tuile != null) {
            handleTilePlacement(placedTile);
        }
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

    // Clic sur une tuile pour la poser
    @Override
    public void mouseClicked(MouseEvent e) {
        Component clickedComponent = e.getComponent();
        if (game.getGameState()) {
            if (clickedComponent instanceof TuilePanel tuilePanel) {
                placerTuileFromDeck(tuilePanel.getTuile());
            }
        }
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

    //Bouton Rotate
    @Override
    public void actionPerformed(ActionEvent e) {
        // Determine which button was clicked
        JButton clickedButton = (JButton) e.getSource();
        TuilePanel tuilePanel = (TuilePanel) clickedButton.getClientProperty("tuilePanel");
        rotateTile(tuilePanel.getTuile());
    }

}

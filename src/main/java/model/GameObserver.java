package main.java.model;

import java.util.Observer;


/**
 * Interface pour les classes qui se font observer durant le jeu , en Readonly pour ne pas modifier les valeurs
 */
public interface GameObserver {

    /**
     * Méthode pour mettre à jour le jeu ; le plateau et les joueurs en passant
     * un argument Game en lecture seule ; on ne pourra pas le modifier
     * @param game
     */
    void update(ReadOnlyGame game);

    /**
     * Méthode pour notifier que le joueur a perdu
     * @param joueur
     */
    void playerLost(String joueur);

    /**
     * Méthode pour notifier que le joueur a gagné
     * @param joueur
     */
    void playerWon(String joueur);

    /**
     * Méthode pour notifier que la partie est finie
     */
    void gameFinish ();

    /**
     * Méthode pour notifier que la partie se termine sur une égalité de deux Joueurs
     */
    void gameWinnersTie();

    /**
     * Méthode pour lancer le timer dès que le Game est crée
     */
    void startTurnTimer();

    /**
     * Méthode qui permet de rénitialiser le timer apres que le joueur ait placé une tuile 
     */
    void resetTimer();
}

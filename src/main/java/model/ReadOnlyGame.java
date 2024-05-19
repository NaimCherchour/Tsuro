package main.java.model;

import java.util.List;

/**
 * Interface pour les classes qui se font observer durant le jeu , en Readonly pour ne pas modifier les valeurs
 */
public interface ReadOnlyGame {
    /**
     * Retourne la tuile aux coordonnées i,j pour afficher les tuiles du plateau dans le paintComponent de GameBoardUI
     * @param i : la ligne de la tuile
     * @param j : la colonne de la tuile
     * @return
     */
    Tuile getTuile(int i, int j);

    /**
     * Retourne la liste des joueurs
     * @return
     */
    List<Joueur> getJoueurs();

    /**
     * Retourne l'indice du joueur courant pour l'affichage du tour de rôle
     * @return
     */
    int getCurrentPlayerIndex();

    /**
     * Retourne le deck de tuiles pour afficher les tuiles du SidePanel
     * @return
     */
    DeckTuiles getDeckTuiles();

    /**
     * Retourne l'état du jeu ; true : Playing , false : Over
     */
    boolean getGameState();


    /**
     * récupérer la variante du jeu
     */

    int getGameModeInt();

    int getNumberOfSavedGames(String username);

}

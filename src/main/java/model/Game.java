package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

public class Game extends Observable implements ReadOnlyGame {
    private PlateauTuiles plateau; // Le plateau de jeu
    private List<Joueur> joueurs; // La liste des joueurs
    private static int NB_JOUEURS; // Le nombre de joueurs dans la partie
    private int currentPlayerIndex; // L'indice du joueur courant
    private final DeckTuiles deckTuiles; // Le deck de tuiles
    private List<GameObserver> observers; // Les observateurs de la partie


    public DeckTuiles getDeckTuiles() {
        return deckTuiles;
    }
    public PlateauTuiles getPlateau() {
        return plateau;
    }

    @Override
    public Tuile getTuile(int i, int j) {
        return plateau.getTuile(i, j);
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }
    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }


    public Game(int taillePlateau, int nombreJoueurs) {
        this.plateau = new PlateauTuiles(taillePlateau);
        this.deckTuiles = new DeckTuiles();
        NB_JOUEURS =  getNumberOfPlayersFromUser();

        initializePlayers(NB_JOUEURS);
        this.observers = new ArrayList<>();
    }

    private int getNumberOfPlayersFromUser() {
        int numberOfPlayers = 0;
        boolean flag = false;
        while (!flag) {
            try {
                String input = JOptionPane.showInputDialog("Entrer le nombre de joueur:");
                numberOfPlayers = Integer.parseInt(input);
                if (numberOfPlayers >= 1 && numberOfPlayers <= 8) {
                    flag = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Entrer un nombre entre 1 et 8.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrer un nombre valide .");
            }
        }
        return numberOfPlayers;
    }


    private void initializePlayers(int numberOfPlayers) {
        //TODO : le prénom sera etré par l'utilisateur dans le menu
        currentPlayerIndex = 0;
        joueurs = new ArrayList<>();
        for (int i = 0; i <  numberOfPlayers; i++) {
            Joueur joueur = new Joueur( "Joueur " + (i + 1),joueurs);
            joueurs.add(joueur);
        }
    }


    public void jouerUnTour(Tuile tuile) {
        //TODO : À améliorer
        Joueur joueurCourant;
        if (NB_JOUEURS == 1) { // 1 seul joueur restant
            Joueur j = joueurs.get(0);
            notifyObservers();
            notifyObserversPlayerWon(j.getPrenom());
            return;
        }
        joueurCourant = joueurs.get(currentPlayerIndex);
        if ( !plateau.placerTuile(tuile, joueurCourant,joueurs) || !joueurCourant.isAlive() ) {
            // Récupérer le joueur avant de le supprimer de la liste
            Joueur joueurPerdant = joueurs.get(currentPlayerIndex);
            joueurs.remove(joueurPerdant);
            NB_JOUEURS--;
            currentPlayerIndexAct();
            notifyObservers();
            notifyObserversPlayerLost(joueurPerdant.getPrenom());

            //JOptionPane.showMessageDialog(this, joueurPerdant.getPrenom() + " (" + joueurPerdant.getCouleur().toString() +  ") a perdu ! ");
            System.out.println("COL"+ joueurCourant.getColonne() + "LIGN" + joueurCourant.getLigne()+ "ENTR" + joueurCourant.getPointEntree());
            // Vérifier s'il ne reste qu'un seul joueur

        } else {
            System.out.println("COL"+ joueurCourant.getColonne() + "LIGN" + joueurCourant.getLigne()+ "ENTR" + joueurCourant.getPointEntree());
            nextPlayer();
        }
    }

    private void currentPlayerIndexAct(){
        if (currentPlayerIndex >= NB_JOUEURS ){
            currentPlayerIndex = 0;
        }
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % NB_JOUEURS;
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Méthode pour notifier les observateurs de la partie pour mettre à jour la vue avec les nouvelles données
     * mais en ReadOnly
     */
    @Override
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(this);
        }
    }

    /**
     * Méthode pour notifier les observateurs de la partie que le joueur a perdu
     * @param playerName
     */
    private void notifyObserversPlayerLost(String playerName) {
        for (GameObserver observer : observers) {
            observer.playerLost(playerName);
        }
    }

    /**
     * Méthode pour notifier les observateurs de la partie que le joueur a gagné
     * @param playerName
     */
    private void notifyObserversPlayerWon(String playerName) {
        for (GameObserver observer : observers) {
            observer.playerWon(playerName);
        }
    }



}




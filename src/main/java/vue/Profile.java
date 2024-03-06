package main.java.vue;

import javax.swing.JOptionPane;

public class Profile {
    private String playerName;
    private int score;

    
    public Profile() {
        this.playerName = "";
        this.score = 0; 
    }

    // Getters et setters pour accéder et modifier les attributs
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Méthode pour permettre au joueur de définir son nom
    public void choosePlayerName() {
        String playerName = JOptionPane.showInputDialog("Choisissez votre nom de joueur :");
        if (playerName != null && !playerName.trim().isEmpty()) {
            setPlayerName(playerName.trim());
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez choisir un nom valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            choosePlayerName(); // Redemande le nom si la saisie est invalide.
        }
    }

}

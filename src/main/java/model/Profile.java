package main.java.model;

import java.util.List;

/**
 * représente un profil ; un utilisateur de l'application et non pas un joueur du jeu
 */

public class Profile {
    private String username;
    private List<String> savedGames; // les chemins des fichiers des parties sauvegardées file.ser

    public Profile(String user , List<String> cheminsSavedGames){
        this.username = user;
        this.savedGames = cheminsSavedGames;
    }

    public void setUsername(String storedUsername) {
        this.username = storedUsername ;
    }
    public void setSavedGames(List<String> savedGames) {
        this.savedGames = savedGames;
    }

    public String getUsername(){
        return  this.username;
    }

    public List<String> getSavedGames(){
        return this.savedGames;
    }

    public void addSavedGame(String savedGamePath) {
        savedGames.add(savedGamePath);
    }
}

package main.java.controller;

// Cet enum sert à distinguer les actions des boutons de la vue afin
// de distinguer leurs actions correspondantes dans le controller
public enum Action {
    ROTATE("Rotate"),
    SAVE("Save"),
    SAVE_LOADED_GAME("Save_Loaded_Game");

    private final String action;

    Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}


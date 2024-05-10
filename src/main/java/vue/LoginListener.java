package main.java.vue;

/**
 * Cette interface permet de faire la connexion entre le model de la Page Login
 * et la vue de La page Login pour alerter si un user a été authentifier correctement ou non
 * afin d'afficher un message dans la Page du Login
 */
public interface LoginListener {

    /**
     * Connexion réussie
     */
    void notifySuccessLog();

    /**
     * Mot De passe erroné
     */
    void notifyWrongPass();

    /**
     * Nouvel utilisateur
     */
    void notifyNewUser();
}

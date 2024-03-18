package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.java.model.BotTsuro.Mouvement;

public class Game {
    private PlateauTuiles plateau; // Le plateau de jeu
    private List<Joueur> joueurs; // La liste des joueurs
    // Une liste car on peut ajouter ou supprimer des joueurs

    public Game(int taillePlateau, int nombreJoueurs) {
        plateau = new PlateauTuiles(taillePlateau);
        joueurs = new ArrayList<>();
        for (int i = 0; i < nombreJoueurs; i++) {
            boolean estBot = (i % 2 == 0); // Exemple: les joueurs pairs sont des bots, à titre d'exemple
            Joueur joueur = new Joueur(0, 0, i + 1, "Joueur " + (i + 1), estBot);
            joueurs.add(joueur);
        }
    }
    
    public PlateauTuiles getPlateau() {
        return plateau;
    }


    public boolean estFinie() {
        return joueurs.size() <= 1;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    // Cette méthode pourrait être plus complexe en réalité
    public void appliquerMouvement(Mouvement mouvement, Joueur joueur) {
        plateau.placerTuile(mouvement.getX(), mouvement.getY(), mouvement.getTuile(), joueur);
        plateau.actualiserPosJ(joueur);
    }

    

    public void jouerPartie() {
        Scanner scanner = new Scanner(System.in);
        List<Tuile> tuiles = TuilesGenerator.genererToutesLesTuiles();
        BotTsuro botTsuro = new BotTsuro(0, 0, 0, null); // Initialisation du bot
        plateau.afficherPlateau();
    
        while (!estFinie()) {
            List<Joueur> joueursAEliminer = new ArrayList<>();
            for (Joueur joueur : joueurs) {
                if (joueur instanceof BotTsuro) {
                    BotTsuro bot = (BotTsuro) joueur;
                    System.out.println(bot.getPrenom() + " (Bot) c'est à votre tour.");
                    Mouvement mouvement = bot.choisirEtAppliquerMouvement(this); // La méthode choisirMouvement doit être adaptée
                    appliquerMouvement(mouvement, bot);
                } else {
                    System.out.println(joueur.getPrenom() + " c'est à votre tour.");
                    // Logique de jeu pour les joueurs humains
                    System.out.println("Entrez les coordonnées de la tuile à placer (ligne colonne) :");
                    int ligneTuile = scanner.nextInt();
                    int colonneTuile = scanner.nextInt();
    
                    // Choix aléatoire d'une tuile
                    Tuile tuile = tuiles.get((int) (Math.random() * tuiles.size()));
    
                    // Placer la tuile sur le plateau
                    plateau.placerTuile(ligneTuile, colonneTuile, tuile, joueur);
                }
    
                plateau.actualiserPosJ(joueur);
                plateau.afficherPlateau();
    
                // Vérifier si le joueur a perdu
                if (plateau.joueurPerdu(joueur)) {
                    System.out.println(joueur.getPrenom() + " a perdu !");
                    joueursAEliminer.add(joueur);
                }
            }
    
            // Retirer les joueurs éliminés après la fin du tour pour éviter les modifications concurrentes
            joueurs.removeAll(joueursAEliminer);
    
            // Condition de sortie de la boucle si le jeu est terminé
            if (estFinie()) {
                break;
            }
        }
    
        scanner.close();
        System.out.println("La partie est terminée. Le gagnant est " + joueurs.get(0).getPrenom() + " !");
    }
    

    public static void main(String[] args) {
        // Création d'une partie avec un plateau de taille 7 et 4 joueurs
        Game game = new Game(7, 4);
        // Lancement de la partie
        game.jouerPartie();
    }
}


package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private PlateauTuiles plateau; // Le plateau de jeu
    private List<Joueur> joueurs; // La liste des joueurs
    // Une liste car on peut ajouter ou supprimer des joueurs

    public Game(int taillePlateau, int nombreJoueurs) {
        plateau = new PlateauTuiles(taillePlateau);
        joueurs = new ArrayList<>();
        for (int i = 0; i < nombreJoueurs; i++) {
            Joueur joueur = new Joueur( "Joueur " + (i + 1));
            joueurs.add(joueur);
        }
    }

    public void jouerPartie() {
        Scanner scanner = new Scanner(System.in);
        List<Tuile> tuiles = TuilesGenerator.genererToutesLesTuiles();
        plateau.afficherPlateau();

        while (joueurs.size() > 1) {
            for (Joueur joueur : joueurs) {
                System.out.println(joueur.getPrenom() + " c'est à votre tour.");

                // Demander au joueur les coordonnées de la tuile à placer
                System.out.println("Entrez les coordonnées de la tuile à placer (ligne colonne) :");
                int ligneTuile = scanner.nextInt();
                int colonneTuile = scanner.nextInt();

                //Une tuile est choisie aléatoirement
                Tuile tuile = tuiles.get((int) (Math.random() * 3) + 1);

                // Placer la tuile sur le plateau
                plateau.placerTuile( tuile, joueur);
                plateau.actualiserPosJ(joueurs);
                plateau.afficherPlateau();

                // Vérifier si le joueur a perdu
                if (plateau.joueurPerdu(joueur)) {
                    System.out.println(joueur.getPrenom() + " a perdu !");
                    joueurs.remove(joueur);
                }
            }
        }

        System.out.println("La partie est terminée. Le gagnant est " + joueurs.get(0).getPrenom() + " !");
    }

    public static void main(String[] args) {
        // Création d'une partie avec un plateau de taille 7 et 4 joueurs
        Game game = new Game(7, 4);
        // Lancement de la partie
        game.jouerPartie();
    }
}


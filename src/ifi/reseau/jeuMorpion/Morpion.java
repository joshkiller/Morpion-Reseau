// La classe Morpion gère la logique du jeu Morpion, y compris les joueurs, la grille et les tours.

package ifi.reseau.jeuMorpion;

import ifi.reseau.interfaceGraphique.Interface; // Import de la classe Interface pour l'interface graphique
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

public class Morpion {
    private final ArrayList<Joueur> listeJoueurs; // Liste des joueurs

    private final String[] grilleMorpion; // La grille de Morpion
    private int nbTour; // Nombre de tours joués

    // Tableau des cas gagnants (combinaisons de positions pour gagner)
    private static final int[][] casGagnants = new int[][] {
            // Lignes horizontales
            {0, 1, 2, 3, 4},
            {5, 6, 7, 8, 9},
            {10, 11, 12, 13, 14},
            {15, 16, 17, 18, 19},
            {20, 21, 22, 23, 24},

            // Lignes verticales
            {0, 5, 10, 15, 20},
            {1, 6, 11, 16, 21},
            {2, 7, 12, 17, 22},
            {3, 8, 13, 18, 23},
            {4, 9, 14, 19, 24},

            // Diagonales principales
            {0, 6, 12, 18, 24},
            {4, 8, 12, 16, 20}
    };

    public Morpion(ArrayList<Joueur> listeJoueurs) {
        this.listeJoueurs = listeJoueurs;
        grilleMorpion = new String[25]; // Grille 5x5 pour le Morpion
        Arrays.fill(grilleMorpion, " "); // Initialisation de la grille avec des espaces vides
        nbTour = 0;
    }

    // Méthode pour augmenter le nombre de tours
    public void incrementerNbTour() {
        nbTour++;
    }

    // Méthode pour obtenir la grille de Morpion
    public String[] getGrilleMorpion() {
        return grilleMorpion;
    }

    // Méthode pour jouer un tour avec un joueur donné (avec l'interface graphique)
    public void jouerInterface(Joueur joueur) {
        if (peutContinuerPartie()) {
            do {
                joueur.setPosition(Interface.getIdCoupJoueur() - 1); // Obtenir la position du coup depuis l'interface
            } while (!estCoupValide(joueur)); // Vérifier si le coup est valide
            ajouterUnCoup(joueur.getPosition(), joueur.getPion()); // Ajouter le coup à la grille
        }
    }

    // Méthode pour ajouter un coup à la grille
    public void ajouterUnCoup(int coup, String pion) {
        grilleMorpion[coup] = pion;
        if (!pion.equals(" ")) {
            incrementerNbTour();
        }
    }

    // Méthode pour vérifier si un coup est valide (dans les limites de la grille et sur une case vide)
    public boolean estCoupValide(Joueur joueur) {
        if (joueur.getPosition() < 0 || joueur.getPosition() > 24) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        if (grilleMorpion[joueur.getPosition()].equals(" ")) {
            return true;
        } else {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    // Méthode pour définir le nombre de tours
    public void setNbTour(int nbTour) {
        this.nbTour = nbTour;
    }

    // Méthode pour vérifier si la partie peut continuer
    public boolean peutContinuerPartie() {
        boolean partieGagnee = partieGagnee(); // Vérifier si la partie est gagnée
        if (!partieGagnee && nbTour < 25) {
            return true;
        }

        if (nbTour >= 25 && !partieGagnee) {
            System.out.print("Egalité : Fin de la partie");
            return false;
        }

        System.out.println("Fin de la partie");
        afficherGagnant();
        return false;
    }

    // Méthode pour afficher le joueur gagnant
    protected void afficherGagnant() {
        for (Joueur joueur : listeJoueurs) {
            if (joueur.isGagnant()) {
                System.out.println("Le gagnant est : " + joueur);
            }
        }
    }

    // Méthode pour vérifier si un joueur a gagné la partie
    private boolean partieGagnee() {
        String piont;
        if (nbTour < 5) {
            return false;
        }
        int cptDePoint;
        for (int i = 0; i < casGagnants.length; i++) {
            cptDePoint = 0;
            piont = grilleMorpion[casGagnants[i][0]];
            if (!piont.equals(" ")) {
                for (int j = 0; j < casGagnants[0].length; j++) {
                    if (grilleMorpion[casGagnants[i][j]].equals(piont)) {
                        cptDePoint++;
                    }
                }
            }
            if (cptDePoint >= 5) {
                for (Joueur listeDesJoueur : listeJoueurs) {
                    if (listeDesJoueur.getPion().equals(piont)) {
                        listeDesJoueur.setGagnant();
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Méthode pour saisir un entier depuis la console
    public static int saisirEntier() {
        Scanner clavier = new Scanner(System.in);
        String s = clavier.nextLine();
        int lu = 456;
        try {
            lu = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            System.err.println("Ce n'est pas un entier valide");
        }
        return lu;
    }

    // Méthode pour obtenir le nombre de tours joués
    public int getNbTour() {
        return nbTour;
    }

    // Méthode pour obtenir la valeur d'une case de la grille
    public String getCaseGrilleMorpion(int i) {
        return grilleMorpion[i];
    }

    // Méthode pour afficher l'état du jeu sous forme de chaîne de caractères
    @Override
    public String toString() {
        StringBuilder affichage = new StringBuilder("Tour n°" + nbTour + "\n Ton profil : " + listeJoueurs.get(0) + "\n Adversaire : " + listeJoueurs.get(1) + "\n");
        for (int i = 0; i < 25; i += 5) {
            for (int j = 0; j < 5; j++) {
                affichage.append("|").append(grilleMorpion[i + j]);
            }
            affichage.append("|\n");
        }
        return affichage.toString();
    }
}

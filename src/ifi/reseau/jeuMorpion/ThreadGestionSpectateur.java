package ifi.reseau.jeuMorpion;

import ifi.reseau.protocole.Client;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ThreadGestionSpectateur implements Runnable {
    private final Socket socketSpectateur;
    private PrintStream sortieServSpec;
    private final Morpion morpion;
    private final Joueur joueurClient;
    private final Joueur joueurServeur;

    public ThreadGestionSpectateur(Socket socketSpectateur, Morpion morpion, Joueur joueurServeur, Joueur joueurClient) {
        // Initialisation des paramètres du thread
        this.morpion = morpion;
        this.joueurClient = joueurClient;
        this.joueurServeur = joueurServeur;
        this.socketSpectateur = socketSpectateur;

        try {
            // Initialisation de la sortie pour communiquer avec le spectateur
            sortieServSpec = new PrintStream(new BufferedOutputStream(socketSpectateur.getOutputStream()));
            System.out.println("On écoute le spectateur sur le " + socketSpectateur);

            // Envoi de la grille au spectateur
            MorpionReseau.pushGrille(morpion, sortieServSpec);

            // Envoi des informations sur les joueurs au spectateur
            MorpionReseau.pushInfoJoueurAuSpectateur(joueurServeur, joueurClient, sortieServSpec);

            // Envoi du numéro du tour actuel
            Client.push(String.valueOf(morpion.getNbTour()), sortieServSpec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            boolean serveurLu = false;
            boolean clientLu = false;
            while (morpion.peutContinuerPartie()) {
                Thread.sleep(100);
                if (joueurClient.getHasPlayed() && !clientLu) {
                    // Quand le client a joué et que cela n'a pas encore été signalé
                    clientLu = true;
                    serveurLu = false;

                    // Envoi de la grille et de l'état de la partie au spectateur
                    MorpionReseau.pushGrille(morpion, sortieServSpec);
                    MorpionReseau.pushEtatPartieAuSpectateur(morpion, sortieServSpec);
                }

                // Doublon des deux fonctions, elles ne servent qu'à synchroniser finalement
                else if (joueurServeur.getHasPlayed() && !serveurLu) {
                    clientLu = false;
                    serveurLu = true;

                    // Envoi de la grille et de l'état de la partie au spectateur
                    MorpionReseau.pushGrille(morpion, sortieServSpec);
                    MorpionReseau.pushEtatPartieAuSpectateur(morpion, sortieServSpec);
                }
            }

            // Informe le spectateur de la fin de la partie
            Client.push("FIN", sortieServSpec);
            socketSpectateur.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

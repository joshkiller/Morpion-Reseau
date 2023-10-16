package ifi.reseau.jeuMorpion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServeurEcouteSpectateur extends Thread {

    private Morpion morpion;
    private Joueur jClient;
    private Joueur jServeur;

    @Override
    public void run() {
        try {
            ServerSocket s_ecoute = new ServerSocket(2001);

            while (morpion.peutContinuerPartie()) {
                Socket spectateur = s_ecoute.accept();
                System.out.println("Connexion d'un nouveau spectateur...");

                // Création d'un thread pour gérer la communication avec le spectateur
                Thread t = new Thread(new ThreadGestionSpectateur(spectateur, morpion, jServeur, jClient));
                t.start();
                Thread.sleep(10); // Délai avant de traiter la prochaine connexion
            }

            s_ecoute.close(); // Fermeture du socket d'écoute après la fin de la partie
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setMorpion(Morpion morpion) {
        this.morpion = morpion;
    }

    public void setjClient(Joueur jClient) {
        this.jClient = jClient;
    }

    public void setjServeur(Joueur jServeur) {
        this.jServeur = jServeur;
    }
}

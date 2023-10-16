// La classe MorpionReseau gère la partie réseau du jeu Morpion, y compris les interactions avec les clients et les spectateurs.

package ifi.reseau.jeuMorpion;

import ifi.reseau.protocole.Client;
import ifi.reseau.protocole.Serveur;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MorpionReseau {

    // Méthode pour gérer le jeu côté client
    public static void morpionClient(Joueur joueurServeur, Joueur joueurClient, Morpion morpion) {
        Socket socket;
        String ip = "localhost";
        String ipNew = "";
        try {
            saisirInfo(joueurClient); // On renseigne le nom et le pion de son joueur, en mettant à jour le morpion
            while (true) {
                System.out.println("Entrer l'IP du serveur:");
                Scanner sc = new Scanner(System.in);
                ipNew = sc.nextLine();
                if (ipNew.isEmpty()) {
                    ipNew = ip;
                }
                try {
                    InetAddress.getByName(ipNew);
                    System.out.println("IP valide");
                    break;
                } catch (UnknownHostException ex) {
                    System.out.println("Adresse IP non valide");
                }
            }
            System.out.println("Connexion en cours...");
            socket = Client.getSocket(ipNew, 2000);
            DataInputStream socketEntree = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            PrintStream socketSortie = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
            System.out.println("Connexion réussie...");
            pushInfoJoueur(joueurClient, socketSortie); // On envoie nos infos au serveur
            System.out.println("Attente des infos du serveur...");
            pullInfoJoueur(joueurServeur, socketEntree); // On met à jour le morpion avec les infos du serveur
            while (morpion.peutContinuerPartie()) {
                System.out.println("À votre tour " + joueurClient.getNom() + "!");
                jouerTour(morpion, joueurClient); // On joue et met à jour le morpion local (client)
                pushCoup(joueurClient, socketSortie); // On envoie le coup du client
                System.out.println("Au tour de " + joueurServeur.getNom() + "...");
                pullCoup(joueurServeur, morpion, socketEntree); // On reçoit le coup du serveur et on met à jour le morpion
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Le client n'a pas pu se connecter");
        }
    }

    // Méthode pour gérer le jeu côté spectateur
    public static void morpionSpectateur(Joueur joueurServeur, Joueur joueurClient, Morpion morpion) {
        Socket socket; // On essaie de se connecter à un serveur local
        try {
            socket = Client.getSocket("localhost", 2001);
            DataInputStream socketEntree = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            pullGrille(morpion, socketEntree);
            pullInfoJoueur(joueurServeur, socketEntree);
            pullInfoJoueur(joueurClient, socketEntree);
            System.out.println("Vous observez une partie se jouant entre " + joueurServeur.getNom() + " et " + joueurClient.getNom());
            morpion.setNbTour(Integer.parseInt(Client.pull(socketEntree)));
            System.out.println("Vous êtes au tour n°" + morpion.getNbTour());
            while (morpion.peutContinuerPartie()) {
                String message = Client.pull(socketEntree);
                if (message.equals("FIN")) // Permet à tout moment d'arrêter la partie à partir de l'impulsion du serveur
                    break;
                remplirGrille(morpion, message); // On remplit la grille totalement à chaque fois
                morpion.incrementerNbTour();
                System.out.println(morpion);
            }
            Client.pull(socketEntree);
            System.out.println("Partie terminée");
            morpion.afficherGagnant();
            socket.close();
        } catch (IOException e) {
            System.err.println("Le spectateur n'a pas pu se connecter");
        }
    }

    // Méthode pour gérer le jeu côté serveur
    public static void morpionServeur(Joueur joueurServeur, Joueur joueurClient, Morpion morpion) {
        try {
            saisirInfo(joueurServeur);
            Socket socketClient = Serveur.initialisationServeur(2000);
            ThreadServeurEcouteSpectateur threadServeurEcouteSpectateur = new ThreadServeurEcouteSpectateur();
            threadServeurEcouteSpectateur.setMorpion(morpion);
            threadServeurEcouteSpectateur.setjClient(joueurClient);
            threadServeurEcouteSpectateur.setjServeur(joueurServeur);
            threadServeurEcouteSpectateur.start();
            DataInputStream entreeServJoueur = new DataInputStream(new BufferedInputStream(socketClient.getInputStream()));
            PrintStream sortieServJoueur = new PrintStream(new BufferedOutputStream(socketClient.getOutputStream()));
            pullInfoJoueur(joueurClient, entreeServJoueur);
            while (joueurClient.getPion().equals(joueurServeur.getPion())) {
                System.out.println("Change de pion s'il te plaît!");
                saisirInfo(joueurServeur);
            }
            pushInfoJoueur(joueurServeur, sortieServJoueur);
            while (morpion.peutContinuerPartie()) {
                System.out.println("En attente de " + joueurClient.getNom());
                pullCoup(joueurClient, morpion, entreeServJoueur);
                joueurServeur.setHasPlayed(false);
                joueurClient.setHasPlayed(true);
                jouerTour(morpion, joueurServeur);
                pushCoup(joueurServeur, sortieServJoueur);
                joueurClient.setHasPlayed(false);
                joueurServeur.setHasPlayed(true);
            }
            socketClient.close();
            threadServeurEcouteSpectateur.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour pousser l'état de la partie au spectateur
    public static void pushEtatPartieAuSpectateur(Morpion morpion, PrintStream sortieServSpec) {
        if (!morpion.peutContinuerPartie())
            Client.push("FIN", sortieServSpec);
    }

    // Méthode pour jouer un tour
    private static void jouerTour(Morpion morpion, Joueur joueur) {
        System.out.println(morpion);
        morpion.jouerInterface(joueur);
        System.out.println(morpion);
    }

    // Méthode pour remplir la grille avec les données reçues
    private static void remplirGrille(Morpion morpion, String grille) {
        morpion.setNbTour(0);
        for (int i = 0; i < grille.length(); i++) {
            morpion.ajouterUnCoup(i, String.valueOf(grille.charAt(i)));
        }
    }

    // Méthode pour pousser les informations des joueurs au spectateur
    public static void pushInfoJoueurAuSpectateur(Joueur joueurServeur, Joueur joueurClient, PrintStream sortieServSpec) {
        pushInfoJoueur(joueurServeur, sortieServSpec);
        pushInfoJoueur(joueurClient, sortieServSpec);
    }

    // Méthode pour extraire la grille de jeu du flux d'entrée du spectateur
    private static void pullGrille(Morpion morpion, DataInputStream entreeSpec) {
        try {
            String grille = Client.pull(entreeSpec);
            for (int i = 0; i < 25; i++) {
                morpion.ajouterUnCoup(i, String.valueOf(grille.charAt(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour extraire le coup d'un adversaire du flux d'entrée du serveur
    public static void pullCoup(Joueur adversaire, Morpion morpion, DataInputStream socketEntree) throws IOException {
        adversaire.setPosition(Integer.parseInt(Client.pull(socketEntree)));
        morpion.ajouterUnCoup(adversaire.getPosition(), adversaire.getPion());
    }

    // Méthode pour pousser l'état de la grille au spectateur
    public static void pushGrille(Morpion morpion, PrintStream sortieServSpec) {
        StringBuilder aEnvoyer = new StringBuilder();
        for (int i = 0; i < 25; i++) {
            aEnvoyer.append(morpion.getCaseGrilleMorpion(i));
        }
        Client.push(aEnvoyer.toString(), sortieServSpec);
    }

    // Méthode pour pousser un coup au serveur
    public static void pushCoup(Joueur j, PrintStream socketSortie) {
        Client.push(String.valueOf(j.getPosition()), socketSortie);
    }

    // Méthode pour saisir les informations du joueur
    public static void saisirInfo(Joueur j) {
        Scanner saisieJoueur = new Scanner(System.in);
        saisirNomJoueur(saisieJoueur, j);
        saisirPion(saisieJoueur, j);
    }

    // Méthode pour saisir le nom du joueur
    public static void saisirNomJoueur(Scanner saisie, Joueur j) {
        System.out.print("Ton nom : \n");
        String nomJoueur = saisie.nextLine();
        j.setNom(nomJoueur);
    }

    // Méthode pour saisir le pion du joueur
    public static void saisirPion(Scanner saisie, Joueur j) {
        System.out.print("Ton pion : \n");
        String pion = saisie.nextLine();
        j.setPion(pion);
    }

    // Méthode pour pousser les informations du joueur au serveur
    public static void pushInfoJoueur(Joueur j1, PrintStream socketSortie) {
        // Envoie des données du joueur
        String pack = j1.getNom() + " " + j1.getPion();
        Client.push(pack, socketSortie);
    }

    // Méthode pour extraire les informations du joueur du flux d'entrée du serveur
    public static void pullInfoJoueur(Joueur adversaire, DataInputStream entreeServ) throws IOException {
        String packRecu = Client.pull(entreeServ);
        String[] infoAdversaire = packRecu.split(" ");
        adversaire.setNom(infoAdversaire[0]);
        adversaire.setPion(infoAdversaire[1]);
    }
}

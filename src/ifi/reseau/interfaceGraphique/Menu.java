/*
package ifi.reseau.interfaceGraphique;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    private JButton btn_jouer;
    private JButton btn_regarder;
    private JButton btn_heberger;
    private JButton btn_quitter;

    public Menu() {
        setTitle("MENU");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 350);

        */
/* Initialisation de boutons *//*

        btn_jouer = new JButton("Jouer");
        btn_regarder = new JButton("Regarder");
        btn_heberger = new JButton("Heberger");
        btn_quitter = new JButton("Quitter");

        */
/* Création de panel *//*

        JPanel panel = new JPanel(new GridBagLayout());

        */
/* Create GridBagConstraints to control the layout *//*

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL; // Remplissage horizontal

        */
/* Add the "Serveur" button to the panel *//*

        constraints.gridx = 0; // Colonne 0
        constraints.gridy = 1; // Ligne 1
        constraints.gridwidth = 1; // Occupe 1 colonne
        constraints.insets = new Insets(5, 5, 5, 5); // Ajoute de l'espace (5 pixels) autour du bouton
        panel.add(btn_jouer, constraints);

        */
/* Add the "Client" button to the panel *//*

        constraints.gridx = 0; // Colonne 0
        constraints.gridy = 2; // Ligne 2
        constraints.gridwidth = 1; // Occupe 1 colonne
        constraints.insets = new Insets(5, 5, 5, 5); // Réinitialise les marges pour le bouton suivant
        panel.add(btn_regarder, constraints);

        */
/* Add the "Client" button to the panel *//*

        constraints.gridx = 0; // Colonne 0
        constraints.gridy = 3; // Ligne 3
        constraints.gridwidth = 1; // Occupe 1 colonne
        constraints.insets = new Insets(5, 5, 5, 5); // Réinitialise les marges pour le bouton suivant
        panel.add(btn_heberger, constraints);

        */
/* Add the "Client" button to the panel *//*

        constraints.gridx = 0; // Colonne 0
        constraints.gridy = 4; // Ligne 4
        constraints.gridwidth = 1; // Occupe 1 colonne
        constraints.insets = new Insets(5, 5, 5, 5); // Réinitialise les marges pour le bouton suivant
        panel.add(btn_quitter, constraints);


        */
/* Add the panel to the frame *//*

        add(panel);

        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
        setVisible(true); // Affiche la fenetre

        */
/* Add action listener to button server *//*

        btn_jouer.addActionListener(e -> {
            System.out.println("jouer");
            Panneau.JouerFrame jouerFrame = new Panneau.JouerFrame();
            dispose();
        });

        btn_regarder.addActionListener(e -> {
            System.out.println("regarder");
            dispose();
        });

        btn_heberger.addActionListener(e -> {
            System.out.println("heberger");
            dispose();
        });

        btn_quitter.addActionListener(e -> {
            System.out.println("quitter");
            dispose();
        });
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.setVisible(true);
    }
}
*/

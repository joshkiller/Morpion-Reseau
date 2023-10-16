// Importation des packages nécessaires
package ifi.reseau.interfaceGraphique;
import ifi.reseau.jeuMorpion.Morpion;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

// Définition de la classe Panneau, qui étend JPanel
public class Panneau extends JPanel {

    // Variable membre pour stocker l'état du tableau de Morpion
    private static String[] tableauMorpion;

    // Constructeur de la classe Panneau
    public Panneau() {
        tableauMorpion = new String[25]; // Ajuster la taille pour une grille de 5x5
        Arrays.fill(tableauMorpion, " "); // Initialiser le tableau avec des espaces vides
    }

    // Méthode pour dessiner le composant graphique
    public void paintComponent(Graphics g) {
        int id = 0;
        g.clearRect(0, 0, this.getWidth(), this.getHeight()); // Effacer le contenu précédent

        int cellWidth = this.getWidth() / 5;
        int cellHeight = this.getHeight() / 5;

        // Parcourir les lignes et colonnes pour dessiner la grille
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                id++;
                if ((i + j) % 2 == 0) {
                    g.setColor(new Color(139, 69, 19)); // Couleur café (marron) pour les cellules impaires
                } else {
                    g.setColor(Color.WHITE); // Couleur pour les cellules paires
                }

                g.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);

                g.setColor(Color.BLACK); // Rétablir la couleur du contour en noir
                g.drawRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                g.drawString(String.valueOf(id), 10 + i * cellWidth, 20 + j * cellHeight); // Afficher l'ID de la cellule
            }
        }

        Font font = new Font("Courier", Font.BOLD, 80);
        g.setFont(font);

        // Parcourir les cases du Morpion et afficher les symboles X, O ou des espaces vides
        for (int i = 0; i < 25; i++) {
            if (tableauMorpion[i].equals("X")) {
                g.setColor(Color.RED); // Couleur rouge pour les "X"
            } else if (tableauMorpion[i].equals("O")) {
                g.setColor(Color.GREEN); // Couleur verte pour les "O"
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawString(tableauMorpion[i], 50 + (i % 5) * cellWidth, 50 + (i / 5) * cellHeight);
        }
    }

    // Méthode pour mettre à jour le tableau de Morpion et redessiner le composant
    public void afficherPion(int id, String apparence) {
        tableauMorpion[id - 1] = apparence;
        repaint(); // Redessiner le composant
    }
}

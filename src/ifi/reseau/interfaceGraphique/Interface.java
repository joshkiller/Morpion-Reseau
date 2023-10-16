// Importation des packages nécessaires
package ifi.reseau.interfaceGraphique;
import ifi.reseau.jeuMorpion.Morpion;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Définition de la classe Interface, qui étend JFrame
public class Interface extends JFrame {

    // Variables membres
    private static int idCoupJoueur;
    private JPanel contentPane;
    private Morpion morpion;

    // Méthode principale (point d'entrée de l'application)
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Création de l'objet Interface
                    Interface frame = new Interface(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Accesseurs pour l'ID du coup du joueur
    public static void setIdCoupJoueur(int idC2) {
        idCoupJoueur = idC2;
    }

    public static int getIdCoupJoueur() {
        return idCoupJoueur;
    }

    // Constructeur de la classe Interface
    public Interface(Morpion morpion) {
        this.morpion = morpion;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 800); // Ajustement de la taille de la fenêtre
        this.setTitle("Morpion");
        this.setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Création d'un objet Panneau
        Panneau panneau = new Panneau();
        this.setContentPane(panneau);
        final int[] id = new int[1];
        int largeur = this.getWidth();
        int hauteur = this.getHeight();

        // Gestionnaire d'événements pour le clic de la souris
        panneau.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int hoveredRow = -1;
                int hoveredCol = -1;

                int cellWidth = largeur / 5;
                int cellHeight = hauteur / 5;

                int colonne = e.getX() / cellWidth;
                int ligne = e.getY() / cellHeight;

                // Calcul de l'ID de la cellule en fonction de la ligne et de la colonne
                id[0] = (ligne * 5) + colonne + 1;

                // Mise à jour des variables de survol
                hoveredRow = ligne;
                hoveredCol = colonne;

                System.out.println(id[0]);
                Interface.setIdCoupJoueur(id[0]);
                for (int i = 1; i <= morpion.getGrilleMorpion().length; i++) {
                    panneau.afficherPion(i, morpion.getCaseGrilleMorpion(i - 1));
                }
                panneau.revalidate();
                panneau.repaint();
            }
        });

        // Gestionnaire d'événements pour le mouvement de la souris
        panneau.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (int i = 1; i <= morpion.getGrilleMorpion().length; i++) {
                    panneau.afficherPion(i, morpion.getCaseGrilleMorpion(i - 1));
                }
                panneau.revalidate();
                panneau.repaint();
            }
        });
    }
}

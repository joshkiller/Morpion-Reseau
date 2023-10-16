// Classe Joueur pour représenter un joueur du jeu Morpion
package ifi.reseau.jeuMorpion;

public class Joueur {
    private String nom; // Nom du joueur
    private String ip; // Adresse IP du joueur (peut être utile pour la partie réseau)
    private String pion; // Symbole (X ou O) du joueur
    private boolean gagnant; // Indique si le joueur est le gagnant de la partie
    private int positionJ; // Position du joueur (éventuellement utilisée pour une grille)
    private boolean hasPlayed; // Indique si le joueur a déjà joué son tour dans une partie

    // Constructeur de la classe Joueur
    public Joueur(String nom, String ip, String pion) {
        this.nom = nom;
        this.ip = ip;
        this.pion = pion;
        hasPlayed = false;
        gagnant = false;
    }

    // Méthode pour obtenir le symbole (X ou O) du joueur
    public String getPion() {
        return pion;
    }

    // Méthode pour marquer le joueur comme gagnant
    void setGagnant() {
        this.gagnant = true;
    }

    // Méthode pour vérifier si le joueur est le gagnant de la partie
    public boolean isGagnant() {
        return gagnant;
    }

    // Méthode pour obtenir la position du joueur (peut être utilisée pour une grille)
    public int getPosition() {
        return positionJ;
    }

    // Méthode pour définir la position du joueur
    public void setPosition(int positionJ) {
        this.positionJ = positionJ;
    }

    // Méthode pour indiquer si le joueur a joué son tour
    public void setHasPlayed(boolean cTonTour) {
        this.hasPlayed = cTonTour;
    }

    // Méthode pour vérifier si le joueur a déjà joué son tour
    public boolean getHasPlayed() {
        return hasPlayed;
    }

    // Méthode pour définir le symbole (X ou O) du joueur
    public void setPion(String piont) {
        if (!piont.equals("") && !piont.equals(" ") && piont.length() == 1) {
            this.pion = piont;
        }
    }

    // Méthode pour définir l'adresse IP du joueur
    public void setIp(String ip) {
        this.ip = ip;
    }

    // Méthode pour obtenir le nom du joueur
    public String getNom() {
        return nom;
    }

    // Méthode pour définir le nom du joueur (sans espaces et non vide)
    public void setNom(String nom) {
        if (!nom.contains(" ") && !nom.equals("")) {
            this.nom = nom;
        }
    }

    // Méthode pour afficher le nom et le symbole du joueur
    @Override
    public String toString() {
        return nom + " " + pion;
    }
}

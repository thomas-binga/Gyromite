package modele.plateau;

public class Colonne extends EntiteDynamique {
    public Colonne(Jeu _jeu, char couleur) {
        super(_jeu);
        this.couleur=couleur;
    }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
    public boolean peutEcraser() {return true;}
    public boolean continuer = true;
    public char couleur = 'r';

    @Override
    public boolean peutEtreTraverse() {
        return false;
    }
}

package modele.plateau;

public class Echelle extends EntiteStatique{
    public Echelle(Jeu _jeu) {
        super(_jeu);

    }
    @Override
    public boolean peutPermettreDeMonterDescendre() {
        return true;
    }
    public boolean peutEtreTraverse(){return true;};
}

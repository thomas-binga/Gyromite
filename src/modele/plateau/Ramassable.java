package modele.plateau;

public class Ramassable extends EntiteStatique{
    public Ramassable(Jeu _jeu) {
        super(_jeu);
    }
    @Override
    public boolean peutServirDeSupport(){
        return false;
    }
}

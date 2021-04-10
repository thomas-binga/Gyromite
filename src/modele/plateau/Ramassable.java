package modele.plateau;

public class Ramassable extends EntiteStatique{
    private static int cptTotalBombes = 0;
    public Ramassable(Jeu _jeu) {
        super(_jeu);
        cptTotalBombes++;
    }
    @Override
    public boolean peutServirDeSupport(){
        return false;
    }
    public boolean peutEtreTraverse(){return true;};

    public static int getTotalBombes() {
        return cptTotalBombes;
    }

}

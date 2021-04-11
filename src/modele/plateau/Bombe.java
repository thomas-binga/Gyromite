package modele.plateau;

public class Bombe extends Ramassable{
    private static int cptTotalBombes = 0;
    public Bombe(Jeu _jeu) {
        super(_jeu);
        cptTotalBombes++;
    }
    public static int getTotalBombes() {
        return cptTotalBombes;
    }
}

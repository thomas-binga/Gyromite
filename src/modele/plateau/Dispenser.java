package modele.plateau;

public class Dispenser extends EntiteStatique{
    public char sens;
    public Dispenser(Jeu _jeu, char sens) {
        super(_jeu);
        this.sens = sens;
    }
}

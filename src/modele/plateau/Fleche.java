package modele.plateau;

import javafx.geometry.Point3D;

import java.util.Random;

public class Fleche extends EntiteDynamique{
    public char sens;
    public Point3D dispPos;
    private Random r = new Random();
    public Fleche(Jeu _jeu, char sens, Point3D dispPos) {
        super(_jeu);
        this.sens = sens;
        this.dispPos = dispPos;
    }

    @Override
    public boolean peutEtreEcrase() {
        return false;
    }

    @Override
    public boolean peutServirDeSupport() {
        return false;
    }

    @Override
    public boolean peutPermettreDeMonterDescendre() {
        return false;
    }

    @Override
    public boolean peutEtreTraverse() {
        return false;
    }

}

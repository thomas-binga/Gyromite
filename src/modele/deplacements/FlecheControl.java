package modele.deplacements;

import modele.plateau.Colonne;
import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.Mur;

public class FlecheControl extends RealisateurDeDeplacement{
    private Direction directionCourante;
    // Design pattern singleton
    private static FlecheControl c3d;

    public static FlecheControl getInstance() {
        if (c3d == null) {
            c3d = new FlecheControl();
        }
        return c3d;
    }

    public static double getTimer() {
        return Math.random()*20;
    }

    public void setDirectionCourante(Direction d) {
        directionCourante = d;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
                if (directionCourante != null)
                    switch (directionCourante) {
                        case gauche:
                            Entite eGauche = e.regarderDansLaDirection(Direction.gauche);
//                        if (eHaut == null || eHaut instanceof Colonne){
//                            if (e.avancerDirectionChoisie(Direction.haut)){
//                                ret = true;
                            if(e.avancerDirectionChoisie(Direction.gauche)) ret=true;
                                break;
                        case droite:
                            Entite eDroite = e.regarderDansLaDirection(Direction.droite);
                            if(e.avancerDirectionChoisie(Direction.droite)) ret=true;
                            break;
                    }
        }

        return ret;

    }

    public void resetDirection() {
        directionCourante = null;
    }
}

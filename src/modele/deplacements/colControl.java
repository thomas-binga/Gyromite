package modele.deplacements;

import modele.plateau.Colonne;
import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.Mur;

public class colControl extends RealisateurDeDeplacement{
    private Direction directionCourante;
    // Design pattern singleton
    private static colControl c3d;

    public static colControl getInstance() {
        if (c3d == null) {
            c3d = new colControl();
        }
        return c3d;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null)
                switch (directionCourante) {
                    case haut:  Entite eHaut = e.regarderDansLaDirection(Direction.haut);
//                        if (eHaut == null || eHaut instanceof Colonne){
//                            if (e.avancerDirectionChoisie(Direction.haut)){
//                                ret = true;

                        if (!(eHaut instanceof Mur)){
                            if(e.avancerDirectionChoisie(Direction.haut)) {
                                ret = true;
                            } break;
                        }
                    case bas:
                        Entite eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas == null || eBas.peutEtreEcrase()){
                            if(e.avancerDirectionChoisie(Direction.bas)) {
                                ret = true;
                            } break;
                        }}
        }

        return ret;

    }

    public void resetDirection() {
        directionCourante = null;
    }

}

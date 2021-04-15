package modele.deplacements;

import modele.plateau.Colonne;
import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.Mur;

public class colControl extends RealisateurDeDeplacement{
    private Direction directionCourante_r;
    private Direction directionCourante_b;
    // Design pattern singleton
    private static colControl c3d;

    public static colControl getInstance() {
        if (c3d == null) {
            c3d = new colControl();
        }
        return c3d;
    }

    public void setDirectionCourante(char couleur) {
        if (couleur == 'r'){
            if(directionCourante_r == Direction.haut) directionCourante_r = Direction.bas;
            else directionCourante_r = Direction.haut;
        }
        else if (couleur == 'b'){
            if(directionCourante_b == Direction.haut) directionCourante_b = Direction.bas;
            else directionCourante_b = Direction.haut;
        }
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (e instanceof  Colonne && ((Colonne) e).couleur=='r') {
                if (directionCourante_r != null)
                    switch (directionCourante_r) {
                        case haut:
                            Entite eHaut = e.regarderDansLaDirection(Direction.haut);
//                        if (eHaut == null || eHaut instanceof Colonne){
//                            if (e.avancerDirectionChoisie(Direction.haut)){
//                                ret = true;

//                            if (!(eHaut instanceof Mur)) {
//                                if (e.avancerDirectionChoisie(Direction.haut)) {
//                                    ret = true;
//                                }
//                                break;
//                            }
                            if (e.avancerDirectionChoisie(Direction.haut)) {
                                ret = true;
                            }
                            break;
                        case bas:
                            Entite eBas = e.regarderDansLaDirection(Direction.bas);
                            if (eBas == null || eBas.peutEtreEcrase()) {
                                if (e.avancerDirectionChoisie(Direction.bas)) {
                                    ret = true;
                                }
                                break;
                            }
                    }
            }
            else{
                if (directionCourante_b != null)
                    switch (directionCourante_b) {
                        case haut:
                            Entite eHaut = e.regarderDansLaDirection(Direction.haut);
//                        if (eHaut == null || eHaut instanceof Colonne){
//                            if (e.avancerDirectionChoisie(Direction.haut)){
//                                ret = true;

                            if (!(eHaut instanceof Mur)) {
                                if (e.avancerDirectionChoisie(Direction.haut)) {
                                    ret = true;
                                }
                                break;
                            }
                        case bas:
                            Entite eBas = e.regarderDansLaDirection(Direction.bas);
                            if (eBas == null || eBas.peutEtreEcrase()) {
                                if (e.avancerDirectionChoisie(Direction.bas)) {
                                    ret = true;
                                }
                                break;
                            }
                    }
            }
        }

        return ret;

    }

    public void resetDirection() {
        directionCourante_r = null;
        directionCourante_b=null;
    }

}

package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class IA extends RealisateurDeDeplacement {
    private Direction directionCourante;
    // Design pattern singleton
    private static IA c3d;

    public static IA getInstance() {
        if (c3d == null) {
            c3d = new IA();
        }
        return c3d;
    }

    public void setDirectionCourante() {
        double rand = 0 + (Math.random() * (15));

        if(rand <2 ){
            directionCourante = Direction.bas;
//            System.out.println("Le robot va en bas !");
        }
        else if(rand < 4){
            directionCourante = Direction.haut;
//            System.out.println("Le robot va en haut !");
        }
        else if(rand < 7){
            directionCourante = Direction.gauche;
//            System.out.println("Le robot va à gauche !");
        }
        else if(rand < 10){
            directionCourante = Direction.droite;
//            System.out.println("Le robot va à droite !");
        }

    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        if(directionCourante == null) {

            setDirectionCourante();}

        for (EntiteDynamique e : lstEntitesDynamiques) {
            if(e.vivant && directionCourante!=null)
            {
                switch (directionCourante) {
                    case haut:Entite eHaut = e.regarderDansLaDirection(Direction.haut);
                        if(eHaut ==null || eHaut.peutPermettreDeMonterDescendre() || eHaut.peutEtreEcrase()){
                            if( (e.regarderDansLaDirection(Direction.haut) != null && e.regarderDansLaDirection(Direction.haut).peutServirDeSupport()))
                            {
                                if ((directionCourante!=null) && e.avancerDirectionChoisie(directionCourante))
                                    ret = true;
                            }
                        }
                        break;

                    case bas: Entite eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas == null|| eBas.peutPermettreDeMonterDescendre() || eBas.peutEtreEcrase()) {
                            if (e.avancerDirectionChoisie(Direction.bas)){
                                ret = true;}
                        }   break;
                    case gauche:
                        Entite eGauche = e.regarderDansLaDirection(Direction.gauche);
                        if (eGauche == null || eGauche.peutPermettreDeMonterDescendre() || eGauche.peutEtreEcrase()) {
                            if (e.avancerDirectionChoisie(Direction.gauche)){
                                ret = true;}
                            else {e.avancerDirectionChoisie(Direction.droite);}}
                        break;

                    case droite:
                        Entite eDroite = e.regarderDansLaDirection(Direction.droite);
                        if (eDroite == null  || eDroite.peutPermettreDeMonterDescendre()|| eDroite.peutEtreEcrase()) {
                            if (e.avancerDirectionChoisie(Direction.droite)){
                                ret = true;}
                            else {e.avancerDirectionChoisie(Direction.gauche);}}
                        break;
                }}
        }
        return ret;}


    public void resetDirection() {
        directionCourante = null;
    }

    // fonction deplacement / Collision
}

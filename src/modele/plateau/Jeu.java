/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.*;

import javafx.geometry.Point3D;
//import java.awt.Point;
import java.util.HashMap;

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu<Integer> {

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 11;
    public static final int SIZE_Z = 2;
    public boolean end = false;
    public boolean win = false;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private final HashMap<Entite, java.lang.Integer> cmptDeplH = new HashMap<Entite, java.lang.Integer>();
    private final HashMap<Entite, java.lang.Integer> cmptDeplV = new HashMap<Entite, java.lang.Integer>();

    private Heros hector;
    private Bot zombie;


    private final HashMap<Entite, Point3D> map = new  HashMap<Entite, Point3D>(); // permet de récupérer la position d'une entité à partir de sa référence
    private final Entite[][][] grilleEntites = new Entite[SIZE_X][SIZE_Y][SIZE_Z]; // permet de récupérer une entité à partir de ses coordonnées

    private final Ordonnanceur ordonnanceur =  new Ordonnanceur( this);

    public int cptBombe = 0;
    public int score = 0;

    public boolean herosSurEchelle =false;
    public boolean botSurEchelle = false;
    public boolean botSurBombe = false;
    public boolean botSurBonus = false;

    public float herosRegardeGauche = 0;
    public float herosRegardeDroite = 0;
    public float botRegardeGauche = 0;
    public float botRegardeDroite = 0;
    public double fleche1Timer = FlecheControl.getTimer();


    public Jeu() {
        initialisationDesEntites();
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public Entite[][][] getGrille() {
        return grilleEntites;
    }
    
    public Heros getHector() {
        return hector;
    }
    public Bot getZombie(){return zombie;}
    
    private void initialisationDesEntites() {
        hector = new Heros(this);
        addEntite(hector, 2, 1, 0);
        zombie = new Bot(this);
        addEntite(zombie, 13,3,0);

        Colonne c1_cube1 = new Colonne(this, 'r');
        addEntite(c1_cube1, 6,8,0);
        Colonne c1_cube2 = new Colonne(this, 'r');
        addEntite(c1_cube2, 6,7,0);
        Colonne c1_cube3 = new Colonne(this, 'r');
        addEntite(c1_cube3, 6,6,0);
        Colonne c2_cube1 = new Colonne(this, 'b');
        addEntite(c2_cube1, 15,1,0);
        Colonne c2_cube2 = new Colonne(this, 'b');
        addEntite(c2_cube2, 15,2,0);
        Colonne c2_cube3 = new Colonne(this, 'b');
        addEntite(c2_cube3, 15,3,0);
        Colonne c2_cube4 = new Colonne(this, 'b');
        addEntite(c2_cube4, 15,4,0);

        Dispenser disp1 = new Dispenser(this, 'd');
        addEntite(disp1, 1,2,1);
        Fleche fleche1 = new Fleche(this, 'd', new Point3D(1,2,0));
        addEntite(fleche1, 1,2,0);




        Gravite g = new Gravite();
        g.addEntiteDynamique(hector);
        g.addEntiteDynamique(zombie);
        ordonnanceur.add(g);

        Controle4Directions.getInstance().addEntiteDynamique(hector);
        IA.getInstance().addEntiteDynamique(zombie);

        colControl.getInstance().addEntiteDynamique(c1_cube1);
        colControl.getInstance().addEntiteDynamique(c1_cube2);
        colControl.getInstance().addEntiteDynamique(c1_cube3);
        colControl.getInstance().addEntiteDynamique(c2_cube1);
        colControl.getInstance().addEntiteDynamique(c2_cube2);
        colControl.getInstance().addEntiteDynamique(c2_cube3);
        colControl.getInstance().addEntiteDynamique(c2_cube4);

        FlecheControl.getInstance().addEntiteDynamique(fleche1);

        ordonnanceur.add(Controle4Directions.getInstance());
        ordonnanceur.add(IA.getInstance());
        ordonnanceur.add(colControl.getInstance());
        ordonnanceur.add(FlecheControl.getInstance());

        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addEntite(new Mur(this), x, 0,0);
            addEntite(new Mur(this), x, 9,0);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntite(new Mur(this), 0, y,0);
            addEntite(new Mur(this), 19, y,0);
        }

        addEntite(new Mur(this), 2, 6,0);
        addEntite(new Mur(this), 3, 6,0);

        addEntite(new Mur(this), 9, 6,0);
        addEntite(new Mur(this), 10, 6,0);

        addEntite(new Mur(this), 12, 4,0);
        addEntite(new Mur(this), 13, 4,0);
        addEntite(new Mur(this), 14, 4,0);

        addEntite(new Bombe(this),7,8,1);
        addEntite(new Bombe(this),14,3,1);

        addEntite(new Bonus(this), 16,8,1);

        addEntite(new Echelle(this),8,8,1);
        addEntite(new Echelle(this),8,7,1);
        addEntite(new Echelle(this),8,6,1);

        addEntite(new Echelle(this),11,6,1);
        addEntite(new Echelle(this),11,5,1);
        addEntite(new Echelle(this),11,4,1);

        addEntite(new Echelle(this),4,6,1);
        addEntite(new Echelle(this),4,7,1);
        addEntite(new Echelle(this),4,8,1);




    }

    private void addEntite(Entite e, int x, int y, int z) {
        grilleEntites[x][y][z] = e;
        map.put(e, new Point3D(x, y, z));
    }
    
    /** Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point3D positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {

        boolean retour = false;
        
        Point3D pCourant = map.get(e);
        
        Point3D pCible = calculerPointCible(pCourant, d);

        if((e instanceof Heros) && !(((Heros) e).vivant)) return false;
        if(contenuDansGrille(pCible)){

            //Gestion ramassables
            if ((objetALaPosition(pCourant) instanceof Heros) && objetALaPosition(pCible) instanceof Bombe) {
                cptBombe++;
                score+=100;
                System.out.println(cptBombe);
            }
            if ((objetALaPosition(pCourant) instanceof Heros) && objetALaPosition(pCible) instanceof Bonus) {
                score+=200;
                System.out.println(cptBombe);
            }
            if ((objetALaPosition(pCourant) instanceof Bot) && objetALaPosition(pCible) instanceof Ramassable) {
                if(objetALaPosition(pCible) instanceof Bombe) botSurBombe = true;
                else botSurBonus= true;
            }

            //Gestion Echelle
            else if (!(objetALaPosition(pCible) instanceof Echelle)&& e instanceof Bot){
                botSurBombe = false;
                botSurBonus = false;
            }
            if (Bombe.getTotalBombes()==cptBombe) System.out.println("Vous avez gagné !");

            if ((objetALaPosition(pCible) instanceof Echelle)&& e instanceof Heros) {
                herosSurEchelle =true;
            }
            else if (!(objetALaPosition(pCible) instanceof Echelle)&& e instanceof Heros && !(objetALaPosition(pCible) instanceof Mur)) {
                herosSurEchelle =false;
            }
            if ((objetALaPosition(pCible) instanceof Echelle)&& e instanceof Bot) {
                botSurEchelle =true;
            }
            else if (!(objetALaPosition(pCible) instanceof Echelle)&& e instanceof Bot && !(objetALaPosition(pCible) instanceof Mur)) {
                botSurEchelle =false;
            }

            //Gestion direction gauche/droite
            if((d == Direction.droite) && e instanceof Heros && !((objetALaPosition(pCible) instanceof Echelle)||(objetALaPosition(pCible) instanceof Mur))){
                herosRegardeGauche =0;
                herosRegardeDroite = 1.5F;
            }
            else if ((d == Direction.gauche) && e instanceof Heros && !((objetALaPosition(pCible) instanceof Echelle) ||(objetALaPosition(pCible) instanceof Mur))){
                herosRegardeDroite =0;
                herosRegardeGauche =1.5F;
            }
            if((d == Direction.droite) && e instanceof Bot && !((objetALaPosition(pCible) instanceof Echelle)||(objetALaPosition(pCible) instanceof Mur))){
                botRegardeGauche =0;
                botRegardeDroite = 1.5F;
            }
            else if ((d == Direction.gauche) && e instanceof Bot && !((objetALaPosition(pCible) instanceof Echelle) ||(objetALaPosition(pCible) instanceof Mur))){
                botRegardeDroite =0;
                botRegardeGauche =1.5F;
            }

            //Gestion Colonne qui souleve le Heros
            if((e instanceof Colonne)&&(d==Direction.haut)&&((objetALaPosition(pCible) instanceof Heros)||(objetALaPosition(pCible) instanceof Bot))){
                Point3D pCible2 = calculerPointCible(pCible, d);
                if(objetALaPosition(pCible2) instanceof Mur){
                    deplacerEntite(pCourant, pCible, e);
                }
                else {
                    deplacerEntite(pCible, pCible2, objetALaPosition(pCible));
                    deplacerEntite(pCourant, pCible, e);
                }

            }

            //Gestion Collisions Bot
            if (objetALaPosition(pCible) instanceof Bot && objetALaPosition(pCourant) instanceof Heros)
            {
                end = true;
            }
            if (objetALaPosition(pCible) instanceof Heros && objetALaPosition(pCourant) instanceof Bot)
            {
                end = true;
            }
            if( (objetALaPosition(pCible)instanceof Bot) && (objetALaPosition(pCourant) instanceof Colonne))
            {
                ((Bot) objetALaPosition(pCible)).vivant = false;
                retour=true;
            }

            //Gestion Fleche
            if(fleche1Timer == 0){
                FlecheControl.getInstance().setDirectionCourante(Direction.droite);
            }
            if ((objetALaPosition(pCible) instanceof Bot) && objetALaPosition(pCourant) instanceof Fleche){
                ((EntiteDynamique) objetALaPosition(pCible)).vivant = false;
            }
            if((objetALaPosition(pCible) instanceof Colonne || objetALaPosition(pCible) instanceof Mur) && e instanceof Fleche){
                deplacerEntite(pCourant, ((Fleche) objetALaPosition(pCourant)).dispPos, e);
                System.out.println("téleportation !");
                FlecheControl.getInstance().resetDirection();
                fleche1Timer = FlecheControl.getTimer();
            }


        }




        if (contenuDansGrille(pCible) && ((objetALaPosition(pCible)==null)||(objetALaPosition(pCible).peutEtreTraverse())||(objetALaPosition(pCible).peutEtreEcrase()))) { // a adapter (collisions murs, etc.)
            // compter le déplacement : 1 deplacement horizontal et vertical max par pas de temps par entité
            switch (d) {
                case bas:
                case haut:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);

                        retour = true;
                    }
                    retour= true;
                    break;
                case gauche:
                case droite:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;

                    }
                    break;
            }
        }

        if (retour) {
            deplacerEntite(pCourant, pCible, e);
        }

        return retour;
    }
    
    private Point3D calculerPointCible(Point3D pCourant, Direction d) {
        Point3D pCible = null;
        
        switch(d) {
            case haut: pCible = new Point3D(pCourant.getX(), pCourant.getY() - 1, pCourant.getZ()); break;
            case bas : pCible = new Point3D(pCourant.getX(), pCourant.getY() + 1,pCourant.getZ()); break;
            case gauche : pCible = new Point3D(pCourant.getX() - 1, pCourant.getY(),pCourant.getZ()); break;
            case droite : pCible = new Point3D(pCourant.getX() + 1, pCourant.getY(),pCourant.getZ()); break;
            
        }
        return pCible;
    }
    
    private void deplacerEntite(Point3D pCourant, Point3D pCible, Entite e) {
        if((objetALaPosition(pCible) instanceof Heros) && ((e instanceof Colonne) || (e instanceof Fleche))){
            ((Heros) objetALaPosition(pCible)).vivant = false;
            //Changer Sprite + Afficher Game Over
            this.end=true;
        }
        grilleEntites[(int) pCourant.getX()][(int) pCourant.getY()][0] = null;

        if((e!=null) && e instanceof Heros && ((objetALaPosition(pCible) != null) && (objetALaPosition(pCible).peutEtreTraverse()) && !(objetALaPosition(pCible) instanceof Echelle))){
            grilleEntites[(int) pCible.getX()][(int) pCible.getY()][1] = null;
        }
        grilleEntites[(int) pCible.getX()][(int) pCible.getY()][0] = e;
        Point3D pCibleNew = new Point3D(pCible.getX(), pCible.getY(), 1);
        map.put(e, pCibleNew);


    }
    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point3D p) {
        return p.getX() >= 0 && p.getX() < SIZE_X && p.getY() >= 0 && p.getY() < SIZE_Y && p.getZ() < SIZE_Z && p.getZ() >= 0;
    }
    
    private Entite objetALaPosition(Point3D p) {
        Entite retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleEntites[(int) p.getX()][(int) p.getY()][0];
            if (retour==null){
                retour = grilleEntites[(int) p.getX()][(int) p.getY()][1];
            }
        }
        
        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }

}

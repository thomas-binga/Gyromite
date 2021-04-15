package VueControleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import javafx.geometry.Point3D;
import modele.deplacements.Controle4Directions;
import modele.deplacements.Direction;
import modele.deplacements.colControl;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleurGyromite extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;
    private int sizeZ;
    public Graphics g;
    long startTime = System.nanoTime();


    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoBombe;
    private ImageIcon icoEchelle;
    private ImageIcon icoHeroEchelle;
    private ImageIcon icoBotEchelle;
    private ImageIcon icoColonne_rouge_bas;
    private ImageIcon icoColonne_rouge_corps;
    private ImageIcon icoColonne_rouge_haut;
    private ImageIcon icoColonne_bleu_bas;
    private ImageIcon icoColonne_bleu_corps;
    private ImageIcon icoColonne_bleu_haut;
    private ImageIcon icoHeroDroite;
    private ImageIcon icoHeroGauche;
    private ImageIcon icoBot;
    private ImageIcon icoBotDroite;
    private ImageIcon icoBotGauche;
    private ImageIcon icoBonus;
    private ImageIcon icoBotSurBombe;
    private ImageIcon icoBotSurBonus;
    private ImageIcon icoDispenserGauche;
    private ImageIcon icoDispenserDroite;
    private ImageIcon icoFlecheGauche;
    private ImageIcon icoFlecheGaucheDevantBombe;
    private ImageIcon icoFlecheGaucheDevantEchelle;
    private ImageIcon icoFlecheGaucheDevantBonus;
    private ImageIcon icoFlecheDroite;
    private ImageIcon icoFlecheDroiteDevantBombe;
    private ImageIcon icoFlecheDroiteDevantEchelle;
    private ImageIcon icoFlecheDroiteDevantBonus;
    private ImageIcon imgFinGagne;
    private ImageIcon imgFinPerdu;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)

    JComponent grilleJLabels;


    public VueControleurGyromite(Jeu _jeu) {
        sizeX = _jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        sizeZ = _jeu.SIZE_Z;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : Controle4Directions.getInstance().setDirectionCourante(Direction.droite); break;
                    case KeyEvent.VK_DOWN : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_A : colControl.getInstance().setDirectionCourante('r'); break;
                    case KeyEvent.VK_E : colControl.getInstance().setDirectionCourante('b'); break;
                    case KeyEvent.VK_R : jeu.restart(200);
                }
            }
        });
    }


    private void chargerLesIcones() {
        icoHero = chargerIcone("Images/hires/Hector.png");
        icoHeroDroite = chargerIcone("Images/hires/HectorDroite.png");
        icoHeroGauche = chargerIcone("Images/hires/HectorGauche.png");
        icoVide = chargerIcone("Images/hires/Vide.png");
        icoMur = chargerIcone("Images/hires/Mur.png");
        icoBombe = chargerIcone("Images/hires/Bombe.png");
        icoEchelle = chargerIcone("Images/hires/Echelle.png");
        icoHeroEchelle = chargerIcone("Images/hires/HeroSurEchelle.png");

        icoColonne_rouge_bas = chargerIcone("Images/hires/Colonne_rouge_bas.png");
        icoColonne_rouge_corps = chargerIcone("Images/hires/Colonne_rouge_corps.png");
        icoColonne_rouge_haut = chargerIcone("Images/hires/Colonne_rouge_haut.png");

        icoColonne_bleu_bas = chargerIcone("Images/hires/Colonne_bleu_bas.png");
        icoColonne_bleu_corps = chargerIcone("Images/hires/Colonne_bleu_corps.png");
        icoColonne_bleu_haut = chargerIcone("Images/hires/Colonne_bleu_haut.png");

        icoBot= chargerIcone("Images/hires/Zombie.png");
        icoBotDroite = chargerIcone("Images/hires/ZombieDroite.png");
        icoBotGauche = chargerIcone("Images/hires/ZombieGauche.png");
        icoBonus = chargerIcone("Images/hires/Emeraude.png");
        icoBotEchelle = chargerIcone("Images/hires/ZombieSurEchelle.png");
        icoBotSurBombe = chargerIcone("Images/hires/ZombieDevantBombe.png");
        icoBotSurBonus = chargerIcone("Images/hires/ZombieDevantBonus.png");

        icoDispenserGauche = chargerIcone("Images/hires/DispenserGauche.png");
        icoDispenserDroite = chargerIcone("Images/hires/DispenserDroite.png");
        icoFlecheGauche = chargerIcone("Images/hires/FlecheGauche.png");
        icoFlecheGaucheDevantBonus = chargerIcone("Images/hires/FlecheGaucheDevantEmeraude.png");
        icoFlecheGaucheDevantBombe = chargerIcone("Images/hires/FlecheGaucheDevantBombe.png");
        icoFlecheGaucheDevantEchelle = chargerIcone("Images/hires/FlecheGaucheDevantEchelle.png");
        icoFlecheDroite = chargerIcone("Images/hires/FlecheDroite.png");
        icoFlecheDroiteDevantBonus = chargerIcone("Images/hires/FlecheDroiteDevantEmeraude.png");
        icoFlecheDroiteDevantBombe = chargerIcone("Images/hires/FlecheDroiteDevantBombe.png");
        icoFlecheDroiteDevantEchelle = chargerIcone("Images/hires/FlecheDroiteDevantEchelle.png");

        imgFinGagne = chargerIcone("Images/hires/finGagne.png");
        imgFinPerdu = chargerIcone("Images/hires/finPerdu.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setSize(1280, 704);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                jlab.setLayout(new BoxLayout(jlab, BoxLayout.X_AXIS));
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    public void endScreen(){
        long endTime = System.nanoTime();
        long time_taken=(endTime-startTime)/1000000000;
        System.out.println(time_taken);

        JPanel endScreenPanel = new JPanel();

        JLabel endScreentxt = new JLabel();
        endScreenPanel.add(endScreentxt);

        if (jeu.loose){
            endScreentxt.setIcon(imgFinPerdu);
            jeu.end = true;
        } else if (jeu.win) {
            endScreentxt.setIcon(imgFinGagne);
            jeu.end = true;
        }



        remove(grilleJLabels);
        revalidate();
        add(endScreenPanel);


    }
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for(int z = sizeZ-1; z>=0; z--) {
                    if (jeu.getGrille()[x][y][z] instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
                        // System.out.println("Héros !");
                        if (jeu.herosSurEchelle) tabJLabel[x][y].setIcon(icoHeroEchelle);
                        else if (!jeu.herosSurEchelle) tabJLabel[x][y].setIcon(icoHero);
                        if(jeu.herosRegardeDroite >0 && (!jeu.herosSurEchelle)) {
                            tabJLabel[x][y].setIcon(icoHeroDroite);
                        }
                        else if(jeu.herosRegardeGauche >0 && (!jeu.herosSurEchelle)) {
                            tabJLabel[x][y].setIcon(icoHeroGauche);
                        }
                    }
                    else if (jeu.getGrille()[x][y][z] instanceof Bot){
                        if (jeu.getBotSurEchelle(x,y,0)) tabJLabel[x][y].setIcon(icoBotEchelle);
                        else if (!jeu.getBotSurEchelle(x,y,0)) tabJLabel[x][y].setIcon(icoBot);
                        if(jeu.botRegardeDroite >0 && (!jeu.getBotSurEchelle(x,y,0))) {
                            tabJLabel[x][y].setIcon(icoBotDroite);
                        }
                        else if(jeu.botRegardeGauche >0 && (!jeu.getBotSurEchelle(x,y,0))) {
                            tabJLabel[x][y].setIcon(icoBotGauche);
                        }
                        if (jeu.getBotSurBombe(x,y,0) && (!jeu.getBotSurEchelle(x,y,0)) && (!jeu.getBotSurBonus(x,y,0)) && (jeu.botRegardeGauche==0) && (jeu.botRegardeDroite==0)) tabJLabel[x][y].setIcon(icoBotSurBombe);
                        else if ((!jeu.getBotSurBombe(x,y,0)) && (!jeu.getBotSurEchelle(x,y,0)) && (!jeu.getBotSurBonus(x,y,0)) && (jeu.botRegardeGauche==0) && (jeu.botRegardeDroite==0)) tabJLabel[x][y].setIcon(icoBot);
                        if (jeu.getBotSurBonus(x,y,0) && (!jeu.getBotSurEchelle(x,y,0)) && (!jeu.getBotSurBombe(x,y,0)) && (jeu.botRegardeGauche==0) && (jeu.botRegardeDroite==0)) tabJLabel[x][y].setIcon(icoBotSurBonus);
                        else if ((!jeu.getBotSurBonus(x,y,0)) && (!jeu.getBotSurEchelle(x,y,0)) && (!jeu.getBotSurBombe(x,y,0)) && (jeu.botRegardeGauche==0) && (jeu.botRegardeDroite==0)) tabJLabel[x][y].setIcon(icoBot);
                    }
                    else if (jeu.getGrille()[x][y][z] instanceof Mur) {
                        tabJLabel[x][y].setIcon(icoMur);
                    } else if (jeu.getGrille()[x][y][z] instanceof Colonne) {
                        Colonne col = (Colonne) jeu.getGrille()[x][y][z];
                        if(!(jeu.getGrille()[x][y-1][z] instanceof Colonne)){
                            if(col.couleur == 'r') tabJLabel[x][y].setIcon(icoColonne_rouge_haut);
                            else tabJLabel[x][y].setIcon(icoColonne_bleu_haut);
                        }
                        else if(!(jeu.getGrille()[x][y+1][z] instanceof Colonne)){
                            if(col.couleur == 'r') tabJLabel[x][y].setIcon(icoColonne_rouge_bas);
                            else tabJLabel[x][y].setIcon(icoColonne_bleu_bas);
                        }
                        else {
                            if(col.couleur == 'r') tabJLabel[x][y].setIcon(icoColonne_rouge_corps);
                            else tabJLabel[x][y].setIcon(icoColonne_bleu_corps);
                        }
                    } else if (jeu.getGrille()[x][y][z] instanceof Bombe) {
                        tabJLabel[x][y].setIcon(icoBombe);
                    }
                    else if (jeu.getGrille()[x][y][z] instanceof Bonus){
                        tabJLabel[x][y].setIcon(icoBonus);
                    }
                    else if (jeu.getGrille()[x][y][z] instanceof Echelle) {
                        tabJLabel[x][y].setIcon(icoEchelle);
                    }
                    else if (jeu.getGrille()[x][y][z] instanceof Dispenser){
                        if(((Dispenser) jeu.getGrille()[x][y][z]).sens == 'g'){
                            tabJLabel[x][y].setIcon(icoDispenserGauche);
                        }
                        else tabJLabel[x][y].setIcon(icoDispenserDroite);
                    }
                    else if (jeu.getGrille()[x][y][z] instanceof Fleche && !(jeu.getGrille()[x][y][1] instanceof Dispenser)){
                        if(((Fleche) jeu.getGrille()[x][y][z]).sens == 'g'){
                            if(jeu.getGrille()[x][y][1] instanceof Echelle){
                                tabJLabel[x][y].setIcon(icoFlecheGaucheDevantEchelle);
                            }
                            else if (jeu.getGrille()[x][y][1] instanceof Bombe){
                                tabJLabel[x][y].setIcon(icoFlecheGaucheDevantBombe);
                            }
                            else if (jeu.getGrille()[x][y][0] instanceof Bonus){
                                tabJLabel[x][y].setIcon(icoFlecheGaucheDevantBonus);
                            }
                            else tabJLabel[x][y].setIcon(icoFlecheGauche);
                        }
                        else{
                            if(jeu.getGrille()[x][y][1] instanceof Echelle){
                                tabJLabel[x][y].setIcon(icoFlecheDroiteDevantEchelle);
                            }
                            else if (jeu.getGrille()[x][y][1] instanceof Bombe){
                                tabJLabel[x][y].setIcon(icoFlecheDroiteDevantBombe);
                            }
                            else if (jeu.getGrille()[x][y][0] instanceof Bonus){
                                tabJLabel[x][y].setIcon(icoFlecheDroiteDevantBonus);
                            }
                            else tabJLabel[x][y].setIcon(icoFlecheDroite);
                        }
                    }
                    else if(z==1 && y<10){
                        tabJLabel[x][y].setIcon(icoVide);
                    } else if(x==0 && y==10){
                        tabJLabel[0][10].setOpaque(true);
                        tabJLabel[0][10].setText("<html>Bombes<br/>ramassées</html>");
                        tabJLabel[0][10].setBackground(Color.BLACK);
                        tabJLabel[0][10].setForeground(Color.WHITE);
                    } else if(x==2 && y==10){
                        tabJLabel[1][10].setOpaque(true);
                        tabJLabel[1][10].setText(String.valueOf(jeu.cptBombe+" / "+Bombe.getTotalBombes()));
                        tabJLabel[1][10].setBackground(Color.BLACK);
                        tabJLabel[1][10].setForeground(Color.GREEN);
                    }
                    else if(x==4 && y==10){
                        if (jeu.cptBombe==Bombe.getTotalBombes()){
                            tabJLabel[4][10].setOpaque(true);
                            tabJLabel[4][10].setText(String.valueOf("Vous avez gagné !"));
                            tabJLabel[4][10].setBackground(Color.BLACK);
                            tabJLabel[4][10].setForeground(Color.GREEN);
                        }
                        else {
                            tabJLabel[4][10].setOpaque(true);
                            tabJLabel[4][10].setText(String.valueOf("Jeu en cours"));
                            tabJLabel[4][10].setBackground(Color.BLACK);
                            tabJLabel[4][10].setForeground(Color.CYAN);
                        }

                    }
                }
            }
        }


    }

    @Override
    public void update(Observable o, Object arg) {
        if (jeu.loose || jeu.win){
            endScreen();
            jeu.end = true;
        } else {
            mettreAJourAffichage();
            if (jeu.herosRegardeDroite > 0) jeu.herosRegardeDroite -= 0.5;
            if (jeu.herosRegardeGauche > 0) jeu.herosRegardeGauche -= 0.5;
            if(jeu.botRegardeGauche > 0) jeu.botRegardeGauche -= 0.5;
            if(jeu.botRegardeDroite > 0) jeu.botRegardeDroite -= 0.5;
            if(jeu.fleche1Timer > 0){
                if (jeu.fleche1Timer - 0.5 >0) jeu.fleche1Timer-=0.5;
                else jeu.fleche1Timer = 0;
            }
            if(jeu.fleche2Timer>0){
                if (jeu.fleche2Timer - 0.5 > 0) jeu.fleche2Timer -= 0.5;
                else jeu.fleche2Timer = 0;
            }

        }
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}

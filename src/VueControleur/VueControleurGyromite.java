package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import modele.deplacements.Controle4Directions;
import modele.deplacements.Couleur;
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

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoColonne;
    private ImageIcon icoBasColonne;
    private ImageIcon icoHautColonne;
    private ImageIcon icoRamassable;
    private ImageIcon icoEchelle;
    private ImageIcon icoHeroEchelle;
    private ImageIcon icoColonne_rouge_bas;
    private ImageIcon icoColonne_rouge_corps;
    private ImageIcon icoColonne_rouge_haut;
    private ImageIcon icoColonne_bleu_bas;
    private ImageIcon icoColonne_bleu_corps;
    private ImageIcon icoColonne_bleu_haut;
    private ImageIcon icoHeroDroite;
    private ImageIcon icoHeroGauche;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


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
        icoRamassable = chargerIcone("Images/hires/Bombe.png");
        icoEchelle = chargerIcone("Images/hires/Echelle.png");
        icoHeroEchelle = chargerIcone("Images/hires/HeroSurEchelle.png");

        icoColonne_rouge_bas = chargerIcone("Images/hires/Colonne_rouge_bas.png");
        icoColonne_rouge_corps = chargerIcone("Images/hires/Colonne_rouge_corps.png");
        icoColonne_rouge_haut = chargerIcone("Images/hires/Colonne_rouge_haut.png");

        icoColonne_bleu_bas = chargerIcone("Images/hires/Colonne_bleu_bas.png");
        icoColonne_bleu_corps = chargerIcone("Images/hires/Colonne_bleu_corps.png");
        icoColonne_bleu_haut = chargerIcone("Images/hires/Colonne_bleu_haut.png");
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

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

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

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for(int z = sizeZ-1; z>=0; z--) {
                    if (jeu.getGrille()[x][y][z] instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
                        // System.out.println("Héros !");
                        if (jeu.HerosSurEchelle) tabJLabel[x][y].setIcon(icoHeroEchelle);
                        else if (!jeu.HerosSurEchelle) tabJLabel[x][y].setIcon(icoHero);
                        if(jeu.regardeDroite>0 && (!jeu.HerosSurEchelle)) {
                            tabJLabel[x][y].setIcon(icoHeroDroite);
                        }
                        else if(jeu.regardeGauche>0 && (!jeu.HerosSurEchelle)) {
                            tabJLabel[x][y].setIcon(icoHeroGauche);
                        }
                    } else if (jeu.getGrille()[x][y][z] instanceof Mur) {
                        tabJLabel[x][y].setIcon(icoMur);
                    } else if (jeu.getGrille()[x][y][z] instanceof Colonne) {
                        Colonne col = (Colonne) jeu.getGrille()[x][y][z];
                        if(!(jeu.getGrille()[x][y-1][z] instanceof Colonne)){
                            if(col.couleur == 'r') tabJLabel[x][y].setIcon(icoColonne_rouge_haut);
                            else tabJLabel[x][y].setIcon(icoColonne_bleu_haut);
                            ((Colonne) jeu.getGrille()[x][y][z]).bord = true;
                        }
                        else if(!(jeu.getGrille()[x][y+1][z] instanceof Colonne)){
                            if(col.couleur == 'r') tabJLabel[x][y].setIcon(icoColonne_rouge_bas);
                            else tabJLabel[x][y].setIcon(icoColonne_bleu_bas);
                            ((Colonne) jeu.getGrille()[x][y][z]).bord = true;
                        }
                        else {
                            if(col.couleur == 'r') tabJLabel[x][y].setIcon(icoColonne_rouge_corps);
                            else tabJLabel[x][y].setIcon(icoColonne_bleu_corps);
                        }
                    } else if (jeu.getGrille()[x][y][z] instanceof Ramassable) {
                        tabJLabel[x][y].setIcon(icoRamassable);
                    } else if (jeu.getGrille()[x][y][z] instanceof Echelle) {
                        tabJLabel[x][y].setIcon(icoEchelle);
                    } else if(z==1 && y<10){
                        tabJLabel[x][y].setIcon(icoVide);
                    } else if(x==0 && y==10){
                        tabJLabel[0][10].setOpaque(true);
                        tabJLabel[0][10].setText("<html>Bombes<br/>ramassées</html>");
                        tabJLabel[0][10].setBackground(Color.BLACK);
                        tabJLabel[0][10].setForeground(Color.WHITE);
                    } else if(x==2 && y==10){
                        tabJLabel[1][10].setOpaque(true);
                        tabJLabel[1][10].setText(String.valueOf(jeu.cptBombe+" / "+Ramassable.getTotalBombes()));
                        tabJLabel[1][10].setBackground(Color.BLACK);
                        tabJLabel[1][10].setForeground(Color.GREEN);
                    }
                    else if(x==4 && y==10){
                        if (jeu.cptBombe==Ramassable.getTotalBombes()){
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
        mettreAJourAffichage();
        if (jeu.regardeDroite>0) jeu.regardeDroite -= 0.5;
        if (jeu.regardeGauche>0) jeu.regardeGauche -= 0.5;
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

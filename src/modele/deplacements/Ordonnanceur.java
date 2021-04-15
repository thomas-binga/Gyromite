package modele.deplacements;

import VueControleur.MusicPlayer;
import modele.plateau.Jeu;

import java.util.ArrayList;
import java.util.Observable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;

import static java.lang.Thread.*;

public class Ordonnanceur extends Observable implements Runnable {
    private Jeu jeu;
    private ArrayList<RealisateurDeDeplacement> lstDeplacements = new ArrayList<RealisateurDeDeplacement>();
    private long pause;
    Thread instance = new Thread(this);

    public void add(RealisateurDeDeplacement deplacement) {
        lstDeplacements.add(deplacement);
    }
    public void clear() {lstDeplacements.clear();}

    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    public void start(long _pause) {
        pause = _pause;
        instance.start();

    }
    public void stop(){
        instance.stop();
    }

    @Override
    public void run() {
        boolean update = false;

        while(true) {
            jeu.resetCmptDepl();
            for (RealisateurDeDeplacement d : lstDeplacements) {
                if (d.realiserDeplacement())
                    update = true;
            }

            Controle4Directions.getInstance().resetDirection();
            IA.getInstance(1).resetDirection();
            IA.getInstance(2).resetDirection();


            if (update) {
                setChanged();
                notifyObservers();
            }

            try {
                sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

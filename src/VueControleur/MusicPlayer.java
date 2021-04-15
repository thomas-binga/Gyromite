package VueControleur;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;



public class MusicPlayer {
    public static void main(String[] args) {
        try {
            File f = new File("music/minecraft.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}

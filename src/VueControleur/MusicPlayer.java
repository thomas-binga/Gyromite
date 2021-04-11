package VueControleur;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;



public class MusicPlayer {
    public static Mixer mixer;
    public static Clip clip;
    public static void main(String[] args){
        Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixInfos){

            System.out.println("Name:  " + info.getName() +  ", Description:  " + info.getDescription());
        }
        System.out.println("caca");
        mixer=AudioSystem.getMixer(mixInfos[0]);
        DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
        try
        {
            clip= (Clip) mixer.getLine(dataInfo);
        }
        catch(LineUnavailableException lue) {lue.printStackTrace();}

        try
        {
            URL soundURL = MusicPlayer.class.getResource("/music/key.mp3");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            clip.open(audioStream);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        clip.start();
    }
}

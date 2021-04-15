package VueControleur;

import jaco.mp3.player.MP3Player;
import java.io.File;


public class MusicPlayer {
    String path = new String();
    boolean loop;
    public MusicPlayer(String path, boolean loop){
        this.path=path;
        this.loop=loop;
    }
    public void main(String[] args) {
        try{
            File f = new File ("Music/"+path);
            MP3Player mp3 = new MP3Player(f);
            mp3.setRepeat(loop);
            mp3.setShuffle(loop);
            mp3.play();
            while(!mp3.isStopped()){
                Thread.sleep(5000);
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}
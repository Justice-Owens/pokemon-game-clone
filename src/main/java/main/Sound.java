package main;

import com.sun.jdi.FloatType;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sound implements Runnable {

    public Clip clip;
    Map<String,URL> soundURL = new HashMap<>();

    public Sound(){

        soundURL.put("main_theme", getClass().getResource("/music/route-104.wav"));
        soundURL.put("receive-item", getClass().getResource("/sound/receive-item.wav"));
        soundURL.put("door_enter", getClass().getResource("/sound/door-enter-sound-effect.wav"));
        soundURL.put("game-over", getClass().getResource("/sound/game-over.wav"));
    }

    public void setFile(String fileName){
        try{

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL.get(fileName));
            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }

    public float getVolume(){
        FloatControl gainControl =(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);

    }

    public void setVolume(float volume){
        FloatControl gainControl =(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        if(volume < 0f || volume > 1f){
            throw new IllegalArgumentException("Volume Not Valid: " + volume);
        }
        gainControl.setValue(20f * (float) Math.log10(volume));
    }


    @Override
    public void run() {
    }
}

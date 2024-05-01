package main.code.service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private long pausedTime;
    private static float volume = -10f;
    public boolean isClipRunning() {
        return clip.isRunning();
    }
    private String filePath = "src/main/resources/music/650965__betabeats__beat-tune-abysses.wav";

    // Method for playing music from file
    public void play() {

        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setMicrosecondPosition(pausedTime);

            // Volume control
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            floatControl.setValue(volume);

            clip.start();

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }


    // Method for stopping music
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Method for pausing music
    public void pause(){
        if (clip != null && clip.isRunning()) {
            clip.stop();
           pausedTime = clip.getMicrosecondPosition();
        }
    }

    // Method for changing the volume
    public static void changeVolume(float value){
        volume = value;
    }
}

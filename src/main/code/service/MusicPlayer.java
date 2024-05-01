package main.code.service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private long pausedTime;

    public boolean isClipRunning() {
        return clip.isRunning();
    }

    // Method for playing music from file
    public void play(String filePath) {

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

            clip.start();
            do {
                Thread.sleep(1);
            } while (clip.isRunning());


        } catch (UnsupportedAudioFileException | LineUnavailableException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    // Method for stopping music
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void pause(){
        if (clip != null && clip.isRunning()) {
            clip.stop();
           pausedTime = clip.getMicrosecondPosition();
        }
    }
}

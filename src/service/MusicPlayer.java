package service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private long pausedTime;
    private static float volume = -10f;
    private String lastPlayed;

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

            // Volume control
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            floatControl.setValue(volume);

            lastPlayed = filePath;

            clip.start();

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void resume() {
        if (lastPlayed != null) {
            play(lastPlayed);
        }
    }


    // Method for stopping music
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Method for pausing music
    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            pausedTime = clip.getMicrosecondPosition();
        }
    }

    // Method for changing the volume
    public static void changeVolume(float value) {
        volume = value;
    }
}

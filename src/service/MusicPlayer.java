package service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private long pausedTime;
    private static float volume = -30f;
    private String lastPlayed;
    private boolean isMusic;
    private static float musicVolume = -30f;

    public boolean isClipRunning() {
        return clip.isRunning();
    }

    // Method for playing music from file
    public void play(String filePath, boolean isMusic) {

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
            if (isMusic) {
                floatControl.setValue(musicVolume);
            } else {
                floatControl.setValue(volume);
            }
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

    // TODO improve this
    public void resume() {
        if (lastPlayed != null) {
            play(lastPlayed, true);
        }
    }


    // Method for stopping music
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static float getVolume() {
        return volume;
    }

    public static void setVolume(float volume) {
        MusicPlayer.volume = volume;
    }

    // Method for pausing music
    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            pausedTime = clip.getMicrosecondPosition();
        }
    }

    // Method for changing the volume
    public static void changeVolume(float value, boolean isMusic) {
        if (isMusic) {
            musicVolume = value;
        } else
            volume = value;
    }
}

package service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private long pausedTime;
    private static float fxVolume = -20f;
    private String lastPlayed;
    private boolean isMusic;
    private static float musicVolume = -30f;

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        MusicPlayer.musicVolume = musicVolume;
    }

    public long getPausedTime() {
        return pausedTime;
    }

    public void setPausedTime(long pausedTime) {
        this.pausedTime = pausedTime;
    }

    public boolean isClipRunning() {
        return clip.isRunning();
    }

    // Method for playing music from file
    private void play(String filePath, boolean isMusic) {

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
                floatControl.setValue(fxVolume);
            }
            lastPlayed = filePath;

            clip.start();

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playMusic(String filePath){
        play(filePath, true);
    }

    public void playFX(String filePath){
        play(filePath, false);
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void resume(boolean isMusic) {
        if (lastPlayed != null) {
            play(lastPlayed, isMusic);
        }
    }

    public void playWithCustomVolume(String musicPath, float volume) {
        float musicVolumeOriginal = getMusicVolume();
        float fxVolumeOriginal = getFxVolume();

        setMusicVolume(volume);
        setFxVolume(volume);

        playMusic(musicPath);

        setMusicVolume(musicVolumeOriginal);
        setFxVolume(fxVolumeOriginal);
    }

    // Method for stopping music
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    public static float getFxVolume() {
        return fxVolume;
    }

    public static void setFxVolume(float fxVolume) {
        MusicPlayer.fxVolume = fxVolume;
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
            fxVolume = value;
    }

}

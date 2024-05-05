package service;

import view.TitleFrame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private long pausedTime;
    private String lastPlayed;
    private boolean isMusic;
    private String path;
    private static float fxVolume = -20f;
    private static float musicVolume = -30f;

    public MusicPlayer(boolean isMusic, String path) {
        this.isMusic = isMusic;
        this.path = path;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        MusicPlayer.musicVolume = musicVolume;
    }

    // Method for playing music from file
    private void playClip(String filePath) {

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

    // Plays from a custom path
    public void play(String filePath) {
        if (!filePath.isBlank()) {
            playClip(filePath);
        }
    }

    // Plays from default path set when making musicPlayer, if available
    public void playDefault() {
        if (!path.isBlank()) {
            play(path);
        }
    }

    // resumes a clip from lastPlayed path
    public void resume() {
        if (lastPlayed != null && !clip.isRunning()) {
            play(lastPlayed);
        }
    }

    public void playWithCustomVolume(float volume, String musicPath) {
        float musicVolumeOriginal = getMusicVolume();
        float fxVolumeOriginal = getFxVolume();

        setMusicVolume(volume);
        setFxVolume(volume);

        play(musicPath);

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

    public long getPausedTime() {
        return pausedTime;
    }

    public void setPausedTime(long pausedTime) {
        this.pausedTime = pausedTime;
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public boolean isMusic() {
        return isMusic;
    }

    public void setMusic(boolean music) {
        isMusic = music;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

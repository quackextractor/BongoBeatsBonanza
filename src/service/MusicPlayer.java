package service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * The {@code MusicPlayer} class provides methods to play, pause, resume, and stop music or sound effects.
 * It also allows setting custom volumes for music and sound effects.
 */
public class MusicPlayer {
    private Clip clip;
    private long pausedTime;
    private String lastPlayed;
    private final boolean isMusic;
    private final String path;
    private static float fxVolume = -20f;
    private static float musicVolume = -10f;

    /**
     * Constructs a {@code MusicPlayer} object.
     *
     * @param isMusic indicates whether the player is for music or sound effects
     * @param path    the default path for music/sound effects
     */
    public MusicPlayer(boolean isMusic, String path) {
        this.isMusic = isMusic;
        this.path = path;
    }

    /**
     * Gets the current music volume.
     *
     * @return the current music volume
     */
    public static float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Sets the music volume.
     *
     * @param musicVolume the volume to set
     */
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
            ErrorLogger.logStackTrace(e);
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

    /**
     * Plays music with a custom volume.
     *
     * @param volume    the custom volume
     * @param musicPath the path of the music to play
     */
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

    /**
     * Gets the current position of the clip in microseconds.
     *
     * @return the current position of the clip in microseconds
     */
    public long getMicroSecondPos(){
        if (clip != null){
            return clip.getMicrosecondPosition();
        }
        else return 0;
    }

    /**
     * Gets the length of the clip in microseconds.
     *
     * @return the length of the clip in microseconds
     */
    public long getMicroSecondLength(){
        if (clip != null){
            return clip.getMicrosecondLength();
        }
        else return 0;
    }
}

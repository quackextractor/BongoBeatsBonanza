package service;

import view.GameJPanel;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * Manages game settings such as music volume, sound effects volume, game difficulty, note speed,
 * and key bindings for game controls.
 */
public class SettingsManager {
    private static final String SETTINGS_FILE = "settings.properties";

    private static final String MUSIC_VOLUME_KEY = "musicVolume";
    private static final String FX_VOLUME_KEY = "fxVolume";
    private static final String DIFFICULTY_KEY = "difficulty";
    private static final String NOTE_SPEED_KEY = "noteSpeed";
    private static final String TRACK1_KEY = "track1Key";
    private static final String TRACK2_KEY = "track2Key";

    private final Properties properties;

    /**
     * Creates a new instance of SettingsManager.
     */
    public SettingsManager() {
        properties = new Properties();
        loadSettings();
    }

    /**
     * Loads settings from the properties file.
     */
    public static void loadSettings(SettingsManager settingsManager) {
        int difficulty = settingsManager.getDifficulty();
        Score.setDifficultyModifier(difficulty * 0.5);
        GameJPanel.modifyMoveParams(settingsManager.getNoteSpeed());
        MusicPlayer.setMusicVolume(settingsManager.getMusicVolume());
        MusicPlayer.setFxVolume(settingsManager.getFxVolume());
        GameJPanel.setTrack1Key(settingsManager.getTrack1Key());
        GameJPanel.setTrack2Key(settingsManager.getTrack2Key());
    }

    /**
     * Saves settings to the properties file.
     */
    public static void saveSettings(SettingsManager settingsManager) {
        settingsManager.saveSettings();
    }

    /**
     * Loads settings from the properties file.
     */
    public void loadSettings() {
        try (FileInputStream input = new FileInputStream(SETTINGS_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
        }
    }

    /**
     * Saves settings to the properties file.
     */
    public void saveSettings() {
        try (FileOutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(output, "Game Settings");
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
        }
    }

    public int getMusicVolume() {
        return Integer.parseInt(properties.getProperty(MUSIC_VOLUME_KEY, "-10"));
    }

    public void setMusicVolume(int volume) {
        properties.setProperty(MUSIC_VOLUME_KEY, String.valueOf(volume));
    }

    public int getFxVolume() {
        return Integer.parseInt(properties.getProperty(FX_VOLUME_KEY, "-10"));
    }

    public void setFxVolume(int volume) {
        properties.setProperty(FX_VOLUME_KEY, String.valueOf(volume));
    }

    public int getDifficulty() {
        return Integer.parseInt(properties.getProperty(DIFFICULTY_KEY, "2"));
    }

    public void setDifficulty(int difficulty) {
        properties.setProperty(DIFFICULTY_KEY, String.valueOf(difficulty));
    }

    public int getNoteSpeed() {
        return Integer.parseInt(properties.getProperty(NOTE_SPEED_KEY, "2"));
    }

    public void setNoteSpeed(int noteSpeed) {
        properties.setProperty(NOTE_SPEED_KEY, String.valueOf(noteSpeed));
    }

    public int getTrack1Key() {
        return Integer.parseInt(properties.getProperty(TRACK1_KEY, String.valueOf(KeyEvent.VK_UNDEFINED)));
    }

    public void setTrack1Key(int keyCode) {
        properties.setProperty(TRACK1_KEY, String.valueOf(keyCode));
    }

    public int getTrack2Key() {
        return Integer.parseInt(properties.getProperty(TRACK2_KEY, String.valueOf(KeyEvent.VK_UNDEFINED)));
    }

    public void setTrack2Key(int keyCode) {
        properties.setProperty(TRACK2_KEY, String.valueOf(keyCode));
    }
}

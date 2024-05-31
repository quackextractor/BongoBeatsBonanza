package controller;

import service.MusicPlayer;
import service.MusicPlayerManager;
import service.SettingsManager;
import view.GameFrame;
import view.LevelSelectionFrame;
import view.TitleFrame;

import javax.swing.*;
import java.awt.*;

import static service.AudioVerification.isMidiValid;
import static service.AudioVerification.isWavValid;

/**
 * The {@code GameController} class provides methods to configure and control the game.
 * It handles starting the game, starting a level, closing all frames, getting the game icon, and ending the game.
 */
public class GameController {

    public static String musicPath;
    public static String fontName;
    public static Image gameIcon;
    private static final MusicPlayer fxPlayer = new MusicPlayer(false, "");

    /**
     * Configures the game controller with the specified music path, font name, and game icon.
     *
     * @param musicPath the path to the music files
     * @param fontName  the name of the font used in the game
     * @param icon      the icon image for the game
     */
    public static void configGameController(String musicPath, String fontName, Image icon) {
        GameController.gameIcon = icon;
        GameController.musicPath = musicPath;
        GameController.fontName = fontName;
    }

    /**
     * Starts the game by loading settings and displaying the title frame.
     */
    public static void startGame() {
        SettingsManager settingsManager = new SettingsManager();
        SettingsManager.loadSettings(settingsManager);
        SwingUtilities.invokeLater(() -> {
            TitleFrame titleFrame = new TitleFrame(musicPath, fontName);
            titleFrame.setIconImage(gameIcon);
        });
    }

    /**
     * Starts a level if both MIDI and WAV files for the level are valid.
     *
     * @param levelName the name of the level to start
     * @return {@code true} if the level starts successfully, {@code false} otherwise
     */
    public static boolean startLevel(String levelName) {
        boolean midiValid = isMidiValid(levelName);
        boolean wavValid = isWavValid(levelName);

        if (midiValid && wavValid) {
            LevelSelectionFrame.hasLevelStarted = true;
            MusicPlayerManager.stopAllMusicPlayers();
            closeAllFrames();
            fxPlayer.play("resources/sounds/start.wav");
            GameFrame gameFrame = new GameFrame(levelName);
            gameFrame.setIconImage(gameIcon);
            return true;
        } else return false;
    }

    /**
     * Closes all frames in the application.
     */
    public static void closeAllFrames() {
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            frame.dispose();
        }
    }

    /**
     * Gets the icon image of the game.
     *
     * @return the game icon image
     */
    public static Image getGameIcon() {
        return gameIcon;
    }

    /**
     * Ends the game by stopping all music players, playing an ending sound, closing all frames, and exiting the application.
     */
    public static void endGame() {
        MusicPlayerManager.stopAllMusicPlayers();
        fxPlayer.play("resources/sounds/boowomp.wav");
        closeAllFrames();
        System.exit(0);
    }
}

package controller;

import service.MusicPlayer;
import service.MusicPlayerManager;
import view.GameFrame;
import view.LevelSelectionFrame;
import view.TitleFrame;

import javax.swing.*;

import java.awt.*;

import static service.AudioVerification.*;

public class GameController {

    public static String musicPath;
    public static String fontName;
    public static Image gameIcon;
    private static final MusicPlayer fxPlayer = new MusicPlayer(false,"");

    public GameController(String musicPath, String fontName, Image icon) {
        GameController.gameIcon = icon;
        GameController.musicPath = musicPath;
        GameController.fontName = fontName;
    }

    public static void startGame() {
        SwingUtilities.invokeLater(() -> {
            TitleFrame titleFrame = new TitleFrame(musicPath, fontName);
            titleFrame.setIconImage(gameIcon);
        });
    }

    public static boolean startLevel(String levelName) {
        boolean midiValid = isMidiValid(levelName);
        boolean wavValid = isWavValid(levelName);

        // Return true only if both midi and wav files are valid
        if (midiValid && wavValid) {
            LevelSelectionFrame.hasLevelStarted = true;
            MusicPlayerManager.stopAllMusicPlayers();
            closeAllFrames();
            fxPlayer.play("src/resources/sounds/start.wav");
            GameFrame gameFrame = new GameFrame(levelName);
            gameFrame.setIconImage(gameIcon);
            return true;
        } else return false;
    }

    public static void closeAllFrames() {
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            frame.dispose();
        }
    }

    public static Image getGameIcon() {
        return gameIcon;
    }

    public static void endGame() {
        MusicPlayerManager.stopAllMusicPlayers();
        fxPlayer.play("src/resources/sounds/boowomp.wav");
        System.exit(0);
    }
}

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

    private final String musicPath;
    private final String fontName;
    private static final MusicPlayer fxPlayer = new MusicPlayer(false,"");

    public GameController(String musicPath, String fontName) {
        this.musicPath = musicPath;
        this.fontName = fontName;
    }

    public void startGame() {
        SwingUtilities.invokeLater(() -> {
            TitleFrame titleFrame = new TitleFrame(musicPath, fontName);
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
            return true;
        } else return false;
    }

    private static void closeAllFrames() {
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            frame.dispose();
        }
    }


    public static void endGame() {
        fxPlayer.play("src/resources/sounds/exit.wav");
        System.exit(0);
    }
}

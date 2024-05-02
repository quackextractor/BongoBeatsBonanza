package controller;

import service.MusicPlayer;
import view.TitleFrame;

import javax.swing.*;

import static service.AudioVerification.*;

public class GameController {

    private String musicPath;
    private String fontName;

    public GameController(String musicPath, String fontName) {
        this.musicPath = musicPath;
        this.fontName = fontName;
    }

    public void startGame() {
        SwingUtilities.invokeLater(() -> {
            TitleFrame titleFrame = new TitleFrame(musicPath, fontName);
        });
    }

    public static boolean startLevel(String levelName){
        boolean midiValid = isMidiValid(levelName);
        boolean wavValid = isWavValid(levelName);

        // Return true only if both midi and wav files are valid
        return midiValid && wavValid;
    }


    public static void endGame() {
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.play("src/resources/sounds/exit.wav");
        System.exit(0);
    }
}

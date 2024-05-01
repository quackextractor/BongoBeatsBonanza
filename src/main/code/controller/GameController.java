package main.code.controller;

import main.code.model.Song;
import main.code.service.MusicPlayer;
import main.code.view.TitleFrame;

import javax.swing.*;

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

    public static void endGame() {
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.play("src/main/resources/sounds/exit.wav");
        System.exit(0);
    }
}

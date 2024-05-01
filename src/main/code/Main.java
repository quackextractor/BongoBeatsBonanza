package main.code;

import main.code.controller.GameController;
import main.code.service.MusicPlayer;
import main.code.view.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // test out music player
        String musicPath = "src/main/resources/music/650965__betabeats__beat-tune-abysses.wav";

        SwingUtilities.invokeLater(()->{GameFrame gameFrame = new GameFrame(musicPath);});

    }
}
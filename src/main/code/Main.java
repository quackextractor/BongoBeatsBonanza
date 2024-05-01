package main.code;

import main.code.view.TitleFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        String musicPath = "src/main/resources/music/650965__betabeats__beat-tune-abysses.wav";
        String fontName = "Blade Runner Movie Font";

        SwingUtilities.invokeLater(()->{
            TitleFrame titleFrame = new TitleFrame(musicPath, fontName);});

    }
}
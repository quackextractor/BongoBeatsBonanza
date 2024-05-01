package main.code;

import main.code.controller.GameController;

public class Main {
    public static void main(String[] args) {

        String musicPath = "src/main/resources/music/650965__betabeats__beat-tune-abysses.wav";
        String fontName = "Blade Runner Movie Font";

        GameController gameController = new GameController(musicPath, fontName);
        gameController.startGame();

    }
}
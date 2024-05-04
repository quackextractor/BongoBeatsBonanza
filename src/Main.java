import controller.GameController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        String musicPath = "src/resources/music/menu.wav";
        String fontName = "Blade Runner Movie Font";

        // Applies System look to all Frames
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        GameController gameController = new GameController(musicPath, fontName);
        gameController.startGame();
    }
}
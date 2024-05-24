import controller.GameController;
import service.ErrorLogger;
import view.GameFrame;

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
            ErrorLogger.logStackTrace(e);
            throw new RuntimeException(e);
        }
       // GameController gameController = new GameController(musicPath, fontName);
       // gameController.startGame();
       GameFrame gameFrame = new GameFrame("oddLoop");
    }
}
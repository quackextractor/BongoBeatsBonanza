import controller.GameController;
import service.ErrorLogger;
import view.GameJPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        String musicPath = "src/resources/music/menu.wav";
        String fontName = "Arial";
        Image gameIcon;
        gameIcon = GameJPanel.loadImage("src/resources/sprites/icon.png");

        // Applies System look to all Frames
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            ErrorLogger.logStackTrace(e);
            throw new RuntimeException(e);
        }
        GameController gameController = new GameController(musicPath, fontName, gameIcon);
        gameController.startGame();
    //   GameFrame gameFrame = new GameFrame("oddLoop");
    }
}
import controller.GameController;
import service.ErrorLogger;
import service.GameUtils;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        String musicPath = "resources/music/menu.wav";
        String fontName = "Arial";
        Image gameIcon;
        gameIcon = GameUtils.loadImage("resources/sprites/icon.png");

        // Applies System look to all Frames
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            ErrorLogger.logStackTrace(e);
            throw new RuntimeException(e);
        }
       GameController.configGameController(musicPath, fontName, gameIcon);
       GameController.startGame();
    }
}
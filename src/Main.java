import controller.GameController;
import service.ErrorLogger;
import view.FancyJLabel;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;

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
        GameController gameController = new GameController(musicPath, fontName);
      //  gameController.startGame();
    //   GameFrame gameFrame = new GameFrame("oddLoop");
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fancy JLabel Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new FlowLayout());

            FancyJLabel fancyLabel = new FancyJLabel("Yippie");
            fancyLabel.setDefaultSize(30);
            frame.add(fancyLabel);
            frame.setVisible(true);
        });
    }
}
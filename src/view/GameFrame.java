package view;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {
    private final int screenWidth = 700;
    private final int screenHeight = 800;
    private final int lineSpacing = screenWidth / 4;
    private final int lineWidth = screenWidth / 20;
    private final int firstLineX = (screenWidth - lineSpacing) / 2 - lineWidth / 2;
    private final int secondLineX = firstLineX + lineSpacing;
    private final int horizontalHeight = screenHeight / 3;
    private final GameJPanel gameJPanel;

    public GameFrame(String levelName) {
        gameJPanel = new GameJPanel(firstLineX, secondLineX, horizontalHeight, levelName);
        add(gameJPanel);
        setTitle(levelName);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Setting to DO_NOTHING_ON_CLOSE
        setSize(screenWidth, screenHeight);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            GameJPanel.setGameOver(true);
        }
    }
}

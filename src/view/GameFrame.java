package view;

import controller.GameController;

import javax.swing.*;

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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(screenWidth, screenHeight);
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                GameController.endGame();
            }
        });


        setVisible(true);
    }
}

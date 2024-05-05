package view;

import controller.GameController;

import javax.swing.*;

public class GameFrame extends JFrame {
    private final int screenWidth = 700; // Assuming initial screen width
    private final int lineSpacing = screenWidth / 4;
    private final int lineWidth = screenWidth / 20;
    private final int firstLineX = (screenWidth - lineSpacing) / 2 - lineWidth / 2;
    private final int secondLineX = firstLineX + lineSpacing;
    private final GameJPanel gameJPanel;

    public GameFrame(String levelName) {
        gameJPanel = new GameJPanel(firstLineX, secondLineX);
        add(gameJPanel);
        setTitle(levelName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 800);
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

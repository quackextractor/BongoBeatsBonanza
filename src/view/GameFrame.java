package view;

import controller.GameController;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(String levelName){

        setTitle(levelName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Window size
        setSize(800, 800);
        // Center the window
        setLocationRelativeTo(null);

        // Ends game on close
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                GameController.endGame();
            }
        });

        setVisible(true);
    }
}

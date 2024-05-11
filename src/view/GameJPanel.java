package view;

import service.ErrorLogger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameJPanel extends JPanel {
    private final int firstLineX;
    private final int secondLineX;
    private Image backgroundImage; // Background image

    public GameJPanel(int firstLineX, int secondLineX) {
        this.firstLineX = firstLineX;
        this.secondLineX = secondLineX;
        try {
            // Load the background image
          backgroundImage = ImageIO.read(new File("src/resources/sprites/cat1.png"));
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
            // Handle error if image loading fails
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, 700, 466, this);
        }
*/
        // Draw vertical lines
        int screenWidth = getWidth();
        int lineHeight = getHeight() / 3;

        // First line
        g.drawLine(firstLineX, 0, firstLineX, getHeight());

        // Second line
        g.drawLine(secondLineX, 0, secondLineX, getHeight());

        // Draw horizontal line in the top third of the screen
        g.drawLine(0, lineHeight, screenWidth, lineHeight);
    }


}

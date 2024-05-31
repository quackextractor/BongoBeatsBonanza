package service;

import view.FancyJLabel;
import view.GameJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The {@code GameUtils} class provides utility methods for the game.
 * It includes methods for preloading images, loading images, setting custom colors, adjusting vertical position,
 * starting a repaint timer, and starting a progress bar timer.
 */
public class GameUtils {

    /**
     * Preloads images for the game panel.
     *
     * @param panel the game panel
     */
    public static void preloadImages(GameJPanel panel) {
        panel.backgroundImage = loadImage("resources/sprites/cat2.png");
        panel.noteImage1 = loadImage("resources/sprites/redNote.png");
        panel.noteImage2 = loadImage("resources/sprites/bluNote.png");
    }

    /**
     * Loads an image from the specified file path.
     *
     * @param imagePath the file path of the image
     * @return the loaded image, or {@code null} if loading fails
     */
    public static BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
            return null;
        }
    }

    /**
     * Sets a custom color for a fancy label.
     *
     * @param fancyJLabel the fancy label
     * @param color       the custom color
     */
    public static void setCustomColor(FancyJLabel fancyJLabel, Color color) {
        fancyJLabel.setShadowColor(color);
        fancyJLabel.setShadowOffset(2);
        fancyJLabel.setShadowOpacity(0.5F);
    }

    /**
     * Adjusts the vertical position of a component.
     *
     * @param gbc the GridBagConstraints for the component
     */
    public static void adjustVertPos(GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;
    }

    /**
     * Starts a timer to repaint the game panel at regular intervals.
     *
     * @param panel the game panel
     */
    public static void startRepaintTimer(GameJPanel panel) {
        Timer repaintTimer = new Timer(GameJPanel.TIMER_DELAY, e -> panel.repaint());
        repaintTimer.start();
    }

    /**
     * Starts a timer to update the progress bar in the game panel at regular intervals.
     *
     * @param panel the game panel
     */
    public static void startProgressBarTimer(GameJPanel panel) {
        Timer progressBarTimer = new Timer(GameJPanel.PROGRESS_BAR_UPDATE_INTERVAL, e -> panel.updateProgressBar());
        progressBarTimer.start();
    }
}

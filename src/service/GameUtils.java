package service;

import view.FancyJLabel;
import view.GameJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static view.GameJPanel.PROGRESS_BAR_UPDATE_INTERVAL;
import static view.GameJPanel.TIMER_DELAY;

public class GameUtils {
    public static void preloadImages(GameJPanel panel) {
        panel.backgroundImage = loadImage("resources/sprites/cat2.png");
        panel.noteImage1 = loadImage("resources/sprites/redNote.png");
        panel.noteImage2 = loadImage("resources/sprites/bluNote.png");
    }

    public static BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
            return null;
        }
    }

    public static void setCustomColor(FancyJLabel fancyJLabel, Color color) {
        fancyJLabel.setShadowColor(color);
        fancyJLabel.setShadowOffset(2);
        fancyJLabel.setShadowOpacity(0.5F);
    }

    public static void adjustVertPos(GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;
    }

    public static void startRepaintTimer(GameJPanel panel) {
        Timer repaintTimer = new Timer(TIMER_DELAY, e -> panel.repaint());
        repaintTimer.start();
    }

    public static void startProgressBarTimer(GameJPanel panel) {
        Timer progressBarTimer = new Timer(PROGRESS_BAR_UPDATE_INTERVAL, e -> panel.updateProgressBar());
        progressBarTimer.start();
    }
}


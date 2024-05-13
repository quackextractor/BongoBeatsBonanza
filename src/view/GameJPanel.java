package view;

import service.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameJPanel extends JPanel {
    private static final int TIMER_DELAY = 10;
    private Timer repaintTimer;

    private final int firstLineX;
    private final int secondLineX;
    private final int horizontalHeight;
    // Background image
    private Image backgroundImage;
    // Note images
    private Image noteImage1;
    private Image noteImage2;
    private NotePool notePool1;
    private NotePool notePool2;
    private MusicTrack musicTrack1;
    private MusicTrack musicTrack2;
    private NoteMovingThread noteMovingThread1;
    private NoteMovingThread noteMovingThread2;

    public GameJPanel(int firstLineX, int secondLineX, int horizontalHeight) {
        this.firstLineX = firstLineX;
        this.secondLineX = secondLineX;
        this.horizontalHeight = horizontalHeight;

        // Preload images
        preloadImages();

        // Initialize note pools, tracks, and threads
        initializeComponents();

        // Start repaint timer
        startRepaintTimer();

        setFocusable(true); // Enable keyboard focus for the JPanel
        requestFocusInWindow(); // Request focus so that the JPanel can receive keyboard events
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyPress(evt);
            }
        });
    }

    private void preloadImages() {
            // Load the images
            backgroundImage = loadImage("src/resources/sprites/cat1.png");
            noteImage1 = loadImage("src/resources/sprites/note1.png");
            noteImage2 = loadImage("src/resources/sprites/note2.png");
    }

    private BufferedImage loadImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
            // Handle error if image loading fails
        }
        return image;
    }

    private void initializeComponents() {
        notePool1 = new NotePool(10);
        notePool2 = new NotePool(10);
        musicTrack1 = new MusicTrack(notePool1, 50, noteImage1, 100, firstLineX, horizontalHeight, 200);
        notePool1.setUpNotePool(200, noteImage1, firstLineX, horizontalHeight, 100);
        musicTrack2 = new MusicTrack(notePool2, 50, noteImage2, 100, secondLineX, horizontalHeight, 200);
        notePool2.setUpNotePool(200, noteImage2, secondLineX, horizontalHeight, 100);

        noteMovingThread1 = new NoteMovingThread(musicTrack1, 1, 10);
        noteMovingThread2 = new NoteMovingThread(musicTrack2, 1, 10);
        noteMovingThread1.start();
        noteMovingThread2.start();
    }

    private void startRepaintTimer() {
        repaintTimer = new Timer(TIMER_DELAY, e -> repaint());
        repaintTimer.start();
    }

    private void handleKeyPress(KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                double accuracy1 = musicTrack1.catchNote();
                System.out.println(accuracy1);
                System.out.println(AccuracyCalculator.getAverageAccuracy());
                break;
            case KeyEvent.VK_RIGHT:
                double accuracy2 = musicTrack2.catchNote();
                System.out.println(accuracy2);
                System.out.println(AccuracyCalculator.getAverageAccuracy());
                break;
            case KeyEvent.VK_UP:
                musicTrack1.addNoteToTrack();
                break;
            case KeyEvent.VK_DOWN:
                musicTrack2.addNoteToTrack();
                break;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw vertical lines
        // First line
        g.drawLine(firstLineX, 0, firstLineX, getHeight());

        // Second line
        g.drawLine(secondLineX, 0, secondLineX, getHeight());

        // Draw horizontal line in the top third of the screen
        g.drawLine(0, horizontalHeight, getWidth(), horizontalHeight);

        // Draw the notes
        musicTrack1.drawNotes(g);
        musicTrack2.drawNotes(g);
    }
}

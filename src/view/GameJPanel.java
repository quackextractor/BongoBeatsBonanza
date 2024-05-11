package view;

import service.ErrorLogger;
import service.NotePool;
import service.Track;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GameJPanel extends JPanel {
    private final int firstLineX;
    private final int secondLineX;
    // Background image
    private Image backgroundImage;
    // Note images
    private Image noteImage1;
    private Image noteImage2;
    private NotePool notePool1;
    private NotePool notePool2;
    private Track track1;
    private Track track2;
    private final int horizontalHeight;

    public GameJPanel(int firstLineX, int secondLineX, int horizontalHeight) {
        this.firstLineX = firstLineX;
        this.secondLineX = secondLineX;
        this.horizontalHeight = horizontalHeight;
        try {
            // Load the images
            backgroundImage = ImageIO.read(new File("src/resources/sprites/cat1.png"));
            noteImage1 = ImageIO.read(new File("src/resources/sprites/note1.png"));
            noteImage2 = ImageIO.read(new File("src/resources/sprites/note2.png"));
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
            // Handle error if image loading fails
        }
        notePool1 = new NotePool(10, noteImage1, firstLineX, horizontalHeight, 100, track1);
        notePool2 = new NotePool(10, noteImage2, secondLineX, horizontalHeight, 100, track2);
        track1 = new Track(notePool1, 30);
        track2 = new Track(notePool2, 30);

        setFocusable(true); // Enable keyboard focus for the JPanel
        requestFocusInWindow(); // Request focus so that the JPanel can receive keyboard events
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyPress(evt);
            }
        });
    }

    private void handleKeyPress(KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                // Left arrow key pressed, call track1.catchNote()
                double accuracy1 = track1.catchNote();
                // Handle accuracy calculation if needed
                break;
            case KeyEvent.VK_RIGHT:
                // Right arrow key pressed, call track2.catchNote()
                double accuracy2 = track2.catchNote();
                // Handle accuracy calculation if needed
                break;
            // Add more cases for other keys if needed
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
        track1.drawNotes(g);
        track2.drawNotes(g);
    }
}

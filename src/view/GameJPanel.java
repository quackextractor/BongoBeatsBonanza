package view;

import controller.GameController;
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

    private Image backgroundImage;
    private Image noteImage1;
    private Image noteImage2;
    private NotePool notePool1;
    private NotePool notePool2;
    private MusicTrack musicTrack1;
    private MusicTrack musicTrack2;
    private NoteMovingThread noteMovingThread1;
    private NoteMovingThread noteMovingThread2;
    private String levelName;
    private MidiPlayer midiPlayer;
    private int spawnDistance;
    private int noteSize;
    private int maxHitDistance;
    private int moveAmount;
    private int moveInterval;
    private JProgressBar progressBar;
    private JProgressBar healthBar;
    private JLabel scoreLabel;
    private JLabel streakLabel;
    private JLabel accuracyLabel;
    private static boolean isGameOver = false;

    public static void setIsGameOver(boolean isGameOver) {
        GameJPanel.isGameOver = isGameOver;
    }

    public static boolean isIsGameOver() {
        return isGameOver;
    }

    public GameJPanel(int firstLineX, int secondLineX, int horizontalHeight, String level) {
        this.firstLineX = firstLineX;
        this.secondLineX = secondLineX;
        this.horizontalHeight = horizontalHeight;
        this.levelName = level;
        isGameOver = false;
        gameOver = false;

        preloadImages();
        initializeComponents();
        initializeUIComponents();
        setFocusable(true);
        requestFocusInWindow();
        Score.reset();
        startGame();
        startRepaintTimer();

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
        noteImage1 = loadImage("src/resources/sprites/redNote.png");
        noteImage2 = loadImage("src/resources/sprites/bluNote.png");
    }

    private void initializeComponents() {
        notePool1 = new NotePool(10);
        notePool2 = new NotePool(10);
        spawnDistance = 2*horizontalHeight;
        noteSize = 100;
        maxHitDistance = noteSize;
        moveAmount = 1;
        moveInterval = 1;

        musicTrack1 = new MusicTrack(notePool1, maxHitDistance, noteImage1, noteSize, firstLineX, horizontalHeight, spawnDistance);
        notePool1.setUpNotePool(spawnDistance, noteImage1, firstLineX, horizontalHeight, noteSize);
        musicTrack2 = new MusicTrack(notePool2, maxHitDistance, noteImage2, noteSize, secondLineX, horizontalHeight, spawnDistance);
        notePool2.setUpNotePool(spawnDistance, noteImage2, secondLineX, horizontalHeight, noteSize);
        noteMovingThread1 = new NoteMovingThread(musicTrack1, moveAmount, moveInterval);
        noteMovingThread2 = new NoteMovingThread(musicTrack2, moveAmount, moveInterval);
        noteMovingThread1.start();
        noteMovingThread2.start();

        int delay = calculateTime(spawnDistance, moveAmount, moveInterval);
        midiPlayer = new MidiPlayer(musicTrack1, musicTrack2, levelName, delay);
    }

    // Method to calculate time in milliseconds
    // TODO fix this later
    public static int calculateTime(int distance, int distancePerMove, int moveDelay) {
        // Calculate total number of moves needed
        int totalMoves = distance / distancePerMove;
        // Calculate total time including move delays
        return totalMoves * moveDelay;
    }

    private void startGame() {
        Thread midiThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            midiPlayer.loadAndPlayMidi();
        });
        midiThread.start();
    }

    public static BufferedImage loadImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
        }
        return image;
    }

    private void startRepaintTimer() {
        repaintTimer = new Timer(TIMER_DELAY, e -> repaint());
        repaintTimer.start();
    }

    private void handleKeyPress(KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                musicTrack1.catchNote();
                break;
            case KeyEvent.VK_RIGHT:
                musicTrack2.catchNote();
                break;
        }
    }

    public void updateMyUI() {
        scoreLabel.setText("Score: " + Score.getTotalScore());
        streakLabel.setText("Streak: " + Score.getStreakCount());
        double averageAccuracy = Score.getAverageAccuracy();
        String formattedAccuracy = String.format("%.2f", averageAccuracy);
        accuracyLabel.setText("Accuracy: " + formattedAccuracy + "%");
        accuracyLabel.setText("Accuracy: " + formattedAccuracy + "%");
        healthBar.setValue(Score.getHealth());
    }

    private void initializeUIComponents() {
        // Initialize UI elements
        progressBar = new JProgressBar();
        healthBar = new JProgressBar(1, 0, 100);
        scoreLabel = new JLabel("Score: 0");
        streakLabel = new JLabel("Streak: 0");
        accuracyLabel = new JLabel("Accuracy: 100%");

        // Add UI elements to panel
        setLayout(new BorderLayout());
        JPanel uiPanel = new JPanel();
        uiPanel.setLayout(new GridLayout(5, 1));
        uiPanel.add(scoreLabel);
        uiPanel.add(streakLabel);
        uiPanel.add(accuracyLabel);
        add(uiPanel, BorderLayout.EAST);
        add(progressBar, BorderLayout.SOUTH);
        add(healthBar, BorderLayout.WEST);
    }


    // Define a boolean flag to track if the game is over
    private boolean gameOver = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.drawLine(firstLineX, 0, firstLineX, getHeight());
        g.drawLine(secondLineX, 0, secondLineX, getHeight());
        g.drawLine(0, horizontalHeight, getWidth(), horizontalHeight);

        // drawing catching field
        // TODO replace this with an image
        int offset = noteSize / 2;
        g.drawRect(firstLineX - offset, horizontalHeight - offset, noteSize, noteSize);
        g.drawRect(secondLineX - offset, horizontalHeight - offset, noteSize, noteSize);

        musicTrack1.drawNotes(g);
        musicTrack2.drawNotes(g);

        if (gameOver) {
            return;
        }

        if (isGameOver) {
            gameOver = true; // Set the flag to true when game over is triggered
            noteMovingThread1.stopMoving();
            noteMovingThread2.stopMoving();
            midiPlayer.stopMusic();
            LevelSelectionFrame.setIsOpen(false);
            GameOverScreen gameOverScreen = new GameOverScreen(levelName);
            gameOverScreen.setIconImage(GameController.getGameIcon());
        }

        updateMyUI();
    }
}
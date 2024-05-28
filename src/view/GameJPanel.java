package view;

import controller.GameController;
import service.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameJPanel extends JPanel {
    private static final int TIMER_DELAY = 10;
    private static final int PROGRESS_BAR_UPDATE_INTERVAL = 100;
    private static final int INITIAL_NOTE_POOL_SIZE = 10;

    private Timer repaintTimer;
    private Timer progressBarTimer;
    private final int firstLineX;
    private final int secondLineX;
    private final int horizontalHeight;
    private final String levelName;

    private Image backgroundImage;
    private Image noteImage1;
    private Image noteImage2;
    private NotePool notePool1;
    private NotePool notePool2;
    private MusicTrack musicTrack1;
    private MusicTrack musicTrack2;
    private NoteMovingThread noteMovingThread1;
    private NoteMovingThread noteMovingThread2;
    private MidiPlayer midiPlayer;
    private int spawnDistance;
    private int noteSize;
    private int maxHitDistance;
    private static int moveAmount;
    private static int moveInterval;
    private JProgressBar progressBar;
    private JProgressBar healthBar;
    private JLabel scoreLabel;
    private JLabel streakLabel;
    private JLabel accuracyLabel;
    private static boolean isGameOver = false;
    private boolean gameOver = false;

    public static void modifyMoveParams(int x) {
        switch (x) {
            case 1:
                moveAmount = 1;
                moveInterval = 2;
                break;
            case 2:
                moveAmount = 1;
                moveInterval = 1;
                break;
            case 3:
                moveAmount = 2;
                moveInterval = 1;
                break;
            default:
                throw new IllegalArgumentException("input ranging from 1-3");
        }
    }

    public static void setGameOver() {
        Score.changeHealth(-100);
    }

    public static boolean isGameOver() {
        return Score.getHealth() == 0;
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
            public void keyPressed(KeyEvent evt) {
                handleKeyPress(evt);
            }
        });
    }

    private void preloadImages() {
        backgroundImage = loadImage("resources/sprites/cat1.png");
        noteImage1 = loadImage("resources/sprites/redNote.png");
        noteImage2 = loadImage("resources/sprites/bluNote.png");
    }

    private void initializeComponents() {
        notePool1 = new NotePool(INITIAL_NOTE_POOL_SIZE);
        notePool2 = new NotePool(INITIAL_NOTE_POOL_SIZE);
        spawnDistance = 2 * horizontalHeight;
        noteSize = 100;
        maxHitDistance = noteSize;

        if (moveAmount == 0) {
            moveAmount = 1;
            moveInterval = 1;
        }

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

    public static int calculateTime(int distance, int distancePerMove, int moveDelay) {
        return (distance / distancePerMove) * moveDelay;
    }

    private void startGame() {
        Thread midiThread = new Thread(() -> {
            midiPlayer.loadAndPlayMidi();
            startProgressBarTimer();
        });
        midiThread.start();
    }

    public static BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            ErrorLogger.logStackTrace(e);
            return null;
        }
    }

    private void startRepaintTimer() {
        repaintTimer = new Timer(TIMER_DELAY, e -> repaint());
        repaintTimer.start();
    }

    private void startProgressBarTimer() {
        progressBarTimer = new Timer(PROGRESS_BAR_UPDATE_INTERVAL, e -> updateProgressBar());
        progressBarTimer.start();
    }

    private void handleKeyPress(KeyEvent evt) {
        switch (evt.getKeyCode()) {
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
        healthBar.setValue(Score.getHealth());
        updateHealthBarColor();
    }

    private void updateHealthBarColor() {
        int value = healthBar.getValue();
        Color color;
        if (value > 80) {
            color = Color.GREEN;
        } else if (value > 60) {
            color = new Color(255, 165, 0);
        } else if (value > 40) {
            color = new Color(255, 204, 51);
        } else if (value > 20) {
            color = Color.RED;
        } else {
            color = new Color(165, 42, 42);
        }
        healthBar.setForeground(color);
    }

    private void initializeUIComponents() {
        progressBar = new JProgressBar();
        healthBar = new JProgressBar(1, 0, 100);
        scoreLabel = new JLabel("Score: 0");
        streakLabel = new JLabel("Streak: 0");
        accuracyLabel = new JLabel("Accuracy: 100%");

        healthBar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.RED;
            }

            protected Color getSelectionForeground() {
                return Color.RED;
            }
        });

        setLayout(new BorderLayout());
        JPanel uiPanel = new JPanel();
        uiPanel.setLayout(new GridLayout(5, 1));
        uiPanel.add(scoreLabel);
        uiPanel.add(streakLabel);
        uiPanel.add(accuracyLabel);
        add(uiPanel, BorderLayout.EAST);
        add(progressBar, BorderLayout.SOUTH);
        add(healthBar, BorderLayout.WEST);

        updateHealthBarColor();
    }

    private void updateProgressBar() {
        if (midiPlayer != null) {
            long currentTime = midiPlayer.getMicroSecondPos();
            long totalTime = midiPlayer.getMicroSecondLength();
            if (totalTime > 0) {
                int progress = (int) ((double) currentTime / totalTime * 100);
                progressBar.setValue(progress);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.drawLine(firstLineX, 0, firstLineX, getHeight());
        g.drawLine(secondLineX, 0, secondLineX, getHeight());
        g.drawLine(0, horizontalHeight, getWidth(), horizontalHeight);

        int offset = noteSize / 2;
        g.drawOval(firstLineX - offset, horizontalHeight - offset, noteSize, noteSize);
        g.drawOval(secondLineX - offset, horizontalHeight - offset, noteSize, noteSize);

        musicTrack1.drawNotes(g);
        musicTrack2.drawNotes(g);

        if ((Score.getHealth() <= 0 || Score.isComplete()) && !gameOver) {
            gameOver = true;
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

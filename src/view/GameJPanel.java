package view;

import controller.GameController;
import service.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.metal.MetalProgressBarUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * JPanel for the main game interface.
 */
public class GameJPanel extends JPanel {
    public static final int TIMER_DELAY = 10;
    public static final int PROGRESS_BAR_UPDATE_INTERVAL = 100;
    private static final int INITIAL_NOTE_POOL_SIZE = 10;
    private static List<Ring> rings;

    private final int firstLineX;
    private final int secondLineX;
    private final int horizontalHeight;
    private final String levelName;
    private final GameFrame gameFrame;

    public Image backgroundImage;
    public Image noteImage1;
    public Image noteImage2;
    public MusicTrack musicTrack1;
    public MusicTrack musicTrack2;
    private NoteMovingThread noteMovingThread1;
    private NoteMovingThread noteMovingThread2;
    private MidiPlayer midiPlayer;
    private int noteSize;
    private static int moveAmount;
    private static int moveInterval;
    private JProgressBar progressBar;
    private JProgressBar healthBar;
    private boolean gameOver;
    private static int track1KeyCode;
    private static int track2KeyCode;

    /**
     * Modifies movement parameters for notes.
     *
     * @param x Movement parameter value (1-3).
     */
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
                throw new IllegalArgumentException("Input must range from 1 to 3");
        }
    }

    /**
     * Sets the game state to game over.
     */
    public static void setGameOver() {
        Score.changeHealth(-1000);
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise.
     */
    public static boolean isGameOver() {
        return Score.getHealth() == 0;
    }

    /**
     * Constructs a GameJPanel with specified parameters.
     *
     * @param firstLineX       X-coordinate of the first line.
     * @param secondLineX      X-coordinate of the second line.
     * @param horizontalHeight Height of the horizontal line.
     * @param level            The name of the level.
     * @param gameFrame        The game frame instance.
     */
    public GameJPanel(int firstLineX, int secondLineX, int horizontalHeight, String level, GameFrame gameFrame) {
        this.firstLineX = firstLineX;
        this.secondLineX = secondLineX;
        this.horizontalHeight = horizontalHeight;
        this.levelName = level;
        this.gameFrame = gameFrame;

        gameOver = false;
        rings = new ArrayList<>();

        GameUtils.preloadImages(this);
        initializeComponents();
        initializeUIComponents();
        setFocusable(true);
        requestFocusInWindow();
        Score.reset();
        startGame();
        GameUtils.startRepaintTimer(this);

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                handleKeyPress(evt);
            }
        });
    }

    /**
     * Handles key presses during the game.
     *
     * @param evt The KeyEvent instance.
     */
    public void handleKeyPress(KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT || keyCode == track1KeyCode) {
            musicTrack1.catchNote();
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == track2KeyCode) {
            musicTrack2.catchNote();
        }
    }

    private void initializeComponents() {
        NotePool notePool1 = new NotePool(INITIAL_NOTE_POOL_SIZE);
        NotePool notePool2 = new NotePool(INITIAL_NOTE_POOL_SIZE);
        int spawnDistance = 2 * horizontalHeight;
        noteSize = 100;
        int maxHitDistance = noteSize;

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

    /**
     * Calculates time based on distance and movement parameters.
     * Used to get the correct delay for the {@link MidiPlayer}.
     *
     * @param distance         The distance.
     * @param distancePerMove  The distance per move.
     * @param moveDelay        The move delay.
     * @return Calculated time.
     */
    public static int calculateTime(int distance, int distancePerMove, int moveDelay) {
        return (distance / distancePerMove) * moveDelay;
    }

    /**
     * Starts the game by loading and playing the MIDI track.
     */
    private void startGame() {
        Thread midiThread = new Thread(() -> {
            midiPlayer.loadAndPlayMidi();
            GameUtils.startProgressBarTimer(this);
        });
        midiThread.start();
    }

    /**
     * Updates the user interface with the current health value.
     */
    public void updateMyUI() {
        healthBar.setValue(Score.getHealth());
        updateHealthBarColor();
    }

    /**
     * Adds a ring to the list of rings.
     *
     * @param ring The ring to add.
     */
    public static void addRing(Ring ring) {
        rings.add(ring);
    }

    /**
     * Updates the state of rings, removing expired ones.
     */
    private void updateRings() {
        List<Ring> expiredRings = new ArrayList<>();
        for (Ring ring : rings) {
            ring.update();
            if (ring.isExpired()) {
                expiredRings.add(ring);
            }
        }
        rings.removeAll(expiredRings);
    }

    /**
     * Updates the color of the health bar based on the current health value.
     */
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

    /**
     * Initializes the UI components for later manipulation.
     */
    private void initializeUIComponents() {
        progressBar = new JProgressBar();
        healthBar = new JProgressBar(1, 0, 100);
        healthBar = new JProgressBar(1, 0, 100);

        healthBar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.RED;
            }

            protected Color getSelectionForeground() {
                return Color.RED;
            }
        });

        // Set foreground color of progressBar to blue
        progressBar.setUI(new MetalProgressBarUI());
        progressBar.setForeground(Color.BLUE);

        setLayout(new BorderLayout());
        JPanel uiPanel = new JPanel();
        uiPanel.setLayout(new GridLayout(2, 1));
        add(uiPanel, BorderLayout.EAST);
        add(progressBar, BorderLayout.SOUTH);
        add(healthBar, BorderLayout.WEST);

        updateHealthBarColor();
    }

    /**
     * Sets the key code for track 2.
     *
     * @param keyCode The key code.
     */
    public static void setTrack2Key(int keyCode) {
        track2KeyCode = keyCode;
    }
    /**
     * Sets the key code for track 1.
     *
     * @param keyCode The key code.
     */
    public static void setTrack1Key(int keyCode) {
        track1KeyCode = keyCode;
    }

    /**
     * Updates the progress bar based on the current position of the MIDI track.
     */
    public void updateProgressBar() {
        if (midiPlayer != null) {
            long currentTime = midiPlayer.getMicroSecondPos();
            long totalTime = midiPlayer.getMicroSecondLength();
            if (totalTime > 0) {
                int progress = (int) ((double) currentTime / totalTime * 100);
                progressBar.setValue(progress);
            }
            repaint();
        }
    }


    /**
     * Overrides the paintComponent method to draw the game elements on the panel.
     *
     * @param g The Graphics context.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image if available
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw rings first
        for (Ring ring : rings) {
            ring.draw(g);
        }

        // Calculate offset for notes
        int offset = noteSize / 2;

        // Draw first vertical line
        g.drawLine(firstLineX, 0, firstLineX, horizontalHeight - offset);
        g.drawLine(firstLineX, horizontalHeight + offset, firstLineX, 3 * horizontalHeight);

        // Draw second vertical line
        g.drawLine(secondLineX, 0, secondLineX, horizontalHeight - offset);
        g.drawLine(secondLineX, horizontalHeight + offset, secondLineX, 3 * horizontalHeight);

        // Draw horizontal line
        g.drawLine(0, horizontalHeight, firstLineX - offset, horizontalHeight);
        g.drawLine(getWidth(), horizontalHeight, secondLineX + offset, horizontalHeight);
        g.drawLine(firstLineX + offset, horizontalHeight, secondLineX - offset, horizontalHeight);

        // Draw note circles
        g.drawOval(firstLineX - offset, horizontalHeight - offset, noteSize, noteSize);
        g.drawOval(secondLineX - offset, horizontalHeight - offset, noteSize, noteSize);

        // Draw notes for both tracks
        musicTrack1.drawNotes(g);
        musicTrack2.drawNotes(g);

        // Check for game over conditions
        if ((Score.getHealth() <= 0 || Score.isComplete()) && !gameOver) {
            gameOver = true;
            noteMovingThread1.stopMoving();
            noteMovingThread2.stopMoving();
            midiPlayer.stopMusic();
            LevelSelectionFrame.setIsOpen(false);
            GameOverScreen gameOverScreen;
            if (Score.isComplete()) {
                gameOverScreen = new GameOverScreen(levelName, "Finish", Color.GREEN);
            } else {
                gameOverScreen = new GameOverScreen(levelName, "Game Over", Color.RED);
            }
            gameOverScreen.setIconImage(GameController.getGameIcon());
        }

        // Update UI elements
        gameFrame.updateMyUI();
        updateMyUI();
        updateRings();
    }
}

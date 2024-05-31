package view;

import controller.GameController;
import service.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the frame for selecting a level to play in the game.
 * Allows the player to choose from available levels and start the game.
 */
public class LevelSelectionFrame extends JFrame {
    /**
     * Static boolean to track if the window is open.
     */
    public static boolean isOpen = false;
    private List<String> levelNames;
    private JComboBox<String> levelComboBox;
    private MusicPlayer fxPlayer;
    /**
     * Static boolean to track if a level has started.
     */
    public static boolean hasLevelStarted = false;

    /**
     * Constructs a LevelSelectionFrame.
     * Initializes the frame, loads available levels, and sets up the user interface components.
     */
    public LevelSelectionFrame() {
        // Check if the frame is already open
        if (isOpen) {
            requestFocus();
            return;
        }
        hasLevelStarted = false;
        isOpen = true;
        fxPlayer = new MusicPlayer(false, "");
        setTitle("Level Selection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        loadLevels();

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JLabel label = new JLabel("Select a level:");
        panel.add(label, BorderLayout.NORTH);

        levelComboBox = new JComboBox<>(levelNames.toArray(new String[0]));
        panel.add(levelComboBox, BorderLayout.CENTER);

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> {
            String selectedLevel = (String) levelComboBox.getSelectedItem();
            if (selectedLevel != null) {
                // Start the game with the selected level
                startGame(selectedLevel, this);
            }
        });

        playButton.setPreferredSize(new Dimension(100, 50));

        panel.add(playButton, BorderLayout.SOUTH);

        // Add window listener to handle window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!hasLevelStarted) {
                    fxPlayer.play("resources/sounds/exit.wav");
                    isOpen = false;
                }
            }
        });

        setVisible(true);
    }

    /**
     * Loads available levels from the resources/levels directory.
     * Only considers files with .mid extension and checks for corresponding .wav files.
     */
    private void loadLevels() {
        levelNames = new ArrayList<>();
        File levelsDirectory = new File("resources/levels");
        if (levelsDirectory.exists() && levelsDirectory.isDirectory()) {
            File[] levelFiles = levelsDirectory.listFiles();
            if (levelFiles != null) {
                for (File file : levelFiles) {
                    if (file.isFile() && file.getName().endsWith(".mid")) {
                        String fileName = file.getName().replace(".mid", "");
                        // Check if there's a corresponding .wav file
                        File wavFile = new File(levelsDirectory, fileName + ".wav");
                        if (wavFile.exists()) {
                            levelNames.add(fileName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Starts the game with the selected level.
     * Displays an error message if the level fails to start.
     *
     * @param selectedLevel The name of the selected level.
     * @param component The component triggering the start action.
     */
    public static void startGame(String selectedLevel, Component component) {
        if (!GameController.startLevel(selectedLevel)) {
            // Display error message as a tooltip
            MusicPlayer fxPlayer = new MusicPlayer(false, "resources/sounds/alert.wav");
            fxPlayer.playDefault();
            JOptionPane.showMessageDialog(component, "Error starting the selected level", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets the state of whether the level selection frame is open or closed.
     *
     * @param isOpen True if the frame is open, otherwise false.
     */
    public static void setIsOpen(boolean isOpen) {
        LevelSelectionFrame.isOpen = isOpen;
    }
}

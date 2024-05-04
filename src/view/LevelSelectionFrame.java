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

public class LevelSelectionFrame extends JFrame {
    private static boolean isOpen = false; // Static boolean to track if the window is open
    private List<String> levelNames;
    private JComboBox<String> levelComboBox;
    private MusicPlayer musicPlayer;
    public static boolean hasLevelStarted = false;

    public LevelSelectionFrame() {
        if (isOpen) {
            requestFocus();
            return;
        }
        hasLevelStarted = false;
        isOpen = true;
        musicPlayer = new MusicPlayer();
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
                startGame(selectedLevel);
            }
        });
        panel.add(playButton, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!hasLevelStarted) {
                    musicPlayer.playFX("src/resources/sounds/exit.wav");
                    isOpen = false;
                }
            }
        });

        setVisible(true);
    }

    private void loadLevels() {
        levelNames = new ArrayList<>();
        File levelsDirectory = new File("src/resources/levels");
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

    private void startGame(String selectedLevel) {
        if (!GameController.startLevel(selectedLevel)) {
            // Display error message as a tooltip
            musicPlayer.playFX("src/resources/sounds/alert.wav");
            JOptionPane.showMessageDialog(this, "Error starting the selected level", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.GameController;
import service.MusicPlayer;

public class LevelSelectionFrame extends JFrame {
    private static boolean isOpen = false; // Static boolean to track if the window is open
    private List<String> levelNames;
    private JComboBox<String> levelComboBox;
    private JButton playButton;
    private MusicPlayer musicPlayer;

    public LevelSelectionFrame() {
        if (isOpen) {
            requestFocus();
            return;
        }
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

        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLevel = (String) levelComboBox.getSelectedItem();
                if (selectedLevel != null) {
                    // Start the game with the selected level
                    startGame(selectedLevel);
                }
            }
        });
        panel.add(playButton, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                musicPlayer.play("src/resources/sounds/exit.wav");
                isOpen = false;
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
            musicPlayer.play("src/resources/sounds/alert.wav");
            JOptionPane.showMessageDialog(this, "Error starting the selected level", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            musicPlayer.play("src/resources/sounds/start.wav");
        }

    }
}

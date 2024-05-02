package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class LevelSelectionFrame extends JFrame {
    private List<String> levelNames;
    private JComboBox<String> levelComboBox;
    private JButton playButton;

    public LevelSelectionFrame() {
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

        setVisible(true);
    }

    private void loadLevels() {
        levelNames = new ArrayList<>();
        File levelsDirectory = new File("Bongo_Beats_Bonanza/resources/levels/");
        if (levelsDirectory.exists() && levelsDirectory.isDirectory()) {
            File[] levelFiles = levelsDirectory.listFiles();
            if (levelFiles != null) {
                for (File file : levelFiles) {
                    if (file.isFile() && file.getName().endsWith(".mid")) {
                        levelNames.add(file.getName());
                    }
                }
            }
        }
    }

    private void startGame(String selectedLevel) {
        // Start the game with the selected level
        // You can implement this method according to your game logic
        // For example:
        // GameController.startGame(selectedLevel);
        // Or you can open a new frame for the game
        // For example:
        // GameFrame gameFrame = new GameFrame(selectedLevel);
        // gameFrame.setVisible(true);
    }
}

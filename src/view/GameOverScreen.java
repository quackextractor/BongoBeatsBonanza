package view;

import controller.GameController;
import service.MidiPlayer;
import service.MusicPlayerManager;
import service.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverScreen extends JFrame {
    private String level;
    private int maxScore;
    private int achievedScore;
    private double accuracy;
    private int greatCount;
    private int goodCount;
    private int badCount;
    private int missCount;
    private int highestStreak;

    public GameOverScreen(String level) {
        MusicPlayerManager.stopAllMusicPlayers();
        this.level = level;
        this.maxScore = MidiPlayer.getTotalNotes() * 100;
        this.achievedScore = Score.getTotalScore();
        this.accuracy = Score.getAverageAccuracy();
        this.greatCount = Score.getGreatCount();
        this.goodCount = Score.getGoodCount();
        this.badCount = Score.getBadCount();
        this.missCount = Score.getMissCount();
        this.highestStreak = Score.getHighestStreak();

        setTitle("Game Over");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(8, 1));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        FancyJLabel titleLabel = new FancyJLabel("Game Over");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(titleLabel);

        FancyJLabel gradeLabel = new FancyJLabel(getGrade());
        gradeLabel.setForeground(Color.WHITE);
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gradeLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(gradeLabel);

        FancyJLabel accuracyLabel = new FancyJLabel("Accuracy: " + String.format("%.2f", accuracy) + "%");
        accuracyLabel.setForeground(Color.WHITE);
        accuracyLabel.setFont(new Font("Arial", Font.BOLD, 20));
        accuracyLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(accuracyLabel);

        FancyJLabel streakLabel;
        if (highestStreak == MidiPlayer.getTotalNotes()) {
            streakLabel = new FancyJLabel("Highest Streak: Full Streak!");
        } else {
            streakLabel = new FancyJLabel("Highest Streak: " + highestStreak);
        }
        streakLabel.setForeground(Color.WHITE);
        streakLabel.setFont(new Font("Arial", Font.BOLD, 20));
        streakLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(streakLabel);

        FancyJLabel greatCountLabel = new FancyJLabel("Great Count: " + greatCount);
        greatCountLabel.setForeground(Color.WHITE);
        greatCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        greatCountLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(greatCountLabel);

        FancyJLabel goodCountLabel = new FancyJLabel("Good Count: " + goodCount);
        goodCountLabel.setForeground(Color.WHITE);
        goodCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        goodCountLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(goodCountLabel);

        FancyJLabel badCountLabel = new FancyJLabel("Bad Count: " + badCount);
        badCountLabel.setForeground(Color.WHITE);
        badCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        badCountLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(badCountLabel);

        FancyJLabel missCountLabel = new FancyJLabel("Miss Count: " + missCount);
        missCountLabel.setForeground(Color.WHITE);
        missCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        missCountLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(missCountLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Right Panel
        ImageIcon imageIcon = new ImageIcon("src/resources/sprites/cat0.png");
        JLabel imageLabel = new JLabel(imageIcon);
        mainPanel.add(imageLabel, BorderLayout.EAST);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Adjust horizontal and vertical gaps as needed
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add padding

        // Retry Button
        JButton retryButton = new JButton("Retry");
        retryButton.setPreferredSize(new Dimension(200, 60)); // Set preferred size
        retryButton.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameController.startLevel(level);
            }
        });
        bottomPanel.add(retryButton);

        // Quit to Menu Button
        JButton quitToMenuButton = new JButton("Quit to Menu");
        quitToMenuButton.setPreferredSize(new Dimension(200, 60)); // Set preferred size
        quitToMenuButton.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        quitToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MusicPlayerManager.stopAllMusicPlayers();
                GameController.startGame();
                GameController.closeAllFrames();
            }
        });
        bottomPanel.add(quitToMenuButton);

        // Quit to Desktop Button
        JButton quitToDesktopButton = new JButton("Quit to Desktop");
        quitToDesktopButton.setPreferredSize(new Dimension(200, 60)); // Set preferred size
        quitToDesktopButton.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        quitToDesktopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameController.endGame();
            }
        });
        bottomPanel.add(quitToDesktopButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private String getGrade() {
        double percentage = (double) achievedScore / maxScore * 100;

        if (percentage >= 95) {
            return "S";
        } else if (percentage >= 80) {
            return "A";
        } else if (percentage >= 60) {
            return "B";
        } else if (percentage >= 40) {
            return "C";
        } else if (percentage >= 20) {
            return "D";
        } else {
            return "F";
        }
    }
}

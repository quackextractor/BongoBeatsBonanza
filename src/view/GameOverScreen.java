package view;

import controller.GameController;
import service.MidiPlayer;
import service.MusicPlayerManager;
import service.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverScreen extends JFrame {
    private int maxScore;
    private int achievedScore;
    private double accuracy;
    private int greatCount;
    private int goodCount;
    private int badCount;
    private int missCount;
    private int highestStreak;
    private final String title;
    private final Color titleColor;

    public GameOverScreen(String level, String title, Color titleColor) {
        this.title = title;
        this.titleColor = titleColor;
        initScores();
        setupFrame();
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createImagePanel(), BorderLayout.EAST);
        mainPanel.add(createBottomPanel(level), BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void initScores() {
        MusicPlayerManager.stopAllMusicPlayers();
        this.maxScore = MidiPlayer.getTotalNotes() * 80;
        this.achievedScore = Score.getTotalScore();
        this.accuracy = Score.getAverageAccuracy();
        this.greatCount = Score.getGreatCount();
        this.goodCount = Score.getGoodCount();
        this.badCount = Score.getBadCount();
        this.missCount = Score.getMissCount();
        this.highestStreak = Score.getHighestStreak();
    }

    private void setupFrame() {
        setTitle(title);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(8, 1));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        centerPanel.add(createLabel(title, titleColor, 40));
        centerPanel.add(createLabel(getGrade(), Color.WHITE, 30));
        centerPanel.add(createLabel("Accuracy: " + String.format("%.2f", accuracy) + "%", Color.WHITE, 20));
        centerPanel.add(createLabel(highestStreak == MidiPlayer.getTotalNotes() ? "Highest Streak: Full Streak!" : "Highest Streak: " + highestStreak, Color.WHITE, 20));
        centerPanel.add(createLabel("Great Count: " + greatCount, Color.WHITE, 20));
        centerPanel.add(createLabel("Good Count: " + goodCount, Color.WHITE, 20));
        centerPanel.add(createLabel("Bad Count: " + badCount, Color.WHITE, 20));
        centerPanel.add(createLabel("Miss Count: " + missCount, Color.WHITE, 20));

        return centerPanel;
    }

    private JLabel createLabel(String text, Color color, int fontSize) {
        FancyJLabel label = new FancyJLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    private JPanel createImagePanel() {
        ImageIcon imageIcon = new ImageIcon("resources/sprites/cat0.png");
        JLabel imageLabel = new JLabel(imageIcon);
        JPanel imagePanel = new JPanel();
        imagePanel.add(imageLabel);
        return imagePanel;
    }

    private JPanel createBottomPanel(String level) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        bottomPanel.add(createButton("Retry", e -> GameController.startLevel(level)));
        bottomPanel.add(createButton("Quit to Menu", e -> {
            MusicPlayerManager.stopAllMusicPlayers();
            GameController.startGame();
            GameController.closeAllFrames();
        }));
        bottomPanel.add(createButton("Quit to Desktop", e -> GameController.endGame()));

        return bottomPanel;
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 60));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.addActionListener(actionListener);
        return button;
    }

    private String getGrade() {
        double percentage = (double) achievedScore / maxScore * 100;
        if (percentage >= 90) {
            return "S";
        } else if (percentage >= 70) {
            return "A";
        } else if (percentage >= 50) {
            return "B";
        } else if (percentage >= 30) {
            return "C";
        } else if (percentage >= 10) {
            return "D";
        } else {
            return "F";
        }
    }
}

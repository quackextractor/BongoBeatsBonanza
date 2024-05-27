package view;

import controller.GameController;
import service.MusicPlayer;
import service.MusicPlayerManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TitleFrame extends JFrame {

    private final MusicPlayer musicPlayer;
    private final MusicPlayer fxPlayer;
    private JLabel animationLabel;
    private ImageIcon[] animationFrames;
    private int currentFrameIndex;

    public TitleFrame(String musicPath, String fontName) {
        setTitle("Bongo Beats Bonanza");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Window size
        setSize(800, 800);
        // Center the window
        setLocationRelativeTo(null);

        FancyJLabel play = new FancyJLabel("Play", JLabel.LEFT);
        FancyJLabel settings = new FancyJLabel("Settings", JLabel.LEFT);
        FancyJLabel quit = new FancyJLabel("Quit", JLabel.LEFT);

        FancyJLabel[] buttons = {play, settings, quit};

        Font font = new Font(fontName, Font.BOLD, 80);

        musicPlayer = new MusicPlayer(true, musicPath);
        musicPlayer.playDefault();
        MusicPlayerManager.addMusicPlayer(musicPlayer);
        fxPlayer = new MusicPlayer(false, "");

        int spacing = 10;  // Define the spacing from the left side of the screen

        setLayout(new BorderLayout());
        double logoScale = 2;
        int width = 400;
        int height = 100;
        initializeAnimationFrames((int) (width * logoScale), (int) (height * logoScale));
        createAnimationLabel();
        startAnimation();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));
        for (FancyJLabel label : buttons) {
            label.setFont(font);
            label.setOpaque(true);  // To make the label's background visible
            label.setBackground(Color.LIGHT_GRAY);  // Set background color
            label.setForeground(Color.BLACK);  // Set text color

            // Wrap each label in a panel to apply padding
            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.setBorder(new EmptyBorder(0, spacing, 0, 0));  // Add left padding
            labelPanel.add(label, BorderLayout.CENTER);

            buttonPanel.add(labelPanel);
        }

        add(animationLabel, BorderLayout.PAGE_START);
        add(buttonPanel, BorderLayout.CENTER);

        play.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                fxPlayer.play("src/resources/sounds/click.wav");
                LevelSelectionFrame levelSelectionFrame = new LevelSelectionFrame();
                levelSelectionFrame.setIconImage(GameController.getGameIcon());
            }
        });

        quit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!LevelSelectionFrame.hasLevelStarted) {
                    musicPlayer.stop();
                    GameController.endGame();
                }
            }
        });

        settings.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                fxPlayer.play("src/resources/sounds/click.wav");
                SettingsFrame settingsFrame = new SettingsFrame(fontName, musicPlayer);
                settingsFrame.setIconImage(GameController.getGameIcon());
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (!LevelSelectionFrame.hasLevelStarted) {
                    musicPlayer.stop();
                    GameController.endGame();
                }
            }
        });

        setVisible(true);
    }

    private void initializeAnimationFrames(int width, int height) {
        animationFrames = new ImageIcon[5];
        for (int i = 0; i < 5; i++) {
            Image img = new ImageIcon("src/resources/sprites/title" + i + ".png").getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            animationFrames[i] = new ImageIcon(scaledImg);
        }
    }

    private void createAnimationLabel() {
        animationLabel = new JLabel(animationFrames[0]);
        animationLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    private void startAnimation() {
        Timer animationTimer = new Timer(300, e -> animate());
        animationTimer.start();
    }

    private void animate() {
        currentFrameIndex = (currentFrameIndex + 1) % animationFrames.length;
        animationLabel.setIcon(animationFrames[currentFrameIndex]);
    }
}

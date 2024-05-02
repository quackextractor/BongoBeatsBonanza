package view;

import controller.GameController;
import service.MusicPlayer;

import javax.swing.*;
import java.awt.*;

// TODO: CODE CLEANUP, FOREACH LOOPS FOR SIMPLIFICATION

public class TitleFrame extends JFrame {

    private JButton play;
    private JButton quit;
    private JButton settings;
    private boolean isPlaying;
    private MusicPlayer musicPlayer;
    private JLabel animationLabel;
    private ImageIcon[] animationFrames;
    private Timer animationTimer;
    private int currentFrameIndex;

    public TitleFrame(String musicPath, String fontName) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        setTitle("Bongo Beats Bonanza");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Window size
        setSize(800, 800);
        // Center the window
        setLocationRelativeTo(null);

        play = new JButton("Play");
        quit = new JButton("Quit");
        settings = new JButton("Settings");

        Font font = new Font(fontName, Font.BOLD, 120);

        play.setFont(font);
        settings.setFont(font);
        quit.setFont(font);

        musicPlayer = new MusicPlayer();
        isPlaying = false;

        setLayout(new GridLayout(4, 1));

        initializeAnimationFrames();
        createAnimationLabel();
        startAnimation();

        add(play);
        add(settings);
        add(quit);

        play.addActionListener(e -> {
            if (isPlaying) {
                musicPlayer.pause();
                isPlaying = false;
                play.setText("Play");
            } else {
                musicPlayer.play(musicPath);
                isPlaying = true;
                play.setText("Pause");
            }
        });

        quit.addActionListener(e -> {
            musicPlayer.stop();
            GameController.endGame();
        });

        settings.addActionListener(e -> {
            play.setText("Play");
            isPlaying = false;
            musicPlayer.pause();
            SettingsFrame settingsFrame = new SettingsFrame(fontName);
        });
        setVisible(true);
    }

    private void initializeAnimationFrames() {
        animationFrames = new ImageIcon[4];
        for (int i = 0; i < 4; i++) {
            animationFrames[i] = new ImageIcon("src/resources/sprites/title" + i + ".png");
        }
    }

    private void createAnimationLabel() {
        animationLabel = new JLabel(animationFrames[0]);

        animationLabel.setBounds(0, 0, 800, 600);
        add(animationLabel);
    }

    private void startAnimation() {
        animationTimer = new Timer(100, e -> animate());
        animationTimer.start();
    }

    private void animate() {
        currentFrameIndex = (currentFrameIndex + 1) % animationFrames.length;
        animationLabel.setIcon(animationFrames[currentFrameIndex]);
    }
}

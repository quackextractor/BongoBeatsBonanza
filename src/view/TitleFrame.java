package view;

import controller.GameController;
import service.MusicPlayer;
import service.MusicPlayerManager;

import javax.swing.*;
import java.awt.*;

public class TitleFrame extends JFrame {

    //  private boolean isPlaying;
    private final MusicPlayer musicPlayer;
    private JLabel animationLabel;
    private ImageIcon[] animationFrames;
    private int currentFrameIndex;

    public TitleFrame(String musicPath, String fontName) {
        setTitle("Bongo Beats Bonanza");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Window size
        setSize(800, 800);
        // Center the window
        setLocationRelativeTo(null);

        JButton play = new JButton("Play");
        JButton settings = new JButton("Settings");
        JButton quit = new JButton("Quit");

        JButton[] buttons = {play, settings, quit};

        Font font = new Font(fontName, Font.BOLD, 120);

        musicPlayer = new MusicPlayer();
        //  isPlaying = false;
        musicPlayer.play(musicPath);
        MusicPlayerManager.addMusicPlayer(musicPlayer);

        setLayout(new GridLayout(4, 1));

        initializeAnimationFrames();
        createAnimationLabel();
        startAnimation();

        for (JButton jbutton:buttons
        ) {
            jbutton.setFont(font);
            add(jbutton);
        }

        /*
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
         */

        play.addActionListener(e -> {
            LevelSelectionFrame levelSelectionFrame = new LevelSelectionFrame();
        });


        quit.addActionListener(e -> {
            musicPlayer.stop();
            GameController.endGame();
        });

        settings.addActionListener(e -> {
            SettingsFrame settingsFrame = new SettingsFrame(fontName, musicPlayer);
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
        Timer animationTimer = new Timer(100, e -> animate());
        animationTimer.start();
    }

    private void animate() {
        currentFrameIndex = (currentFrameIndex + 1) % animationFrames.length;
        animationLabel.setIcon(animationFrames[currentFrameIndex]);
    }

}

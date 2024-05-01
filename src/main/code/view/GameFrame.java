package main.code.view;

import main.code.service.MusicPlayer;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private JButton play;
    boolean isPlaying;
    private MusicPlayer musicPlayer;

    public GameFrame(String filepath) {
        setTitle("Bongo Beats Bonanza");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        // center the frame
        setLocationRelativeTo(null);

        play = new JButton("Play");
        play.setFont(new Font("Comic Sans MS", Font.BOLD, 120));
        musicPlayer = new MusicPlayer();
        isPlaying = false;

        setLayout(new GridLayout(1, 1));
        add(play);
        play.addActionListener(e -> {
            if (isPlaying) {
                musicPlayer.pause();
                isPlaying = false;
                play.setText("Play");
            } else {
                musicPlayer.play(filepath);
                isPlaying = true;
                play.setText("Pause");
            }
        });

        setVisible(true);
    }
}

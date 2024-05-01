package main.code.view;

import main.code.service.MusicPlayer;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private JButton play;
    private JButton pause;
    private MusicPlayer musicPlayer;

    public GameFrame(String filepath) {
        setTitle("Bongo Beats Bonanza");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        // center the frame
        setLocationRelativeTo(null);

        play = new JButton("Play");
        pause = new JButton("Pause");
        musicPlayer = new MusicPlayer();

        setLayout(new GridLayout(1, 2));
        add(play);
        add(pause);
        play.addActionListener(e -> {
            musicPlayer.play(filepath);
        });
        pause.addActionListener(e -> {
            musicPlayer.pause();
        });

        setVisible(true);
    }
}

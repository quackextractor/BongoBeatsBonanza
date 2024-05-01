package main.code.view;

import main.code.service.MusicPlayer;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private JButton play;
    private JButton quit;
    private JButton settings;
    private boolean isPlaying;
    private MusicPlayer musicPlayer;

    public GameFrame(String musicPath, String fontName) {
        setTitle("Bongo Beats Bonanza");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        // center the frame
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

        setLayout(new GridLayout(3, 1));

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

        quit.addActionListener(e -> System.exit(0));

        settings.addActionListener(e -> {
            play.setText("Play");
            isPlaying = false;
            musicPlayer.pause();
        SettingsFrame settingsFrame = new SettingsFrame(fontName);
        });

        setVisible(true);
    }
}

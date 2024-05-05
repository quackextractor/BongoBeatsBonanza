package view;

import service.MusicPlayer;
import service.MusicPlayerManager;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class SettingsFrame extends JFrame {
    private static boolean isOpen = false; // Track whether the frame is open
    private JSlider musicVolumeSlider;
    private JSlider fxVolumeSlider;
    private JSlider difficultySlider;
    private static int difficulty = 2;
    private static int noteSpeed = 2;
    private JSlider noteSpeedSlider;
    private float volumeValue = -10;


    public SettingsFrame(String fontName, MusicPlayer musicPlayer) {
        if (isOpen) {
            // If the frame is already open, bring it to the front and return
            toFront();
            return;
        }
        isOpen = true; // Mark the frame as open

        MusicPlayer fxPlayer = new MusicPlayer(false, "");
        MusicPlayerManager.addMusicPlayer(musicPlayer);
        MusicPlayerManager.addMusicPlayer(fxPlayer);

        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 800);
        // center the frame
        setLocationRelativeTo(null);

        // initialize layout
        setLayout(new GridLayout(4, 1));

        JLabel musicVolumeLabel = new JLabel("Music vol", SwingConstants.CENTER);
        JLabel fxVolumeLabel = new JLabel("FX vol", SwingConstants.CENTER);
        Font font = new Font(fontName, Font.BOLD, 30);
        musicVolumeLabel.setFont(font);
        fxVolumeLabel.setFont(font);
        add(musicVolumeLabel);


        musicVolumeSlider = new JSlider(-30, 7, (int) MusicPlayer.getMusicVolume());
        fxVolumeSlider = new JSlider(-30, 7, (int) MusicPlayer.getFxVolume());

        JSlider[] volumeSliders = {musicVolumeSlider, fxVolumeSlider};

        for (JSlider volumeSlider : volumeSliders) {
            volumeSlider.setPaintLabels(true);
            volumeSlider.setPaintTicks(true);
            volumeSlider.setMajorTickSpacing(1);

            // add values to volume slider
            Hashtable<Integer, JLabel> volumeHashtable = new Hashtable<>();
            volumeHashtable.put(-30, new JLabel("-30dB"));
            volumeHashtable.put(-10, new JLabel("-10dB"));
            volumeHashtable.put(7, new JLabel("7dB"));
            volumeSlider.setLabelTable(volumeHashtable);
        }

        add(musicVolumeSlider);
        add(fxVolumeLabel);
        add(fxVolumeSlider);

        JLabel noteSpeedLabel;
        add(noteSpeedLabel = new JLabel("Note speed", SwingConstants.CENTER));
        noteSpeedLabel.setFont(font);

        noteSpeedSlider = new JSlider(1, 3, noteSpeed);
        noteSpeedSlider.setPaintLabels(true);
        noteSpeedSlider.setPaintTicks(true);
        noteSpeedSlider.setMajorTickSpacing(1);
        Hashtable<Integer, JLabel> speedHashtable = new Hashtable<>();
        speedHashtable.put(1, new JLabel("slow"));
        speedHashtable.put(2, new JLabel("medium"));
        Font font2 = new Font("Arial", Font.BOLD, 13);
        JLabel label = new JLabel("TURBO");
        label.setForeground(Color.red);
        label.setFont(font2);
        speedHashtable.put(3, label);
        noteSpeedSlider.setLabelTable(speedHashtable);
        add(noteSpeedSlider);

        JLabel difficultyLabel;
        add(difficultyLabel = new JLabel("Difficulty", SwingConstants.CENTER));
        difficultyLabel.setFont(font);

        difficultySlider = new JSlider(1, 3, difficulty);
        difficultySlider.setPaintLabels(true);
        difficultySlider.setPaintTicks(true);
        difficultySlider.setMajorTickSpacing(1);
        Hashtable<Integer, JLabel> difficultyHashtable = new Hashtable<>();
        difficultyHashtable.put(1, new JLabel("easy"));
        difficultyHashtable.put(2, new JLabel("normal"));
        JLabel label2 = new JLabel("NIGHTMARE");
        label2.setForeground(Color.BLUE);
        label2.setFont(font2);
        difficultyHashtable.put(3, label2);
        difficultySlider.setLabelTable(difficultyHashtable);
        add(difficultySlider);

        setSize(468, 488);
        setLocationRelativeTo(null);
        configMusicVolume(musicPlayer, fxPlayer);
        configFxVolume(fxPlayer);
        configSpeed(fxPlayer);
        configDifficulty(fxPlayer);

        // Add a window listener to mark the frame as closed when it is disposed
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                isOpen = false;
                fxPlayer.play("src/resources/sounds/exit.wav");
            }
        });

        setVisible(true);
    }

    private void configMusicVolume(MusicPlayer musicPlayer, MusicPlayer fxPlayer) {
        musicVolumeSlider.addChangeListener(e -> {
            if (!musicVolumeSlider.getValueIsAdjusting()) {
                volumeValue = musicVolumeSlider.getValue();
                MusicPlayer.changeVolume(volumeValue, true);
                musicPlayer.pause();
                fxPlayer.playWithCustomVolume(volumeValue, "src/resources/sounds/click.wav");
                musicPlayer.resume();
            }
        });
    }

    private void configFxVolume(MusicPlayer fxPlayer) {
        fxVolumeSlider.addChangeListener(e -> {
            if (!fxVolumeSlider.getValueIsAdjusting()) {
                volumeValue = fxVolumeSlider.getValue();
                MusicPlayer.changeVolume(volumeValue, false);
                fxPlayer.play("src/resources/sounds/click.wav");
            }
        });
    }

    private void configDifficulty(MusicPlayer fxPlayer) {
        difficultySlider.addChangeListener(e -> {
            if (!difficultySlider.getValueIsAdjusting()) {
                difficulty = difficultySlider.getValue();
                fxPlayer.play("src/resources/sounds/click.wav");
            }
        });
    }

    private void configSpeed(MusicPlayer fxPlayer) {
        noteSpeedSlider.addChangeListener(e -> {
            if (!noteSpeedSlider.getValueIsAdjusting()) {
                noteSpeed = noteSpeedSlider.getValue();
                fxPlayer.play("src/resources/sounds/click.wav");
            }
        });
    }
}

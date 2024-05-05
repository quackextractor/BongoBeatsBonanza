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

        // Initialize frame properties
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        // Initialize layout
        setLayout(new GridLayout(4, 1));

        // Create labels and set font
        Font font = new Font(fontName, Font.BOLD, 30);
        JLabel musicVolumeLabel = new JLabel("Music vol", SwingConstants.CENTER);
        musicVolumeLabel.setFont(font);
        JLabel fxVolumeLabel = new JLabel("FX vol", SwingConstants.CENTER);
        fxVolumeLabel.setFont(font);

        // Initialize volume sliders
        musicVolumeSlider = initializeVolumeSlider(MusicPlayer.getMusicVolume());
        fxVolumeSlider = initializeVolumeSlider(MusicPlayer.getFxVolume());

        // Add components to the frame
        add(musicVolumeLabel);
        add(musicVolumeSlider);
        add(fxVolumeLabel);
        add(fxVolumeSlider);

        // Initialize note speed slider
        noteSpeedSlider = initializeSpeedSlider();

        // Add note speed slider to the frame
        JLabel noteSpeedLabel = new JLabel("Note speed", SwingConstants.CENTER);
        noteSpeedLabel.setFont(font);
        add(noteSpeedLabel);
        add(noteSpeedSlider);

        // Initialize difficulty slider
        difficultySlider = initializeDifficultySlider();

        // Add difficulty slider to the frame
        JLabel difficultyLabel = new JLabel("Difficulty", SwingConstants.CENTER);
        difficultyLabel.setFont(font);
        add(difficultyLabel);
        add(difficultySlider);

        // Set frame size and location
        setSize(468, 488);
        setLocationRelativeTo(null);

        // Configure volume, difficulty, and speed settings
        MusicPlayer fxPlayer = new MusicPlayer(false, "");
        MusicPlayerManager.addMusicPlayer(musicPlayer);
        MusicPlayerManager.addMusicPlayer(fxPlayer);
        configMusicVolume(musicPlayer, fxPlayer);
        configFxVolume(fxPlayer);
        configSpeed(fxPlayer);
        configDifficulty(fxPlayer);

        // Add window listener to handle frame closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                isOpen = false;
                fxPlayer.play("src/resources/sounds/exit.wav");
            }
        });

        setVisible(true);
    }

    private JSlider initializeVolumeSlider(float initialValue) {
        JSlider slider = new JSlider(-30, 7, (int) initialValue);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(1);

        Hashtable<Integer, JLabel> volumeHashtable = new Hashtable<>();
        volumeHashtable.put(-30, new JLabel("-30dB"));
        volumeHashtable.put(-10, new JLabel("-10dB"));
        volumeHashtable.put(7, new JLabel("7dB"));
        slider.setLabelTable(volumeHashtable);

        return slider;
    }

    private JSlider initializeSpeedSlider() {
        JSlider slider = new JSlider(1, 3, noteSpeed);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(1);

        Hashtable<Integer, JLabel> speedHashtable = new Hashtable<>();
        speedHashtable.put(1, new JLabel("slow"));
        speedHashtable.put(2, new JLabel("medium"));
        Font font2 = new Font("Arial", Font.BOLD, 13);
        JLabel label = new JLabel("TURBO");
        label.setForeground(Color.red);
        label.setFont(font2);
        speedHashtable.put(3, label);

        slider.setLabelTable(speedHashtable);
        return slider;
    }

    private JSlider initializeDifficultySlider() {
        JSlider slider = new JSlider(1, 3, difficulty);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(1);

        Hashtable<Integer, JLabel> difficultyHashtable = new Hashtable<>();
        difficultyHashtable.put(1, new JLabel("easy"));
        difficultyHashtable.put(2, new JLabel("normal"));
        JLabel label2 = new JLabel("NIGHTMARE");
        label2.setForeground(Color.BLUE);
        Font font2 = new Font("Arial", Font.BOLD, 13);
        label2.setFont(font2);
        difficultyHashtable.put(3, label2);

        slider.setLabelTable(difficultyHashtable);
        return slider;
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

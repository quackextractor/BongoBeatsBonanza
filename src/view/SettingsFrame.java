package view;

import service.MusicPlayer;
import service.MusicPlayerManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Hashtable;

public class SettingsFrame extends JFrame {
    private static boolean isOpen = false;
    private JSlider musicVolumeSlider;
    private JSlider fxVolumeSlider;
    private JSlider difficultySlider;
    private static int difficulty = 2;
    private static int noteSpeed = 2;
    private JSlider noteSpeedSlider;
    private float volumeValue = -10;

    public SettingsFrame(String fontName, MusicPlayer musicPlayer) {
        if (isOpen) {
            toFront();
            return;
        }
        isOpen = true;
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        gbc.weightx = 1; // Make components stretch horizontally

        Font font = new Font(fontName, Font.BOLD, 30);

        // Volume Settings
        JLabel musicVolumeLabel = createFancyLabel("Music vol", font);
        musicVolumeSlider = initializeVolumeSlider(MusicPlayer.getMusicVolume());
        addComponent(musicVolumeLabel, gbc, 0, 0);
        addComponent(musicVolumeSlider, gbc, 1, 0);

        // FX Volume Settings
        JLabel fxVolumeLabel = createFancyLabel("FX vol", font);
        fxVolumeSlider = initializeVolumeSlider(MusicPlayer.getFxVolume());
        addComponent(fxVolumeLabel, gbc, 0, 1);
        addComponent(fxVolumeSlider, gbc, 1, 1);

        // Note Speed Settings
        JLabel noteSpeedLabel = createFancyLabel("Note speed", font);
        noteSpeedSlider = initializeSpeedSlider();
        addComponent(noteSpeedLabel, gbc, 0, 2);
        addComponent(noteSpeedSlider, gbc, 1, 2);

        // Difficulty Settings
        JLabel difficultyLabel = createFancyLabel("Difficulty", font);
        difficultySlider = initializeDifficultySlider();
        addComponent(difficultyLabel, gbc, 0, 3);
        addComponent(difficultySlider, gbc, 1, 3);

        // Adjust vertical positioning
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;

        // Configure settings
        MusicPlayer fxPlayer = new MusicPlayer(false, "");
        MusicPlayerManager.addMusicPlayer(musicPlayer);
        MusicPlayerManager.addMusicPlayer(fxPlayer);
        configMusicVolume(musicPlayer, fxPlayer);
        configFxVolume(fxPlayer);
        configSpeed(fxPlayer);
        configDifficulty(fxPlayer);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                isOpen = false;
                fxPlayer.play("src/resources/sounds/exit.wav");
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                isOpen = false;
            }
        });

        setVisible(true);
    }

    private void addComponent(Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private FancyJLabel createFancyLabel(String text, Font font) {
        FancyJLabel label = new FancyJLabel(text, JLabel.LEFT);
        label.setFont(font);
        label.setShadowOffset(2); // Set shadow offset to 2
        return label;
    }

    private JSlider initializeVolumeSlider(float initialValue) {
        JSlider slider = new JSlider(-30, 6, (int) initialValue);
        customizeSlider(slider);

        Hashtable<Integer, JLabel> volumeHashtable = new Hashtable<>();
        volumeHashtable.put(-30, new JLabel("-30dB"));
        volumeHashtable.put(-10, new JLabel("-10dB"));
        volumeHashtable.put(6, new JLabel("6dB"));
        slider.setLabelTable(volumeHashtable);

        return slider;
    }

    private JSlider initializeSpeedSlider() {
        JSlider slider = new JSlider(1, 3, noteSpeed);
        customizeSlider(slider);

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
        customizeSlider(slider);

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

    private void customizeSlider(JSlider slider) {
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(1);
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

package view;

import service.MusicPlayer;
import service.MusicPlayerManager;
import service.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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
    private JTextField track1KeyTextField;
    private JTextField track2KeyTextField;
    private MusicPlayer fxPlayer;

    public SettingsFrame(String fontName, MusicPlayer musicPlayer) {
        if (isOpen) {
            toFront();
            return;
        }
        isOpen = true;
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        gbc.weightx = 1; // Make components stretch horizontally

        Font font = new Font(fontName, Font.BOLD, 30);
        String defaultText = "Insert Key. Press the <ENTER> key to Confirm";

        // Catch Note Controls Settings
        FancyJLabel track1KeyLabel = createFancyLabel("Track 1 Key:", font);
        setCustomColor(track1KeyLabel, Color.BLUE);
        track1KeyTextField = new JTextField(defaultText);
        addComponent(track1KeyLabel, gbc, 0, 4);
        addComponent(track1KeyTextField, gbc, 1, 4);

        FancyJLabel track2KeyLabel = createFancyLabel("Track 2 Key:", font);
        setCustomColor(track2KeyLabel, Color.cyan);
        track2KeyTextField = new JTextField(defaultText);
        addComponent(track2KeyLabel, gbc, 0, 5);
        addComponent(track2KeyTextField, gbc, 1, 5);

        adjustVertPos(gbc);

        // Volume Settings
        FancyJLabel musicVolumeLabel = createFancyLabel("Music volume", font);
        setCustomColor(musicVolumeLabel, Color.red);
        musicVolumeSlider = initializeVolumeSlider(MusicPlayer.getMusicVolume());
        addComponent(musicVolumeLabel, gbc, 0, 0);
        addComponent(musicVolumeSlider, gbc, 1, 0);

        // FX Volume Settings
        FancyJLabel fxVolumeLabel = createFancyLabel("FX volume", font);
        setCustomColor(fxVolumeLabel, Color.magenta);
        fxVolumeSlider = initializeVolumeSlider(MusicPlayer.getFxVolume());
        addComponent(fxVolumeLabel, gbc, 0, 1);
        addComponent(fxVolumeSlider, gbc, 1, 1);

        // Note Speed Settings
        FancyJLabel noteSpeedLabel = createFancyLabel("Note speed", font);
        setCustomColor(noteSpeedLabel, Color.GREEN);
        noteSpeedSlider = initializeSpeedSlider();
        addComponent(noteSpeedLabel, gbc, 0, 2);
        addComponent(noteSpeedSlider, gbc, 1, 2);

        // Difficulty Settings
        FancyJLabel difficultyLabel = createFancyLabel("Difficulty", font);
        setCustomColor(difficultyLabel, Color.YELLOW);
        difficultySlider = initializeDifficultySlider();
        addComponent(difficultyLabel, gbc, 0, 3);
        addComponent(difficultySlider, gbc, 1, 3);

        adjustVertPos(gbc);

        // Configure settings
        fxPlayer = new MusicPlayer(false, "resources/sounds/click.wav");
        MusicPlayerManager.addMusicPlayer(musicPlayer);
        MusicPlayerManager.addMusicPlayer(fxPlayer);
        configMusicVolume(musicPlayer, fxPlayer);
        configFxVolume();
        configSpeed();
        configDifficulty();
        configCatchNoteControls();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                isOpen = false;
                fxPlayer.play("resources/sounds/exit.wav");
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                isOpen = false;
            }
        });

        setVisible(true);
    }

    private void setCustomColor(FancyJLabel fancyJLabel, Color color){
        fancyJLabel.setShadowColor(color);
        fancyJLabel.setShadowOffset(2);
        fancyJLabel.setShadowOpacity(0.5F);
    }

    private void adjustVertPos(GridBagConstraints gbc){
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;
    }

    private void addComponent(Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    private FancyJLabel createFancyLabel(String text, Font font) {
        FancyJLabel label = new FancyJLabel(text, JLabel.LEFT);
        label.setFont(font);
        label.setShadowOffset(2);
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

    private void configCatchNoteControls() {
        track1KeyTextField.addActionListener(e -> {
            String keyText = track1KeyTextField.getText().toUpperCase();
            verifyAndSetKeyCode(keyText, true);
        });

        track2KeyTextField.addActionListener(e -> {
            String keyText = track2KeyTextField.getText().toUpperCase();
            verifyAndSetKeyCode(keyText, false);
        });
    }

    private void verifyAndSetKeyCode(String keyText, boolean is1Track) {
        if (keyText.length() != 1) {
            JOptionPane.showMessageDialog(this, "Invalid key! Please enter a single character key.", "Error", JOptionPane.ERROR_MESSAGE);
            fxPlayer.play("resources/sounds/alert.wav");
            return;
        }

        char keyChar = keyText.charAt(0);
        int keyCode = KeyEvent.getExtendedKeyCodeForChar(keyChar);
        if (keyCode == KeyEvent.CHAR_UNDEFINED) {
            JOptionPane.showMessageDialog(this, "Invalid key! Please enter a valid key character.", "Error", JOptionPane.ERROR_MESSAGE);
            fxPlayer.play("resources/sounds/alert.wav");
            return;
        }

        if (is1Track) {
            GameJPanel.setTrack1Key(keyCode);
        } else {
            GameJPanel.setTrack2Key(keyCode);
        }
        fxPlayer.playDefault();
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
                fxPlayer.playWithCustomVolume(volumeValue, "resources/sounds/click.wav");
                musicPlayer.resume();
            }
        });
    }

    private void configFxVolume() {
        fxVolumeSlider.addChangeListener(e -> {
            if (!fxVolumeSlider.getValueIsAdjusting()) {
                volumeValue = fxVolumeSlider.getValue();
                MusicPlayer.changeVolume(volumeValue, false);
                fxPlayer.playDefault();
            }
        });
    }

    private void configDifficulty() {
        difficultySlider.addChangeListener(e -> {
            if (!difficultySlider.getValueIsAdjusting()) {
                difficulty = difficultySlider.getValue();
                fxPlayer.playDefault();
                Score.setDifficultyModifier(difficulty * 0.5);
            }
        });
    }

    private void configSpeed() {
        noteSpeedSlider.addChangeListener(e -> {
            if (!noteSpeedSlider.getValueIsAdjusting()) {
                noteSpeed = noteSpeedSlider.getValue();
                fxPlayer.playDefault();
                GameJPanel.modifyMoveParams(noteSpeed);
            }
        });
    }
}

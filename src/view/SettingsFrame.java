package view;

import service.MusicPlayer;
import service.MusicPlayerManager;
import service.Score;
import service.SettingsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import static service.GameUtils.adjustVertPos;
import static service.GameUtils.setCustomColor;

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
    private final SettingsManager settingsManager;

    /**
     * Constructs a SettingsFrame object with specified parameters.
     *
     * @param fontName The name of the font to be used for labels.
     * @param musicPlayer The music player instance used in the game.
     */
    public SettingsFrame(String fontName, MusicPlayer musicPlayer) {
        settingsManager = new SettingsManager();
        SettingsManager.loadSettings(settingsManager);

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
        String defaultText = "Insert an alternative key here. <ENTER> to Confirm";

        String defaultText1 = KeyEvent.getKeyText(settingsManager.getTrack1Key());
        String defaultText2 = KeyEvent.getKeyText(settingsManager.getTrack2Key());

        if (defaultText1.equals("Unknown keyCode: 0x0")) {
            defaultText1 = defaultText;
        }
        if (defaultText2.equals("Unknown keyCode: 0x0")) {
            defaultText2 = defaultText;
        }

        // Catch Note Controls Settings
        FancyJLabel track1KeyLabel = createFancyLabel("Left Bongo Key:", font);
        setCustomColor(track1KeyLabel, Color.BLUE);
        track1KeyTextField = new JTextField(defaultText1);
        addComponent(track1KeyLabel, gbc, 0, 4);
        addComponent(track1KeyTextField, gbc, 1, 4);

        FancyJLabel track2KeyLabel = createFancyLabel("Right Bongo Key:", font);
        setCustomColor(track2KeyLabel, Color.cyan);
        track2KeyTextField = new JTextField(defaultText2);
        addComponent(track2KeyLabel, gbc, 0, 5);
        addComponent(track2KeyTextField, gbc, 1, 5);

        adjustVertPos(gbc);

        // Volume Settings
        FancyJLabel musicVolumeLabel = createFancyLabel("Music volume", font);
        setCustomColor(musicVolumeLabel, Color.red);
        musicVolumeSlider = initializeVolumeSlider(settingsManager.getMusicVolume());
        addComponent(musicVolumeLabel, gbc, 0, 0);
        addComponent(musicVolumeSlider, gbc, 1, 0);

        // FX Volume Settings
        FancyJLabel fxVolumeLabel = createFancyLabel("FX volume", font);
        setCustomColor(fxVolumeLabel, Color.magenta);
        fxVolumeSlider = initializeVolumeSlider(settingsManager.getFxVolume());
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
                SettingsManager.saveSettings(settingsManager);
                fxPlayer.play("resources/sounds/exit.wav");
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                isOpen = false;
                SettingsManager.saveSettings(settingsManager);
            }
        });

        setVisible(true);
    }

    /**
     * Adds a component to the frame using specified grid bag constraints.
     *
     * @param component The component to be added.
     * @param gbc The grid bag constraints.
     * @param x The column position.
     * @param y The row position.
     */
    private void addComponent(Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(component, gbc);
    }

    /**
     * Creates a fancy label with specified text and font.
     *
     * @param text The text of the label.
     * @param font The font of the label.
     * @return The created fancy label.
     */
    private FancyJLabel createFancyLabel(String text, Font font) {
        FancyJLabel label = new FancyJLabel(text, JLabel.LEFT);
        label.setFont(font);
        label.setShadowOffset(2);
        return label;
    }

    /**
     * Initializes the music volume slider with the given initial value.
     *
     * @param initialValue The initial value of the music volume slider.
     * @return The initialized music volume slider.
     */
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

    /**
     * Initializes the note speed slider.
     *
     * @return The initialized note speed slider.
     */
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

    /**
     * Configures settings related to catch note controls.
     */
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

    /**
     * Verifies and sets the key code based on the provided key text.
     *
     * @param keyText The text representing the key.
     * @param is1Track Indicates whether it's the first or second track key.
     */
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
            settingsManager.setTrack1Key(keyCode);
            confirmFlash(track1KeyTextField);
        } else {
            GameJPanel.setTrack2Key(keyCode);
            settingsManager.setTrack2Key(keyCode);
            confirmFlash(track2KeyTextField);
        }
        fxPlayer.playDefault();
    }

    /**
     * Displays a confirmation message and flashes the text field when a key is successfully set.
     *
     * @param jTextField The text field to be flashed.
     */
    private static void confirmFlash(JTextField jTextField) {
        String savedText = jTextField.getText();
        jTextField.setText("CONFIRMED!");
        jTextField.repaint();

        // Use Swing Timer to change text back after a delay
        Timer timer = new Timer(300, e -> {
            jTextField.setText(savedText);
            jTextField.repaint();
        });

        // Ensure the timer only runs once
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Initializes the difficulty slider.
     *
     * @return The initialized difficulty slider.
     */
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

    /**
     * Customizes the appearance of a slider.
     *
     * @param slider The slider to be customized.
     */
    private void customizeSlider(JSlider slider) {
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(1);
    }

    /**
     * Configures music volume settings.
     *
     * @param musicPlayer The music player instance.
     * @param fxPlayer The sound effects player instance.
     */
    private void configMusicVolume(MusicPlayer musicPlayer, MusicPlayer fxPlayer) {
        musicVolumeSlider.addChangeListener(e -> {
            if (!musicVolumeSlider.getValueIsAdjusting()) {
                volumeValue = musicVolumeSlider.getValue();
                MusicPlayer.changeVolume(volumeValue, true);
                musicPlayer.pause();
                fxPlayer.playWithCustomVolume(volumeValue, "resources/sounds/click.wav");
                musicPlayer.resume();
                settingsManager.setMusicVolume((int) volumeValue);
            }
        });
    }

    /**
     * Configures sound effects volume settings.
     */
    private void configFxVolume() {
        fxVolumeSlider.addChangeListener(e -> {
            if (!fxVolumeSlider.getValueIsAdjusting()) {
                volumeValue = fxVolumeSlider.getValue();
                MusicPlayer.changeVolume(volumeValue, false);
                fxPlayer.playDefault();
                settingsManager.setFxVolume((int) volumeValue);
            }
        });
    }

    /**
     * Configures difficulty settings.
     */
    private void configDifficulty() {
        difficultySlider.addChangeListener(e -> {
            if (!difficultySlider.getValueIsAdjusting()) {
                difficulty = difficultySlider.getValue();
                fxPlayer.playDefault();
                Score.setDifficultyModifier(difficulty * 0.5);
                settingsManager.setDifficulty(difficulty);
            }
        });
    }

    /**
     * Configures note speed settings.
     */
    private void configSpeed() {
        noteSpeedSlider.addChangeListener(e -> {
            if (!noteSpeedSlider.getValueIsAdjusting()) {
                noteSpeed = noteSpeedSlider.getValue();
                fxPlayer.playDefault();
                GameJPanel.modifyMoveParams(noteSpeed);
                settingsManager.setNoteSpeed(noteSpeed);
            }
        });
    }
}
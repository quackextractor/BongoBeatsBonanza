package view;

import service.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class SettingsFrame extends JFrame {
    private JSlider volumeSlider;
    private JSlider difficultySlider;
    private int difficulty = 2;
    private int noteSpeed = 2;
    private JSlider noteSpeedSlider;
    private JLabel volumeLabel;
    private JLabel difficultyLabel;
    private JLabel noteSpeedLabel;
    private ImageIcon icon;
    private Hashtable<Integer, JLabel> volumeHashtable = new Hashtable<>();
    private Hashtable<Integer, JLabel> speedHashtable = new Hashtable<>();
    private Hashtable<Integer, JLabel> difficultyHashtable = new Hashtable<>();
    private float volumeValue = -10;


    public SettingsFrame(String fontName, MusicPlayer musicPlayer) {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 800);
        // center the frame
        setLocationRelativeTo(null);

        // initialize layout
        setLayout(new GridLayout(3, 1));

        Font font = new Font(fontName, Font.BOLD, 30);
        add(volumeLabel = new JLabel("Volume", SwingConstants.CENTER));
        volumeLabel.setFont(font);

        volumeSlider = new JSlider(-30, 7, -11);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setMajorTickSpacing(1);

        // add values to volume slider
        volumeHashtable.put(-30, new JLabel("-30dB"));
        volumeHashtable.put(-10, new JLabel("-10dB"));
        volumeHashtable.put(7, new JLabel("7dB"));
        volumeSlider.setLabelTable(volumeHashtable);
        add(volumeSlider);


        add(noteSpeedLabel = new JLabel("Note speed", SwingConstants.CENTER));
        noteSpeedLabel.setFont(font);

        noteSpeedSlider = new JSlider(1, 3, noteSpeed);
        noteSpeedSlider.setPaintLabels(true);
        noteSpeedSlider.setPaintTicks(true);
        noteSpeedSlider.setMajorTickSpacing(1);
        speedHashtable.put(1, new JLabel("slow"));
        speedHashtable.put(2, new JLabel("medium"));
        speedHashtable.put(3, new JLabel("fast"));
        noteSpeedSlider.setLabelTable(speedHashtable);
        add(noteSpeedSlider);

        add(difficultyLabel = new JLabel("Difficulty", SwingConstants.CENTER));
        difficultyLabel.setFont(font);

        difficultySlider = new JSlider(1, 3, difficulty);
        difficultySlider.setPaintLabels(true);
        difficultySlider.setPaintTicks(true);
        difficultySlider.setMajorTickSpacing(1);
        difficultyHashtable.put(1, new JLabel("easy"));
        difficultyHashtable.put(2, new JLabel("normal"));
        difficultyHashtable.put(3, new JLabel("hard"));
        difficultySlider.setLabelTable(difficultyHashtable);
        add(difficultySlider);

        setSize(468, 488);
        setLocationRelativeTo(null);
        configVolume(musicPlayer);
        configSpeed();
        configDifficulty();

        setVisible(true);
    }

    private void configVolume(MusicPlayer musicPlayer) {
    volumeSlider.addChangeListener(e ->{
        if (!volumeSlider.getValueIsAdjusting()){
            volumeValue = volumeSlider.getValue();
            MusicPlayer.changeVolume(volumeValue);
            musicPlayer.pause();
            musicPlayer.resume();
        }
    });
    }

    private void configDifficulty() {
        difficultySlider.addChangeListener(e ->{
            if (!difficultySlider.getValueIsAdjusting()){
                difficulty = difficultySlider.getValue();
            }
        });
    }
    private void configSpeed() {
        noteSpeedSlider.addChangeListener(e ->{
            if (!noteSpeedSlider.getValueIsAdjusting()){
                noteSpeed = noteSpeedSlider.getValue();
            }
        });
    }
}

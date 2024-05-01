package main.code.view;

import main.code.service.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class SettingsFrame extends JFrame {
    private JSlider volume;
    private JSlider difficulty;
    private JSlider noteSpeed;
    private JLabel volumeLabel;
    private JLabel difficultyLabel;
    private JLabel noteSpeedLabel;
    private ImageIcon icon;
    private Hashtable<Integer, JLabel> volumeHashtable = new Hashtable<>();
    private Hashtable<Integer, JLabel> speedHashtable = new Hashtable<>();
    private Hashtable<Integer, JLabel> difficultyHashtable = new Hashtable<>();
    private float volumeValue = -10;


    public SettingsFrame(String fontName) {
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

        volume = new JSlider(-30, 7, -11);
        volume.setPaintLabels(true);
        volume.setPaintTicks(true);
        volume.setMajorTickSpacing(1);

        // add values to volume slider
        volumeHashtable.put(-30, new JLabel("-30dB"));
        volumeHashtable.put(-10, new JLabel("-10dB"));
        volumeHashtable.put(7, new JLabel("7dB"));
        volume.setLabelTable(volumeHashtable);
        add(volume);


        add(noteSpeedLabel = new JLabel("Note speed", SwingConstants.CENTER));
        noteSpeedLabel.setFont(font);

        noteSpeed = new JSlider(1, 3, 2);
        noteSpeed.setPaintLabels(true);
        noteSpeed.setPaintTicks(true);
        noteSpeed.setMajorTickSpacing(1);
        speedHashtable.put(1, new JLabel("slow"));
        speedHashtable.put(2, new JLabel("medium"));
        speedHashtable.put(3, new JLabel("fast"));
        noteSpeed.setLabelTable(speedHashtable);
        add(noteSpeed);

        add(difficultyLabel = new JLabel("Difficulty", SwingConstants.CENTER));
        difficultyLabel.setFont(font);

        difficulty = new JSlider(1, 3, 2);
        difficulty.setPaintLabels(true);
        difficulty.setPaintTicks(true);
        difficulty.setMajorTickSpacing(1);
        difficultyHashtable.put(1, new JLabel("easy"));
        difficultyHashtable.put(2, new JLabel("normal"));
        difficultyHashtable.put(3, new JLabel("hard"));
        difficulty.setLabelTable(difficultyHashtable);
        add(difficulty);

        setSize(468, 488);
        setLocationRelativeTo(null);
        configVolume();

        setVisible(true);
    }

    private void configVolume() {
    volume.addChangeListener(e ->{
        if (!volume.getValueIsAdjusting()){
            volumeValue = volume.getValue();
            MusicPlayer.changeVolume(volumeValue);
        }
    });
    }
}

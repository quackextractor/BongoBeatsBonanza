package main.code.view;

import main.code.service.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class SettingsFrame extends JFrame {
    private JSlider volume;
    private JButton difficulty;
    private JSlider noteSpeed;
    private JLabel volumeLabel;
    private ImageIcon icon;
    private Hashtable<Integer, JLabel> volumeHashtable = new Hashtable<>();
    private Hashtable<Integer, JLabel> speedHashtable = new Hashtable<>();
    private float volumeValue = -10;


    public SettingsFrame(MusicPlayer player) {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 800);
        // center the frame
        setLocationRelativeTo(null);

        volumeLabel = new JLabel("quack");

        volume = new JSlider(-90, 7, -10);
        volume.setPaintLabels(true);
        volume.setPaintTicks(true);
        volume.setMajorTickSpacing(1);

        volumeHashtable.put(-90, new JLabel("-90dB"));
        volumeHashtable.put(-60, new JLabel("-60dB"));
        volumeHashtable.put(-30, new JLabel("-30dB"));
        volumeHashtable.put(-10, new JLabel("-10dB"));
        volumeHashtable.put(7, new JLabel("7dB"));
        volume.setLabelTable(volumeHashtable);
        setLayout(new GridLayout(2, 3));
        add(volume);
        volume.add(volumeLabel);

        noteSpeed = new JSlider(1, 3, 2);
        noteSpeed.setPaintLabels(true);
        noteSpeed.setPaintTicks(true);
        noteSpeed.setMajorTickSpacing(1);
        speedHashtable.put(1, new JLabel("slow"));
        speedHashtable.put(2, new JLabel("medium"));
        speedHashtable.put(3, new JLabel("fast"));
        noteSpeed.setLabelTable(speedHashtable);
        add(noteSpeed);

        difficulty = new JButton("Difficulty");
        add(difficulty);

        setSize(468, 488);
        setLocationRelativeTo(null);
        configVolume(player);
        setVisible(true);
    }

    private void configVolume(MusicPlayer player) {
    volume.addChangeListener(e ->{
        if (!volume.getValueIsAdjusting()){
            volumeValue = volume.getValue();
            MusicPlayer.changeVolume(volumeValue);
        }
    });
    }
}

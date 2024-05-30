package view;

import controller.GameController;
import service.MusicPlayer;
import service.MusicPlayerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class TitleFrame extends JFrame {

    private final MusicPlayer musicPlayer;
    private final MusicPlayer fxPlayer;
    private JLabel animationLabel;
    private ImageIcon[] animationFrames;
    private int currentFrameIndex;
    private final String fontName;

    public TitleFrame(String musicPath, String fontName) {
        LevelSelectionFrame.hasLevelStarted = false;
        this.fontName = fontName;

        setTitle("Bongo Beats Bonanza");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Window size
        setSize(800, 800);
        // Center the window
        setLocationRelativeTo(null);

        JButton play = new JButton("Play");
        JButton settings = new JButton("Settings");
        JButton quit = new JButton("Quit");

        JButton[] buttons = {play, settings, quit};

        Font font = new Font(fontName, Font.BOLD, 80);

        musicPlayer = new MusicPlayer(true, musicPath);
        musicPlayer.playDefault();
        MusicPlayerManager.addMusicPlayer(musicPlayer);
        fxPlayer = new MusicPlayer(false, "resources/sounds/click.wav");

        setLayout(new BorderLayout());
        double logoScale = 2;
        int width = 400;
        int height = 100;
        initializeAnimationFrames((int) (width * logoScale), (int) (height * logoScale));
        createAnimationLabel();
        startAnimation();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));
        for (JButton button : buttons) {
            button.setFont(font);
            button.setOpaque(true);  // To make the button's background visible
            button.setBackground(Color.LIGHT_GRAY);  // Set background color
            button.setForeground(Color.BLACK);  // Set text color
            button.addActionListener(new ButtonClickListener());

            // Wrap each button in a panel to apply padding
            JPanel buttonPanelWrapper = new JPanel(new BorderLayout());
            //    buttonPanelWrapper.setBorder(new EmptyBorder(0, 0, 0, 0));  // Add left padding
            buttonPanelWrapper.add(button, BorderLayout.CENTER);

            buttonPanel.add(buttonPanelWrapper);
        }

        add(animationLabel, BorderLayout.PAGE_START);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            musicPlayer.stop();
            GameController.endGame();
        }
    }


    private void initializeAnimationFrames(int width, int height) {
        animationFrames = new ImageIcon[5];
        for (int i = 0; i < 5; i++) {
            Image img = new ImageIcon("resources/sprites/title" + i + ".png").getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            animationFrames[i] = new ImageIcon(scaledImg);
        }
    }

    private void createAnimationLabel() {
        animationLabel = new JLabel(animationFrames[0]);
        animationLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    private void startAnimation() {
        Timer animationTimer = new Timer(300, e -> animate());
        animationTimer.start();
    }

    private void animate() {
        currentFrameIndex = (currentFrameIndex + 1) % animationFrames.length;
        animationLabel.setIcon(animationFrames[currentFrameIndex]);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            switch (source.getText()) {
                case "Play":
                    fxPlayer.playDefault();
                    LevelSelectionFrame levelSelectionFrame = new LevelSelectionFrame();
                    levelSelectionFrame.setIconImage(GameController.getGameIcon());
                    break;
                case "Settings":
                    fxPlayer.playDefault();
                    SettingsFrame settingsFrame = new SettingsFrame(fontName, musicPlayer);
                    settingsFrame.setIconImage(GameController.getGameIcon());
                    break;
                case "Quit":
                    musicPlayer.stop();
                    GameController.endGame();
                    break;
                default:
                    break;
            }
        }
    }
}

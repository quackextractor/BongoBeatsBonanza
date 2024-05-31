package view;

import controller.GameController;
import service.MusicPlayer;
import service.MusicPlayerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Represents the title frame of the game, providing options to play, access settings, view credits, or quit the game.
 * Displays an animated logo and handles button clicks to navigate to different frames.
 */
public class TitleFrame extends JFrame {

    private final MusicPlayer musicPlayer;
    private final MusicPlayer fxPlayer;
    private JLabel animationLabel;
    private ImageIcon[] animationFrames;
    private int currentFrameIndex;
    private final String fontName;

    /**
     * Constructs a TitleFrame object with the specified music path and font name.
     *
     * @param musicPath The path to the background music.
     * @param fontName  The name of the font to be used.
     */
    public TitleFrame(String musicPath, String fontName) {
        LevelSelectionFrame.hasLevelStarted = false;
        this.fontName = fontName;
        setResizable(false);

        setTitle("Bongo Beats Bonanza");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        JButton play = new JButton("Play");
        JButton settings = new JButton("Settings");
        JButton credits = new JButton("Credits");
        JButton quit = new JButton("Quit");

        JButton[] buttons = {play, settings, credits, quit};

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
        buttonPanel.setLayout(new GridLayout(4, 1));
        for (JButton button : buttons) {
            button.setFont(font);
            button.setOpaque(true);
            button.setBackground(Color.LIGHT_GRAY);
            button.setForeground(Color.BLACK);
            button.addActionListener(new ButtonClickListener());

            JPanel buttonPanelWrapper = new JPanel(new BorderLayout());
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

    /**
     * Initializes the animation frames with the specified width and height.
     *
     * @param width  The width of the animation frames.
     * @param height The height of the animation frames.
     */
    private void initializeAnimationFrames(int width, int height) {
        animationFrames = new ImageIcon[5];
        for (int i = 0; i < 5; i++) {
            Image img = new ImageIcon("resources/sprites/title" + i + ".png").getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            animationFrames[i] = new ImageIcon(scaledImg);
        }
    }

    /**
     * Creates the animation label.
     */
    private void createAnimationLabel() {
        animationLabel = new JLabel(animationFrames[0]);
        animationLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * Starts the animation.
     */
    private void startAnimation() {
        Timer animationTimer = new Timer(300, e -> animate());
        animationTimer.start();
    }

    /**
     * Animates the logo by updating the animation frames.
     */
    private void animate() {
        currentFrameIndex = (currentFrameIndex + 1) % animationFrames.length;
        animationLabel.setIcon(animationFrames[currentFrameIndex]);
    }

    /**
     * Adds credits to the specified credits frame.
     *
     * @param creditsFrame The credits frame to add credits to.
     */
    private void addCredits(CreditsFrame creditsFrame){
        creditsFrame.addTitle("Game Development");
        creditsFrame.addCredit(
                "Development and Coding: LostSoul",
                "resources/credits/LostSoul.png",
                200,
                200,
                "https://discord.gg/hA4BVNnDA5",
                "Made this game for a school project and kind of got carried away in the process. No regrets though :)",
                "Game is licensed under the MIT License."
        );
        creditsFrame.addTitle("Music");
        creditsFrame.addCredit(
                "Composing and Charting: Kitsune",
                "resources/credits/kitsune.png",
                200,
                200,
                "mailto:dhheadless@gmail.com",
                "Composed and produced the Tutorial song (absolute banger imo) and did the charting for this game. Thank you for all your support!",
                "License: Attribution 3.0"
        );
        creditsFrame.addCredit(
                "Music Producer: YonKaGor",
                "resources/credits/YonKaGor.png",
                200,
                200,
                "https://youtu.be/lgzCxqQUU5g?si=_j6aPFfl8fR9G3GN",
                "Created \"Memory Merge\", a song featured in the game. Thank you for making such nice and meaningful songs!",
                "License: Creative Commons Attribution-NonCommercial"
        );
        creditsFrame.addTitle("Free Sounds");
        int freeSoundSize = 150;
        creditsFrame.addCredit(
                "beat tune abysses.wav by betabeats.",
                "resources/credits/betabeats.png",
                freeSoundSize,
                freeSoundSize,
                "https://freesound.org/s/650965/",
                "Very nice menu music.",
                "License: Attribution NonCommercial 4.0"
        );creditsFrame.addCredit(
                "tambohit4.wav by NoiseCollector",
                "resources/credits/NoiseCollector.png",
                freeSoundSize,
                freeSoundSize,
                "https://freesound.org/s/125605/",
                "Note hit sound in level",
                "License: Attribution 3.0"
        );
        creditsFrame.addCredit(
                "Click by complex_waveform",
                "resources/credits/complex_waveform.png",
                freeSoundSize,
                freeSoundSize,
                "https://freesound.org/s/213148/",
                "Menu click sound",
                "License: Attribution 4.0"
        );
        creditsFrame.addCredit(
                "13024 reel fast forward by Robinhood76",
                "resources/credits/Robinhood76.png",
                freeSoundSize,
                freeSoundSize,
                "https://freesound.org/s/733146/",
                "Note miss sound",
                "License: Attribution NonCommercial 4.0"
        );
        creditsFrame.addCredit(
                "freesound.org",
                "resources/credits/freeSoundLogo.png",
                288,
                100,
                "https://freesound.org/",
                "Thank you for hosting a non-profit platform for media distribution!",
                "Media that wasn't featured here falls under Creative Commons 0."
        );
        creditsFrame.addTitle("");
        creditsFrame.addTitle("THANK YOU FOR PLAYING! <3");
    }


    /**
     * Handles button click events.
     */
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
                case "Credits":
                    fxPlayer.playDefault();
                    CreditsFrame creditsFrame = new CreditsFrame();
                    addCredits(creditsFrame);
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

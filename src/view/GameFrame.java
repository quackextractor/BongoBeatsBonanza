package view;

import service.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {
    private final int screenWidth = 800;
    private final int screenHeight = 800;
    private final int lineSpacing = screenWidth / 4;
    private final int lineWidth = screenWidth / 20;
    private final int firstLineX = (screenWidth - lineSpacing) / 2 - lineWidth / 2;
    private final int secondLineX = firstLineX + lineSpacing;
    private final int horizontalHeight = screenHeight / 3;
    private final GameJPanel gameJPanel;
    private final FancyJLabel scoreLabel;
    private final FancyJLabel streakLabel;
    private final FancyJLabel accuracyLabel;
    private final FancyJLabel scoreValueLabel;
    private final FancyJLabel streakValueLabel;
    private final FancyJLabel accuracyValueLabel;

    public GameFrame(String levelName) {
        setTitle(levelName);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(screenWidth, screenHeight);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        gameJPanel = new GameJPanel(firstLineX, secondLineX, horizontalHeight, levelName, this);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(screenWidth, screenHeight));
        gameJPanel.setBounds(0, 0, screenWidth, screenHeight);
        layeredPane.add(gameJPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel uiPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = setupGridBagConstraints();

        int width = 120;
        scoreLabel = createFixedSizeLabel("Score:", width);
        streakLabel = createFixedSizeLabel("Streak:", width);
        accuracyLabel = createFixedSizeLabel("Accuracy:", width);

        scoreValueLabel = createValueLabel("0");
        streakValueLabel = createValueLabel("0");
        accuracyValueLabel = createValueLabel("100.00%");

        addLabelsToPanel(uiPanel, gbc, accuracyLabel, accuracyValueLabel); // Accuracy label first
        addLabelsToPanel(uiPanel, gbc, streakLabel, streakValueLabel);
        addLabelsToPanel(uiPanel, gbc, scoreLabel, scoreValueLabel); // Score label last

        int uiPanelHeight = 120;
        int uiPanelWidth = 220;
        int uiPanelXOffset = -20;
        int uiPanelYOffset = 20;
        uiPanel.setBounds(screenWidth - uiPanelWidth + uiPanelXOffset, uiPanelYOffset, uiPanelWidth, uiPanelHeight);
        uiPanel.setOpaque(false);
        layeredPane.add(uiPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private GridBagConstraints setupGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    private void addLabelsToPanel(JPanel panel, GridBagConstraints gbc, FancyJLabel label, FancyJLabel valueLabel) {
        panel.add(label, gbc);
        gbc.gridx++;
        panel.add(valueLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private FancyJLabel createFixedSizeLabel(String text, int width) {
        FancyJLabel label = new FancyJLabel(text, SwingConstants.LEFT);
        Dimension size = new Dimension(width, 40);
        label.setPreferredSize(size);
        label.setMinimumSize(size);
        label.setMaximumSize(size);
        return label;
    }

    private FancyJLabel createValueLabel(String text) {
        FancyJLabel label = new FancyJLabel(text, SwingConstants.LEFT);
        Dimension size = new Dimension(100, 40);
        label.setPreferredSize(size);
        label.setMinimumSize(size);
        label.setMaximumSize(size);
        return label;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            GameJPanel.setGameOver();
        }
    }

    public void updateMyUI() {
        SwingUtilities.invokeLater(() -> {
            scoreValueLabel.setText(String.format("%05d", Score.getTotalScore()));
            streakValueLabel.setText(String.format("%03d", Score.getStreakCount()));
            accuracyValueLabel.setText(String.format("%.2f%%", Score.getAverageAccuracy()));
        });
    }
}

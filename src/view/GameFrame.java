package view;

import service.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {

    private final FancyJLabel scoreValueLabel;
    private final FancyJLabel streakValueLabel;
    private final FancyJLabel accuracyValueLabel;

    public GameFrame(String levelName) {
        setTitle(levelName);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        int windowSize = 800;
        setSize(windowSize, windowSize + 41);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        int lineSpacing = windowSize / 4;
        int lineWidth = windowSize / 20;
        int firstLineX = (windowSize - lineSpacing) / 2 - lineWidth / 2;
        int secondLineX = firstLineX + lineSpacing;
        int horizontalHeight = windowSize / 3;
        GameJPanel gameJPanel = new GameJPanel(firstLineX, secondLineX, horizontalHeight, levelName, this);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowSize, windowSize));
        gameJPanel.setBounds(0, 0, windowSize, windowSize);
        layeredPane.add(gameJPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel uiPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = setupGridBagConstraints();

        int width = 120;
        FancyJLabel scoreLabel = createFixedSizeLabel("Score:", width);
        FancyJLabel streakLabel = createFixedSizeLabel("Streak:", width);
        FancyJLabel accuracyLabel = createFixedSizeLabel("Accuracy:", width);

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
        uiPanel.setBounds(windowSize - uiPanelWidth + uiPanelXOffset, uiPanelYOffset, uiPanelWidth, uiPanelHeight);
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

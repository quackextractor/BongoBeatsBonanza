package view;

import javax.swing.*;
import java.awt.*;

public class FancyJLabel extends JLabel {

    private Color shadowColor = Color.GRAY;
    private int shadowOffset = 2;
    private float shadowOpacity = 0.6f;
    private int defaultSize = 24;
    private String defaultFontName = "Arial";
    private int defaultFontStyle = Font.BOLD;

    public FancyJLabel(String text) {
        super(text);
        setFont(new Font(defaultFontName, defaultFontStyle, defaultSize));
    }

    public FancyJLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setFont(new Font(defaultFontName, defaultFontStyle, defaultSize));
    }

    public FancyJLabel() {
        setFont(new Font(defaultFontName, defaultFontStyle, defaultSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for text rendering
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw shadow
        g2d.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), (int) (shadowOpacity * 255)));
        g2d.drawString(getText(), shadowOffset, g2d.getFontMetrics().getAscent() + shadowOffset);

        // Draw the original text
        g2d.setColor(getForeground());
        g2d.drawString(getText(), 0, g2d.getFontMetrics().getAscent());

        g2d.dispose();
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        repaint();
    }

    public void setShadowOffset(int shadowOffset) {
        this.shadowOffset = shadowOffset;
        repaint();
    }

    public void setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
        repaint();
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
        updateFont();
    }

    public void setDefaultFontName(String defaultFontName) {
        this.defaultFontName = defaultFontName;
        updateFont();
    }

    public void setDefaultFontStyle(int defaultFontStyle) {
        this.defaultFontStyle = defaultFontStyle;
        updateFont();
    }

    private void updateFont() {
        setFont(new Font(defaultFontName, defaultFontStyle, defaultSize));
        repaint();
    }
}

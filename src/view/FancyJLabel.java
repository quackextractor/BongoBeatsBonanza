package view;

import javax.swing.*;
import java.awt.*;

/**
 * A JLabel subclass that provides additional features like text shadow.
 */
public class FancyJLabel extends JLabel {

    private Color shadowColor = Color.GRAY;
    private int shadowOffset = 3;
    private float shadowOpacity = 0.6f;
    private int defaultSize = 24;
    private String defaultFontName = "Arial";
    private int defaultFontStyle = Font.BOLD;

    /**
     * Constructs a FancyJLabel with the specified text.
     *
     * @param text The text to be displayed on the label.
     */
    public FancyJLabel(String text) {
        super(text);
        setFont(new Font(defaultFontName, defaultFontStyle, defaultSize));
    }

    /**
     * Constructs a FancyJLabel with the specified text and horizontal alignment.
     *
     * @param text              The text to be displayed on the label.
     * @param horizontalAlignment The horizontal alignment of the label's text.
     */
    public FancyJLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setFont(new Font(defaultFontName, defaultFontStyle, defaultSize));
    }

    /**
     * Constructs a FancyJLabel.
     */
    public FancyJLabel() {
        setFont(new Font(defaultFontName, defaultFontStyle, defaultSize));
    }

    /**
     * Overrides the paintComponent method to add text shadow effect.
     */
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

    /**
     * Sets the shadow color of the text.
     *
     * @param shadowColor The color of the text shadow.
     */
    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        repaint();
    }

    /**
     * Sets the offset of the text shadow.
     *
     * @param shadowOffset The offset of the text shadow.
     */
    public void setShadowOffset(int shadowOffset) {
        this.shadowOffset = shadowOffset;
        repaint();
    }

    /**
     * Sets the opacity of the text shadow.
     *
     * @param shadowOpacity The opacity of the text shadow.
     */
    public void setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
        repaint();
    }

    /**
     * Sets the default size of the label's font.
     *
     * @param defaultSize The default size of the font.
     */
    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
        updateFont();
    }

    /**
     * Sets the default font name of the label.
     *
     * @param defaultFontName The default font name.
     */
    public void setDefaultFontName(String defaultFontName) {
        this.defaultFontName = defaultFontName;
        updateFont();
    }

    /**
     * Sets the default font style of the label.
     *
     * @param defaultFontStyle The default font style.
     */
    public void setDefaultFontStyle(int defaultFontStyle) {
        this.defaultFontStyle = defaultFontStyle;
        updateFont();
    }

    /**
     * Updates the font of the label.
     */
    private void updateFont() {
        setFont(new Font(defaultFontName, defaultFontStyle, defaultSize));
        repaint();
    }
}

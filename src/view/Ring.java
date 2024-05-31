package view;

import java.awt.*;

/**
 * Represents a ring graphic that expands outward from a center point.
 * Used for visual effects in the game.
 */
public class Ring {
    private final Point center;
    private final int maxRadius;
    private final Color color;
    private int currentRadius;
    private final int expansionRate;
    private int thickness;

    /**
     * Constructs a Ring object with specified parameters.
     *
     * @param center The center point of the ring.
     * @param initialRadius The initial radius of the ring.
     * @param maxRadius The maximum radius the ring can reach.
     * @param color The color of the ring.
     * @param initialThickness The initial thickness of the ring.
     * @param expansionRate The rate at which the ring expands.
     */
    public Ring(Point center, int initialRadius, int maxRadius, Color color, int initialThickness, int expansionRate) {
        this.center = center;
        this.maxRadius = maxRadius;
        this.color = color;
        this.currentRadius = initialRadius;
        this.expansionRate = expansionRate;
        this.thickness = initialThickness;
    }

    /**
     * Checks if the ring has reached its maximum radius.
     *
     * @return True if the ring is expired (reached max radius), otherwise false.
     */
    public boolean isExpired() {
        return currentRadius >= maxRadius;
    }

    /**
     * Updates the ring's radius and thickness.
     */
    public void update() {
        currentRadius += expansionRate;
        thickness = Math.max(1, thickness - 1);
    }

    /**
     * Draws the ring on the graphics object.
     *
     * @param g The graphics object on which to draw the ring.
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        int diameter = currentRadius * 2;
        int x = center.x - currentRadius;
        int y = center.y - currentRadius;
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawOval(x, y, diameter, diameter);
    }
}

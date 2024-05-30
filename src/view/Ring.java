package view;

import java.awt.*;

public class Ring {
    private final Point center;
    private final int maxRadius;
    private final Color color;
    private int currentRadius;
    private final int expansionRate;
    private int thickness;

    public Ring(Point center, int initialRadius, int maxRadius, Color color, int initialThickness, int expansionRate) {
        this.center = center;
        this.maxRadius = maxRadius;
        this.color = color;
        this.currentRadius = initialRadius;
        this.expansionRate = expansionRate;
        this.thickness = initialThickness;
    }

    public boolean isExpired() {
        return currentRadius >= maxRadius;
    }

    public void update() {
        currentRadius += expansionRate;
        thickness = Math.max(1, thickness - 1);
    }

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

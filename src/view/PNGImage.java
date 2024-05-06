package view;

public class PNGImage {
    private int lane;
    private int yPos;

    public PNGImage(int lane) {
        this.lane = lane;
        this.yPos = 600; // Initial position at the bottom of the screen
    }

    public void move() {
        yPos -= 5; // Move upwards
    }

    public int getLane() {
        return lane;
    }

    public int getYPos() {
        return yPos;
    }
}
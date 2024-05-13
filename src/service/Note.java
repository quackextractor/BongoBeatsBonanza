package service;

import java.awt.*;

public class Note {
    private final int size;
    // distance from target
    private int distance;
    private final int xPos;
    private int yPos;
    private final Image noteImage;

    public int getDeadZone() {
        return deadZone;
    }

    private final int deadZone;

    public Note(int spawnDistance, Image noteImage, int targetXPos, int targetYPos, int size) {
        this.distance = spawnDistance;
        this.noteImage = noteImage;
        this.xPos = targetXPos;
        this.yPos = targetYPos + spawnDistance;
        this.size = size;
        deadZone = 20;
    }

    public int getDistance() {
        // distance is always positive
        return Math.abs(distance);
    }

    public void move(int amount){
        distance -= amount;
        yPos -= amount;
    }


    // Method to reset the note properties to their initial state
    public void reset(int spawnDistance, int targetYPos) {
        this.distance = spawnDistance;
        this.yPos = targetYPos + spawnDistance;
    }

    public int getSize() {
        return size;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public Image getNoteImage() {
        return noteImage;
    }
}

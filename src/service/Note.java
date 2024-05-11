package service;

import java.awt.*;

public class Note {
    private int size;
    // distance from target
    private int distance;
    private final int xPos;
    private int yPos;
    private final Image noteImage;
    private final Track track;

    public Note(int spawnDistance, Image noteImage, int targetXPos, int targetYPos, Track track) {
        this.distance = spawnDistance;
        this.noteImage = noteImage;
        this.xPos = targetXPos;
        this.track = track;
        this.yPos = targetYPos + spawnDistance;
    }

    public int getDistance() {
        // distance is always positive
        return Math.abs(distance);
    }

    public void move(int amount){
        distance -= amount;
        yPos -= amount;
        checkPos();
    }

    public void checkPos(){
        if(yPos<=0){
            track.removeNoteFromTrack(this);
        }
    }

    // Method to reset the note properties to their initial state
    public void reset(int spawnDistance, int targetYPos) {
        this.distance = spawnDistance;
        this.yPos = targetYPos + spawnDistance;
    }
}

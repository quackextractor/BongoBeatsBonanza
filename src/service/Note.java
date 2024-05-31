package service;

import java.awt.*;

/**
 * The {@code Note} class represents a note in the game.
 * It defines properties and behavior related to notes.
 */
public class Note {
    private final int size;
    private int distance;
    private final int xPos;
    private int yPos;
    private final Image noteImage;
    private final int deadZone;

    /**
     * Constructs a {@code Note} object with the specified parameters.
     *
     * @param spawnDistance the distance from where the note spawns
     * @param noteImage     the image representing the note
     * @param targetXPos    the x-coordinate of the target position
     * @param targetYPos    the y-coordinate of the target position
     * @param size          the size of the note
     */
    public Note(int spawnDistance, Image noteImage, int targetXPos, int targetYPos, int size) {
        this.distance = spawnDistance;
        this.noteImage = noteImage;
        this.xPos = targetXPos;
        this.yPos = targetYPos + spawnDistance;
        this.size = size;
        deadZone = 20;
    }

    /**
     * Gets the distance of the note from the target.
     *
     * @return the distance from the target
     */
    public int getDistance() {
        return Math.abs(distance);
    }

    /**
     * Moves the note by the specified amount.
     *
     * @param amount the amount to move the note by
     */
    public void move(int amount){
        distance -= amount;
        yPos -= amount;
    }

    /**
     * Resets the note properties to their initial state.
     *
     * @param spawnDistance the distance from where the note spawns
     * @param targetYPos    the y-coordinate of the target position
     */
    public void reset(int spawnDistance, int targetYPos) {
        this.distance = spawnDistance;
        this.yPos = targetYPos + spawnDistance;
    }

    /**
     * Gets the size of the note.
     *
     * @return the size of the note
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the x-coordinate of the note.
     *
     * @return the x-coordinate of the note
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * Gets the y-coordinate of the note.
     *
     * @return the y-coordinate of the note
     */
    public int getYPos() {
        return yPos;
    }

    /**
     * Gets the image representing the note.
     *
     * @return the image representing the note
     */
    public Image getNoteImage() {
        return noteImage;
    }

    /**
     * Gets the dead zone distance.
     *
     * @return the dead zone distance
     */
    public int getDeadZone() {
        return deadZone;
    }
}

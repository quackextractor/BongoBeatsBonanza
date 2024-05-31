package service;

import view.GameJPanel;
import view.Ring;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@code MusicTrack} class represents a track for notes in the game.
 * It manages the spawning, movement, catching, and drawing of notes.
 */
public class MusicTrack {

    private final ConcurrentLinkedQueue<Note> notesOnTrack;
    private final NotePool notePool;
    private final int maxHitDistance;

    private final int targetXPos;
    private final int targetYPos;
    private final int spawnDistance;
    private final int noteSize;
    private final Image noteImage;

    /**
     * Constructs a {@code MusicTrack} object with the specified parameters.
     *
     * @param notePool       the note pool to retrieve notes from
     * @param maxHitDistance the maximum distance for a successful hit
     * @param noteImage      the image representing the note
     * @param noteSize       the size of the note
     * @param targetXPos     the x-coordinate of the target position
     * @param targetYPos     the y-coordinate of the target position
     * @param spawnDistance  the distance from where notes spawn
     */
    public MusicTrack(NotePool notePool, int maxHitDistance, Image noteImage, int noteSize, int targetXPos, int targetYPos, int spawnDistance) {
        this.notePool = notePool;
        this.maxHitDistance = maxHitDistance;
        notesOnTrack = new ConcurrentLinkedQueue<>();

        this.noteImage = noteImage;
        this.targetXPos = targetXPos;
        this.targetYPos = targetYPos;
        this.spawnDistance = spawnDistance;
        this.noteSize = noteSize;
    }

    /**
     * Removes a note from the track.
     *
     * @param note the note to remove
     */
    public void removeNoteFromTrack(Note note) {
        if (notesOnTrack.contains(note)) {
            notesOnTrack.remove(note);
            notePool.returnNoteToPool(note, spawnDistance, targetYPos);
        }
    }

    /**
     * Moves all notes on the track by the specified amount.
     *
     * @param amount the amount to move the notes by
     */
    public void moveNotes(int amount) {
        Iterator<Note> iterator = notesOnTrack.iterator();
        while (iterator.hasNext()) {
            Note note = iterator.next();
            if (note != null) {
                note.move(amount);
                if (note.getYPos() <= note.getDeadZone()) {
                    iterator.remove();
                    Score.miss();
                }
            }
        }
    }

    /**
     * Adds a note to the track.
     */
    public void addNoteToTrack() {
        notesOnTrack.add(notePool.getNote(spawnDistance, noteImage, targetXPos, targetYPos, noteSize));
    }

    /**
     * Handles catching a note on the track.
     */
    public void catchNote() {
        if (notesOnTrack.isEmpty()) {
            Score.miss();
            return;
        }

        Note noteWithMinDistance = Collections.min(notesOnTrack, Comparator.comparingDouble(Note::getDistance));
        int minDistance = noteWithMinDistance.getDistance();

        String accuracy = Score.determineAccuracy(minDistance, maxHitDistance);

        if (!accuracy.equals("MISS")){
            removeNoteFromTrack(noteWithMinDistance);
        }

        Ring ring = getRing(accuracy);
        GameJPanel.addRing(ring);
    }

    private Ring getRing(String accuracy) {
        Color ringColor;
        Point notePosition = new Point(targetXPos, targetYPos);

        switch (accuracy) {
            case "GREAT":
                ringColor = Color.CYAN;
                break;
            case "GOOD":
                ringColor = Color.GREEN;
                break;
            case "BAD":
                ringColor = Color.RED;
                break;
            default:
                ringColor = Color.GRAY;
                return new Ring(notePosition, noteSize/4, noteSize/2, ringColor, 5, 1);
        }
        return new Ring(notePosition, noteSize/2, (int) (noteSize*1.5), ringColor, 10, 2);
    }

    /**
     * Draws all notes on the track.
     *
     * @param g the graphics context to draw on
     */
    public void drawNotes(Graphics g) {
        for (Note note : notesOnTrack) {
            int noteX = note.getXPos();
            int noteY = note.getYPos();
            int noteSize = note.getSize();
            int offset = noteSize / 2;
            Image noteImage = note.getNoteImage();

            g.drawImage(noteImage, noteX - offset, noteY - offset, noteSize, noteSize, null);
        }
    }

}

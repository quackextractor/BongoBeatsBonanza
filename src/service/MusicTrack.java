package service;

import view.GameJPanel;
import view.Ring;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MusicTrack {

    private ConcurrentLinkedQueue<Note> notesOnTrack;
    private NotePool notePool;
    private int maxHitDistance;
    private Comparator<Note> noteDistanceComparator = Comparator.comparingDouble(Note::getDistance);

    private final int targetXPos;
    private final int targetYPos;
    private final int spawnDistance;
    private final int noteSize;
    private final Image noteImage;

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

    public void removeNoteFromTrack(Note note) {
        if (notesOnTrack.contains(note)) {
            notesOnTrack.remove(note);
            notePool.returnNoteToPool(note, spawnDistance, targetYPos);
        }
    }

    public void moveNotes(int amount) {
        Iterator<Note> iterator = notesOnTrack.iterator();
        while (iterator.hasNext()) {
            Note note = iterator.next();
            if (note != null) {
                note.move(amount);
                if (note.getYPos() <= note.getDeadZone()) {
                    // Remove the note from the track if it reaches the dead zone
                    iterator.remove();
                    Score.miss();
                }
            }
        }
    }

    public void addNoteToTrack() {
        notesOnTrack.add(notePool.getNote(spawnDistance, noteImage, targetXPos, targetYPos, noteSize));
    }

    public String catchNote() {
        // Checks if track is empty first to skip evaluation
        if (notesOnTrack.isEmpty()) {
            Score.miss();
            return "MISS";
        }

        Note noteWithMinDistance = Collections.min(notesOnTrack, noteDistanceComparator);
        int minDistance = noteWithMinDistance.getDistance();

        if (minDistance > maxHitDistance) {
            Score.miss();
            return "MISS";
        }

        // remove caught note
        removeNoteFromTrack(noteWithMinDistance);
        String accuracy = Score.determineAccuracy(minDistance, maxHitDistance);

        // Determine ring color based on accuracy
        Color ringColor;
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
        }

        // Create and add a ring
        Point notePosition = new Point(targetXPos, targetYPos);
        Ring ring = new Ring(notePosition, 10, 100, ringColor, 10, 2);
        GameJPanel.addRing(ring);

        return accuracy;
    }


    public void drawNotes(Graphics g) {
        for (Note note : notesOnTrack) {
            int noteX = note.getXPos();
            int noteY = note.getYPos();
            int noteSize = note.getSize();
            int offset = noteSize / 2;
            Image noteImage = note.getNoteImage();

            // Draw the note image at the specified position with size
            g.drawImage(noteImage, noteX - offset, noteY - offset, noteSize, noteSize, null);
        }
    }

}

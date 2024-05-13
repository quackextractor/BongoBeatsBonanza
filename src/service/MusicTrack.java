package service;

import java.awt.*;
import java.util.*;

public class MusicTrack {

    private ArrayList<Note> notesOnTrack;
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
        notesOnTrack = new ArrayList<>();

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
                    AccuracyCalculator.miss();
                }
            }
        }
    }

    public void addNoteToTrack() {
        notesOnTrack.add(notePool.getNote(spawnDistance, noteImage, targetXPos, targetYPos, noteSize));
    }

    // Catches Notes and gets accuracy
    public double catchNote() {
        // Checks if track is empty first to skip evaluation
        if (notesOnTrack.isEmpty()) {
            AccuracyCalculator.miss();
            return 0;
        }

        Note noteWithMinDistance = Collections.min(notesOnTrack, noteDistanceComparator);
        int minDistance = noteWithMinDistance.getDistance();

        if (minDistance > maxHitDistance) {
            AccuracyCalculator.miss();
            return 0;
        }

        // remove caught note
        removeNoteFromTrack(noteWithMinDistance);
        return AccuracyCalculator.determineAccuracy(minDistance, maxHitDistance);
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

            // Draw a black filled circle
            // g.setColor(Color.BLACK);
            // g.fillOval(noteX, noteY, noteSize, noteSize);

            // UNTESTED
            // Draw guidelines below the note
            //  g.setColor(Color.RED); // Set the color of the guidelines
            //  g.drawLine(noteX - 10, noteY + noteSize, noteX + noteSize + 10, noteY + noteSize);
        }
    }

}

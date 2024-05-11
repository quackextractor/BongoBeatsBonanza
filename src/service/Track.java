package service;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Track {

    private ArrayList<Note> notesOnTrack;
    private NotePool notePool;
    private int maxHitDistance;
    private Comparator<Note> noteDistanceComparator = Comparator.comparingDouble(Note::getDistance);

    public Track(NotePool notePool, int maxHitDistance) {
        this.notePool = notePool;
        this.maxHitDistance = maxHitDistance;
        notesOnTrack = new ArrayList<>();
    }

    public void removeNoteFromTrack(Note note) {
        if (notesOnTrack.contains(note)) {
            notesOnTrack.remove(note);
            notePool.returnNoteToPool(note);
        }
    }

    public void moveNotes(int amount) {
        for (Note note : notesOnTrack) {
            note.move(amount);
        }
    }

    public void addNoteToTrack(){
        notesOnTrack.add(notePool.getNote());
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
            Image noteImage = note.getNoteImage();

            // Draw the note image at the specified position with size
            g.drawImage(noteImage, noteX, noteY, noteSize, noteSize, null);

            // UNTESTED
            // Draw guidelines below the note
          //  g.setColor(Color.RED); // Set the color of the guidelines
          //  g.drawLine(noteX - 10, noteY + noteSize, noteX + noteSize + 10, noteY + noteSize);
        }
    }

}

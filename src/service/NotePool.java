package service;

import java.awt.*;
import java.util.LinkedList;

public class NotePool {
    private LinkedList<Note> noteLinkedList; // LinkedList for dynamic pool size

    public void setNoteImage(Image noteImage) {
        this.noteImage = noteImage;
    }

    // Note variables
    private Image noteImage;
    private final int targetXPos;
    private final int targetYPos;
    private final int spawnDistance;
    private final Track track;
    private final int noteSize;


    public NotePool(int initialSize, Image noteImage, int noteSize, int targetXPos, int targetYPos, int spawnDistance, Track track) {
        this.noteImage = noteImage;
        this.targetXPos = targetXPos;
        this.targetYPos = targetYPos;
        this.spawnDistance = spawnDistance;
        this.track = track;
        this.noteSize = noteSize;
        noteLinkedList = new LinkedList<>();

        // Pre-allocate note objects and add them to the LinkedList
        for (int i = 0; i < initialSize; i++) {
            Note note = new Note(spawnDistance, noteImage, targetXPos, targetYPos, track, noteSize);
            noteLinkedList.add(note);
        }
    }

    // Method to retrieve a note from the pool
    public Note getNote() {
        if (noteLinkedList.isEmpty()) {
            // If the LinkedList is empty, create a new note
            return new Note(spawnDistance, noteImage, targetXPos, targetYPos, track, noteSize);
        } else {
            // Otherwise, retrieve a note from the LinkedList
            return noteLinkedList.removeFirst();
        }
    }

    // Method to return a note to the pool
    public void returnNoteToPool(Note note) {
        // Reset note properties
        note.reset(spawnDistance, targetYPos);

        // Add the note back to the LinkedList
        noteLinkedList.add(note);
    }
}
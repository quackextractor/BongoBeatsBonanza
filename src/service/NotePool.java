package service;

import java.awt.*;
import java.util.LinkedList;

public class NotePool {
    private LinkedList<Note> noteLinkedList; // LinkedList for dynamic pool size
    private final int initialSize;


    public NotePool(int initialSize) {
        this.initialSize = initialSize;
        noteLinkedList = new LinkedList<>();
    }

    public void setUpNotePool(int spawnDistance, Image noteImage, int targetXPos, int targetYPos, int noteSize){
        // Pre-allocate note objects and add them to the LinkedList
        for (int i = 0; i < initialSize; i++) {
            Note note = new Note(spawnDistance, noteImage, targetXPos, targetYPos, noteSize);
            noteLinkedList.add(note);
        }
    }

    // Method to retrieve a note from the pool
    public Note getNote(int spawnDistance, Image noteImage, int targetXPos, int targetYPos, int noteSize) {
        if (noteLinkedList.isEmpty()) {
            // If the LinkedList is empty, create a new note
            return new Note(spawnDistance, noteImage, targetXPos, targetYPos, noteSize);
        } else {
            // Otherwise, retrieve a note from the LinkedList
            return noteLinkedList.removeFirst();
        }
    }

    // Method to return a note to the pool
    public void returnNoteToPool(Note note, int spawnDistance, int targetYPos) {
        // Reset note properties
        note.reset(spawnDistance, targetYPos);

        // Add the note back to the LinkedList
        noteLinkedList.add(note);
    }
}
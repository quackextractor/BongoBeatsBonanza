package service;

import java.awt.*;
import java.util.LinkedList;

/**
 * The {@code NotePool} class represents a pool of notes that can be reused to optimize memory usage.
 * It provides methods to retrieve notes from the pool and return them back to the pool when they are no longer needed.
 */
public class NotePool {
    private final LinkedList<Note> noteLinkedList;
    private final int initialSize;

    /**
     * Constructs a {@code NotePool} with the specified initial size.
     *
     * @param initialSize the initial size of the note pool
     */
    public NotePool(int initialSize) {
        this.initialSize = initialSize;
        noteLinkedList = new LinkedList<>();
    }

    /**
     * Sets up the note pool by pre-allocating note objects and adding them to the pool.
     *
     * @param spawnDistance the distance at which notes are spawned
     * @param noteImage     the image representing the note
     * @param targetXPos    the x-coordinate of the target position
     * @param targetYPos    the y-coordinate of the target position
     * @param noteSize      the size of the note
     */
    public void setUpNotePool(int spawnDistance, Image noteImage, int targetXPos, int targetYPos, int noteSize) {
        for (int i = 0; i < initialSize; i++) {
            Note note = new Note(spawnDistance, noteImage, targetXPos, targetYPos, noteSize);
            noteLinkedList.add(note);
        }
    }

    /**
     * Retrieves a note from the pool.
     *
     * @param spawnDistance the distance at which the note is spawned
     * @param noteImage     the image representing the note
     * @param targetXPos    the x-coordinate of the target position
     * @param targetYPos    the y-coordinate of the target position
     * @param noteSize      the size of the note
     * @return a note object
     */
    public Note getNote(int spawnDistance, Image noteImage, int targetXPos, int targetYPos, int noteSize) {
        if (noteLinkedList.isEmpty()) {
            return new Note(spawnDistance, noteImage, targetXPos, targetYPos, noteSize);
        } else {
            return noteLinkedList.removeFirst();
        }
    }

    /**
     * Returns a note to the pool.
     *
     * @param note          the note to be returned
     * @param spawnDistance the distance at which notes are spawned
     * @param targetYPos    the y-coordinate of the target position
     */
    public void returnNoteToPool(Note note, int spawnDistance, int targetYPos) {
        note.reset(spawnDistance, targetYPos);
        noteLinkedList.add(note);
    }
}
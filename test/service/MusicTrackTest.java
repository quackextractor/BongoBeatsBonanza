package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameJPanel;
import view.Ring;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

class MusicTrackTest {

    private MusicTrack musicTrack;
    private Image noteImage;

    @BeforeEach
    void setUp() {
        // Initialize the NotePool and MusicTrack
        NotePool notePool = new NotePool(10);
        // Image for note
        noteImage = new Canvas().createImage(10, 10);
        musicTrack = new MusicTrack(notePool, 100, noteImage, 20, 300, 400, 500);

        // Initialize the static fields in GameJPanel
        initializeGameJPanel();
    }

    @Test
    void catchNote_noNotes() {
        // Ensure there are no notes on the track initially
        assertTrue(Objects.requireNonNull(getNotesOnTrack()).isEmpty());

        // Record initial miss count
        int initialMissCount = Score.getMissCount();

        musicTrack.catchNote();

        // Check if a miss was recorded
        assertEquals(initialMissCount + 1, Score.getMissCount());
    }

    @Test
    void catchNote_noteHit() {
        // Create and add a note to the track
        Note note = new Note(500, noteImage, 300, 400, 20);
        note.setYPos(50);
        addNoteToTrack(note);

        // Set the distance to a value within maxHitDistance
        setNoteDistance(note, 50);

        // Record initial score counts
        int initialMissCount = Score.getMissCount();

        musicTrack.catchNote();

        // Verify the note was removed from the track
        assertFalse(Objects.requireNonNull(getNotesOnTrack()).contains(note));

        // Verify no additional miss was recorded
        assertEquals(initialMissCount, Score.getMissCount());

        // Verify a ring was added to GameJPanel
        assertFalse(Objects.requireNonNull(getRings()).isEmpty());
    }

    @Test
    void catchNote_noteMissed() {
        // Create and add a note to the track
        Note note = new Note(500, noteImage, 300, 400, 20);
        note.setYPos(50);
        addNoteToTrack(note);

        // Set the distance to a value beyond maxHitDistance
        setNoteDistance(note, 150);

        // Record initial miss count
        int initialMissCount = Score.getMissCount();

        musicTrack.catchNote();

        // Verify the note was not removed from the track
        assertTrue(Objects.requireNonNull(getNotesOnTrack()).contains(note));

        // Verify miss
        assertEquals(initialMissCount + 1, Score.getMissCount());

        // Verify ring was added to GameJPanel
        assertFalse(Objects.requireNonNull(getRings()).isEmpty());
    }

    // Utility methods
    @SuppressWarnings("unchecked")
    private ConcurrentLinkedQueue<Note> getNotesOnTrack() {
        try {
            var field = MusicTrack.class.getDeclaredField("notesOnTrack");
            field.setAccessible(true);
            return (ConcurrentLinkedQueue<Note>) field.get(musicTrack);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access notesOnTrack field.");
            return null;
        }
    }

    private void addNoteToTrack(Note note) {
        ConcurrentLinkedQueue<Note> notesOnTrack = getNotesOnTrack();
        assert notesOnTrack != null;
        notesOnTrack.add(note);
    }

    private void setNoteDistance(Note note, int distance) {
        try {
            var field = Note.class.getDeclaredField("distance");
            field.setAccessible(true);
            field.setInt(note, distance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set note distance.");
        }
    }

    @SuppressWarnings("unchecked")
    private List<Ring> getRings() {
        try {
            var field = GameJPanel.class.getDeclaredField("rings");
            field.setAccessible(true);
            return (List<Ring>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access rings field.");
            return null;
        }
    }

    private void initializeGameJPanel() {
        try {
            var field = GameJPanel.class.getDeclaredField("rings");
            field.setAccessible(true);
            field.set(null, new java.util.ArrayList<Ring>()); // Initialize as an empty list
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to initialize GameJPanel rings field.");
        }
    }
}

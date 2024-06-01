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
        NotePool notePool = new NotePool(10);
        noteImage = new Canvas().createImage(10, 10);
        musicTrack = new MusicTrack(notePool, 100, noteImage, 20, 300, 400, 500);
        initializeGameJPanel();
    }

    @Test
    void catchNoteNoNotes() {
        assertTrue(Objects.requireNonNull(getNotesOnTrack()).isEmpty());
        int initialMissCount = Score.getMissCount();
        musicTrack.catchNote();
        assertEquals(initialMissCount + 1, Score.getMissCount());
    }

    @Test
    void catchNoteNoteHit() {
        Note note = new Note(500, noteImage, 300, 400, 20);
        note.setYPos(50);
        addNoteToTrack(note);
        setNoteDistance(note, 50);
        int initialMissCount = Score.getMissCount();
        musicTrack.catchNote();
        assertFalse(Objects.requireNonNull(getNotesOnTrack()).contains(note));
        assertEquals(initialMissCount, Score.getMissCount());
        assertFalse(Objects.requireNonNull(getRings()).isEmpty());
    }

    @Test
    void catchNoteNoteMissed() {
        Note note = new Note(500, noteImage, 300, 400, 20);
        note.setYPos(50);
        addNoteToTrack(note);
        setNoteDistance(note, 150);
        int initialMissCount = Score.getMissCount();
        musicTrack.catchNote();
        assertTrue(Objects.requireNonNull(getNotesOnTrack()).contains(note));
        assertEquals(initialMissCount + 1, Score.getMissCount());
        assertFalse(Objects.requireNonNull(getRings()).isEmpty());
    }

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
            field.set(null, new java.util.ArrayList<Ring>());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to initialize GameJPanel rings field.");
        }
    }
}
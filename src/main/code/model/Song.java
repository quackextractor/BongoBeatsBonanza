package main.code.model;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class Song {
    private Sequence sequence;

    public Song(String filePath) throws InvalidMidiDataException, IOException {
        this.sequence = loadMidi(filePath);
    }

    // Method for getting midi sequence
    private Sequence loadMidi(String filePath) throws InvalidMidiDataException, IOException {
        File file = new File(filePath);
        return MidiSystem.getSequence(file);
    }

    public Sequence getSequence() {
        return sequence;
    }
}

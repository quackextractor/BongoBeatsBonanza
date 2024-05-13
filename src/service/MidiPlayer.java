package service;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

// TODO add musicPlayer logic
public class MidiPlayer {
    private Sequencer sequencer;
    private Track track1;
    private Track track2;
    private MusicTrack musicTrack1;
    private MusicTrack musicTrack2;

    public MidiPlayer(MusicTrack musicTrack1, MusicTrack musicTrack2, String level) {
        this.musicTrack1 = musicTrack1;
        this.musicTrack2 = musicTrack2;
        initializeSequencer();
    }

    private void initializeSequencer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void loadAndPlayMidi(String midiFilePath) {
        try {
            Sequence sequence = MidiSystem.getSequence(new File(midiFilePath));
            sequencer.setSequence(sequence);

            // Get the tracks from the sequence
            Track[] tracks = sequence.getTracks();
            // Assuming there are two tracks in the MIDI file
            if (tracks.length >= 2) {
                track1 = tracks[0];
                track2 = tracks[1];
            }

            // Start playing the MIDI sequence in a separate thread
            Thread musicThread = new Thread(() -> {
                sequencer.start();
                while (sequencer.isRunning()) {
                    addNotesFromMidiEvents(track1, musicTrack1);
                    addNotesFromMidiEvents(track2, musicTrack2);
                    try {
                        Thread.sleep(10); // Adjust sleep time as needed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            musicThread.start();

        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addNotesFromMidiEvents(Track track, MusicTrack musicTrack) {
        for (int i = 0; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage) {
                ShortMessage shortMessage = (ShortMessage) message;
                    // Assuming note on messages represent when a note should start
                if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                    // int noteValue = shortMessage.getData1();
                    // Convert MIDI note value to target Y position (adjust as needed)
                    // int targetYPos = 100; // Example target Y position
                    // Add note to the pool with appropriate parameters
                    musicTrack.addNoteToTrack();
                }
            }
        }
    }

    public void stopMusic() {
        sequencer.stop();
    }
}

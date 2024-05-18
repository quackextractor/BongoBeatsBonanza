package service;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

// TODO add musicPlayer logic, Replace e.printStackTrace(); with ErrorLogger.logStackTrace(e);
public class MidiPlayer {
    private Sequencer sequencer;
    private Track track1;
    private Track track2;
    private MusicTrack musicTrack1;
    private MusicTrack musicTrack2;
    private String midiFilePath;
    private String musicFilePath;
    private int delayMs;
    MusicPlayer songPlayer;

    public MidiPlayer(MusicTrack musicTrack1, MusicTrack musicTrack2, String levelName, int delayMs) {
        this.musicTrack1 = musicTrack1;
        this.musicTrack2 = musicTrack2;
        String levelPath = "src/resources/levels/"+levelName;
        midiFilePath = levelPath+".mid";
        musicFilePath = levelPath+".wav";
        songPlayer = new MusicPlayer(true,musicFilePath);
        this.delayMs = delayMs;
        initializeSequencer();
    }

    private void initializeSequencer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (MidiUnavailableException e) {
           ErrorLogger.logStackTrace(e);
        }
    }

    public void loadAndPlayMidi() {
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
                }
            });
            musicThread.start();
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            songPlayer.playDefault();

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

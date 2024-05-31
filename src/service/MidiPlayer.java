package service;

import view.GameJPanel;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The MidiPlayer class handles the loading and playing of MIDI files for the game tracks.
 * It synchronizes the playback of MIDI events with the visual elements on the game panel.
 */
public class MidiPlayer {
    private Sequencer sequencer;
    private final MusicTrack musicTrack1;
    private final MusicTrack musicTrack2;
    private final String midiFilePath;
    private final int delayMs;
    private final MusicPlayer songPlayer;
    private float bpm;
    public static int totalNotes;
    private volatile boolean stopRequested;

    /**
     * Constructs a MidiPlayer object with the specified parameters.
     *
     * @param musicTrack1 The first music track to synchronize with MIDI events.
     * @param musicTrack2 The second music track to synchronize with MIDI events.
     * @param levelName   The name of the level associated with the MIDI file.
     * @param delayMs     The delay in milliseconds before starting the MIDI playback.
     */
    public MidiPlayer(MusicTrack musicTrack1, MusicTrack musicTrack2, String levelName, int delayMs) {
        totalNotes = 0;
        this.musicTrack1 = musicTrack1;
        this.musicTrack2 = musicTrack2;
        String levelPath = "resources/levels/" + levelName;
        midiFilePath = levelPath + ".mid";
        String musicFilePath = levelPath + ".wav";
        songPlayer = new MusicPlayer(true, musicFilePath);
        this.delayMs = delayMs;
        initializeSequencer();
        stopRequested = false;  // Initialize the stop flag
    }

    /**
     * Initializes the MIDI sequencer.
     */
    private void initializeSequencer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (MidiUnavailableException e) {
            ErrorLogger.logStackTrace(e);
        }
    }

    /**
     * Loads and plays the MIDI file.
     */
    public void loadAndPlayMidi() {
        if (GameJPanel.isGameOver()) {
            return;
        }
        try {
            Sequence sequence = MidiSystem.getSequence(new File(midiFilePath));
            sequencer.setSequence(sequence);

            Track[] tracks = sequence.getTracks();
            System.out.println("Number of tracks: " + tracks.length);

            if (tracks.length > 0) {
                List<MidiEvent> allNoteEvents = new ArrayList<>();

                // Collect all NOTE_ON events and find the tempo
                for (Track track : tracks) {
                    for (int i = 0; i < track.size(); i++) {
                        MidiEvent event = track.get(i);
                        MidiMessage message = event.getMessage();
                        if (message instanceof ShortMessage shortMessage) {
                            if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                                allNoteEvents.add(event);
                            }
                        } else if (message instanceof MetaMessage metaMessage) {
                            if (metaMessage.getType() == 0x51) {
                                bpm = getBpmFromMetaMessage(metaMessage);
                                System.out.println("BPM: " + bpm);
                            }
                        }
                    }
                }

                totalNotes = allNoteEvents.size();
                System.out.println("Total NOTE_ON events: " + totalNotes);

                long pauseDurationMs = 0;
                if (!allNoteEvents.isEmpty()) {
                    // Calculate the pause before the first note
                    long firstNoteTick = allNoteEvents.get(0).getTick();
                    pauseDurationMs = tickToMs(firstNoteTick, bpm);
                    System.out.println("Pause before the first note: " + pauseDurationMs + " ms");
                }
                System.out.println("Added note speed delay: " + delayMs + " ms");
                int totalPauseDuration = (int) (delayMs + pauseDurationMs);
                System.out.println("Total duration: " + totalPauseDuration + " ms");

                // Process events and divide them into two tracks
                processMidiEvents(allNoteEvents);

                // Wait for the specified delay before playing the music file
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    ErrorLogger.logStackTrace(e);
                }

                if (GameJPanel.isGameOver()) {
                    sequencer.stop();
                    return;
                }

                // Play the music file
                songPlayer.playDefault();
            } else {
                System.err.println("Error: No tracks found in the MIDI file.");
            }

        } catch (InvalidMidiDataException | IOException e) {
            ErrorLogger.logStackTrace(e);
        }
    }


    /**
     * Processes MIDI events by grouping them based on pitch and distributing them to corresponding music tracks.
     *
     * @param allNoteEvents The list of MIDI events to process.
     */
    private void processMidiEvents(List<MidiEvent> allNoteEvents) {
        Map<Integer, List<MidiEvent>> pitchToEventsMap = new HashMap<>();

        // Group events by pitch
        for (MidiEvent event : allNoteEvents) {
            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage shortMessage) {
                if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                    int pitch = shortMessage.getData1();
                    pitchToEventsMap.computeIfAbsent(pitch, k -> new ArrayList<>()).add(event);
                }
            }
        }

        // Sort pitches by the number of events
        List<Map.Entry<Integer, List<MidiEvent>>> sortedPitches = new ArrayList<>(pitchToEventsMap.entrySet());
        sortedPitches.sort(Comparator.comparingInt(e -> e.getValue().size()));

        // Merge the events of the pitches with the fewest notes into two main lists
        List<MidiEvent> eventsForTrack1 = new ArrayList<>();
        List<MidiEvent> eventsForTrack2 = new ArrayList<>();

        for (int i = 0; i < sortedPitches.size(); i++) {
            if (i % 2 == 0) {
                eventsForTrack1.addAll(sortedPitches.get(i).getValue());
            } else {
                eventsForTrack2.addAll(sortedPitches.get(i).getValue());
            }
        }

        // Process events for both tracks
        new Thread(() -> processEventsForMusicTrack(eventsForTrack1, musicTrack1)).start();
        new Thread(() -> processEventsForMusicTrack(eventsForTrack2, musicTrack2)).start();
    }


    /**
     * Processes MIDI events for the specified music track.
     *
     * @param events     The MIDI events to process.
     * @param musicTrack The music track to process events for.
     */
    private void processEventsForMusicTrack(List<MidiEvent> events, MusicTrack musicTrack) {
        long lastTick = 0;

        // Ensure events are sorted by tick
        events.sort(Comparator.comparingLong(MidiEvent::getTick));

        for (MidiEvent event : events) {
            if (stopRequested) {  // Check if stop is requested
                break;
            }

            long currentTick = event.getTick();
            long tickDifference = currentTick - lastTick;

            // Calculate the delay in milliseconds based on the tempo
            long delayInMs = tickToMs(tickDifference, bpm);

            // This rarely ever happens, couldn't replicate issue
            if (delayInMs < 0) {
                System.err.println("Warning: Negative delay detected. Setting delay to 0.");
                delayInMs = 0;  // Ensure that delayInMs is non-negative
            }

            try {
                Thread.sleep(delayInMs);
            } catch (InterruptedException e) {
                ErrorLogger.logStackTrace(e);
            }

            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage shortMessage) {
                if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                    musicTrack.addNoteToTrack();
                }
            }

            lastTick = currentTick;
        }
    }

    /**
     * Converts ticks to milliseconds based on the given tempo.
     *
     * @param ticks      The number of ticks.
     * @param tempoInBPM The tempo in beats per minute.
     * @return The equivalent milliseconds.
     */
    private long tickToMs(long ticks, float tempoInBPM) {
        float ticksPerBeat = sequencer.getSequence().getResolution();
        float msPerBeat = 60000 / tempoInBPM;
        float msPerTick = msPerBeat / ticksPerBeat;
        return (long) (ticks * msPerTick);
    }

    /**
     * Retrieves the BPM (beats per minute) from the given meta message.
     *
     * @param metaMessage The meta message containing tempo information.
     * @return The BPM value.
     */
    private float getBpmFromMetaMessage(MetaMessage metaMessage) {
        byte[] data = metaMessage.getData();
        int tempo = ((data[0] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[2] & 0xff);
        return 60000000f / tempo;
    }

    /**
     * Stops the MIDI and music playback.
     */
    public void stopMusic() {
        // Signal the threads to stop
        stopRequested = true;
        sequencer.stop();
        songPlayer.stop();
    }

    public long getMicroSecondPos() {
        return songPlayer.getMicroSecondPos();
    }

    public long getMicroSecondLength() {
        return songPlayer.getMicroSecondLength();
    }

    public static int getTotalNotes() {
        return totalNotes;
    }
}
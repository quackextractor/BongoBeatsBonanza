package service;

import view.GameJPanel;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MidiPlayer {
    private Sequencer sequencer;
    private MusicTrack musicTrack1;
    private MusicTrack musicTrack2;
    private String midiFilePath;
    private String musicFilePath;
    private int delayMs;
    private MusicPlayer songPlayer;
    private float bpm;
    public static int totalNotes;
    private volatile boolean stopRequested;

    public MidiPlayer(MusicTrack musicTrack1, MusicTrack musicTrack2, String levelName, int delayMs) {
        totalNotes = 0;
        this.musicTrack1 = musicTrack1;
        this.musicTrack2 = musicTrack2;
        String levelPath = "resources/levels/" + levelName;
        midiFilePath = levelPath + ".mid";
        musicFilePath = levelPath + ".wav";
        songPlayer = new MusicPlayer(true, musicFilePath);
        this.delayMs = delayMs;
        initializeSequencer();
        stopRequested = false;  // Initialize the stop flag
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
                        if (message instanceof ShortMessage) {
                            ShortMessage shortMessage = (ShortMessage) message;
                            if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                                allNoteEvents.add(event);
                            }
                        } else if (message instanceof MetaMessage) {
                            MetaMessage metaMessage = (MetaMessage) message;
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
                    e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    private void processMidiEvents(List<MidiEvent> allNoteEvents) {
        Map<Integer, List<MidiEvent>> pitchToEventsMap = new HashMap<>();

        // Group events by pitch
        for (MidiEvent event : allNoteEvents) {
            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage) {
                ShortMessage shortMessage = (ShortMessage) message;
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

            // Log tick values and differences for debugging
            System.out.println("Current Tick: " + currentTick + ", Last Tick: " + lastTick + ", Tick Difference: " + tickDifference);

            // Calculate the delay in milliseconds based on the tempo
            long delayInMs = tickToMs(tickDifference, bpm);

            if (delayInMs < 0) {
                System.err.println("Warning: Negative delay detected. Setting delay to 0.");
                delayInMs = 0;  // Ensure that delayInMs is non-negative
            }

            try {
                Thread.sleep(delayInMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage) {
                ShortMessage shortMessage = (ShortMessage) message;
                if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                    musicTrack.addNoteToTrack();
                }
            }

            lastTick = currentTick;
        }
    }

    private long tickToMs(long ticks, float tempoInBPM) {
        float ticksPerBeat = sequencer.getSequence().getResolution();
        float msPerBeat = 60000 / tempoInBPM;
        float msPerTick = msPerBeat / ticksPerBeat;
        return (long) (ticks * msPerTick);
    }

    private float getBpmFromMetaMessage(MetaMessage metaMessage) {
        byte[] data = metaMessage.getData();
        int tempo = ((data[0] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[2] & 0xff);
        return 60000000f / tempo;
    }

    public void stopMusic() {
        stopRequested = true;  // Signal the threads to stop
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

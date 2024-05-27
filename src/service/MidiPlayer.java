package service;

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

    public MidiPlayer(MusicTrack musicTrack1, MusicTrack musicTrack2, String levelName, int delayMs) {
        this.musicTrack1 = musicTrack1;
        this.musicTrack2 = musicTrack2;
        String levelPath = "src/resources/levels/" + levelName;
        midiFilePath = levelPath + ".mid";
        musicFilePath = levelPath + ".wav";
        songPlayer = new MusicPlayer(true, musicFilePath);
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

                System.out.println("Total NOTE_ON events: " + allNoteEvents.size());

                if (!allNoteEvents.isEmpty()) {
                    // Calculate the pause before the first note
                    long firstNoteTick = allNoteEvents.get(0).getTick();
                    long pauseDurationMs = tickToMs(firstNoteTick, bpm);
                    System.out.println("Pause before the first note: " + pauseDurationMs + " ms");
                }
                System.out.println("added note speed delay: " + delayMs + " ms");

                // Process events and divide them into two tracks
                processMidiEvents(allNoteEvents);

                // Wait for the specified delay before playing the music file
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

        for (MidiEvent event : events) {
            long currentTick = event.getTick();
            long tickDifference = currentTick - lastTick;

            // Calculate the delay in milliseconds based on the tempo
            long delayInMs = tickToMs(tickDifference, bpm);

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
        sequencer.stop();
    }
}

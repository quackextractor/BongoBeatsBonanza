package service;

import javax.sound.midi.MidiSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * The {@code AudioVerification} class provides methods to verify the validity of audio files.
 * It checks whether a WAV or MIDI file is valid by performing various integrity checks.
 */
public class AudioVerification {

    /**
     * Checks the validity of a WAV file.
     *
     * @param levelName the name of the level associated with the WAV file
     * @return {@code true} if the WAV file is valid, {@code false} otherwise
     */
    public static boolean isWavValid(String levelName) {
        try {
            File wavFile = new File("resources/levels/" + levelName + ".wav");
            if (!wavFile.exists()) {
                System.err.println("WAV file does not exist: " + wavFile.getAbsolutePath());
                return false;
            }
            if (!wavFile.canRead()) {
                System.err.println("Cannot read WAV file: " + wavFile.getAbsolutePath());
                return false;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
            AudioFormat format = audioInputStream.getFormat();
            if (format == null) {
                System.err.println("Unsupported audio format.");
                return false;
            }
            long fileLength = wavFile.length();
            if (fileLength <= 0) {
                System.err.println("Invalid file length: " + fileLength);
                return false;
            }
            return true;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading WAV file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error occurred while checking WAV file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks the validity of a MIDI file.
     *
     * @param levelName the name of the level associated with the MIDI file
     * @return {@code true} if the MIDI file is valid, {@code false} otherwise
     */
    public static boolean isMidiValid(String levelName) {
        try {
            File midiFile = new File("resources/levels/" + levelName + ".mid");
            MidiSystem.getSequence(midiFile);
            return true;
        } catch (Exception e) {
            ErrorLogger.logStackTrace(e);
            return false;
        }
    }
}

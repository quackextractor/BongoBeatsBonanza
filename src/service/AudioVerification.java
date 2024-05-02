package service;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class AudioVerification {
    public static boolean isWavValid(String levelName) {
        try {
            File wavFile = new File("src/resources/levels/" + levelName + ".wav");

            // Check if the file exists
            if (!wavFile.exists()) {
                System.err.println("WAV file does not exist: " + wavFile.getAbsolutePath());
                return false;
            }

            // Check if the file is readable
            if (!wavFile.canRead()) {
                System.err.println("Cannot read WAV file: " + wavFile.getAbsolutePath());
                return false;
            }

            // Get AudioInputStream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);

            // Check if AudioFormat is supported
            AudioFormat format = audioInputStream.getFormat();
            if (format == null) {
                System.err.println("Unsupported audio format.");
                return false;
            }

            // Check if the file length is valid (greater than 0)
            long fileLength = wavFile.length();
            if (fileLength <= 0) {
                System.err.println("Invalid file length: " + fileLength);
                return false;
            }

            // Additional integrity checks can be performed here

            // If all checks pass, WAV file is considered valid
            return true;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading WAV file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error occurred while checking WAV file: " + e.getMessage());
        }

        // If any exception occurs, or if any integrity check fails, WAV file is considered invalid
        return false;
    }

    public static boolean isMidiValid(String levelName) {
        try {
            File midiFile = new File("src/resources/levels/" + levelName + ".mid");
            Sequence sequence = MidiSystem.getSequence(midiFile);
            // If sequence is successfully obtained, midi file is valid
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Midi file is invalid
            return false;
        }
    }
}

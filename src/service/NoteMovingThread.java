package service;

/**
 * The {@code NoteMovingThread} class represents a thread responsible for moving notes on a music track.
 * It continuously moves notes by a specified amount at a specified interval.
 */
public class NoteMovingThread extends Thread {
    private final MusicTrack musicTrack;
    private boolean running;
    private final int moveAmount;
    private final long moveInterval;

    /**
     * Constructs a {@code NoteMovingThread} with the specified parameters.
     *
     * @param musicTrack   the music track containing notes to be moved
     * @param moveAmount   the amount to move notes by in each iteration
     * @param moveInterval the interval between each move in milliseconds
     */
    public NoteMovingThread(MusicTrack musicTrack, int moveAmount, long moveInterval) {
        this.musicTrack = musicTrack;
        this.moveAmount = moveAmount;
        this.moveInterval = moveInterval;
        this.running = true;
    }

    /**
     * Runs the thread, continuously moving notes on the music track.
     */
    @Override
    public void run() {
        while (running) {
            musicTrack.moveNotes(moveAmount);
            try {
                Thread.sleep(moveInterval);
            } catch (InterruptedException e) {
                ErrorLogger.logStackTrace(e);
            }
        }
    }

    /**
     * Stops the note moving thread.
     */
    public void stopMoving() {
        running = false;
    }
}
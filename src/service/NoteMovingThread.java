package service;

public class NoteMovingThread extends Thread {
    private final MusicTrack musicTrack;
    private boolean running;
    private final int moveAmount; // Amount to move notes by in each iteration
    private final long moveInterval; // Interval between each move in milliseconds

    public NoteMovingThread(MusicTrack musicTrack, int moveAmount, long moveInterval) {
        this.musicTrack = musicTrack;
        this.moveAmount = moveAmount;
        this.moveInterval = moveInterval;
        this.running = true;
    }

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

    public void stopMoving() {
        running = false;
    }
}

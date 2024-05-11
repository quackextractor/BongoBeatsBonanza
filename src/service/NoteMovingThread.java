package service;

public class NoteMovingThread extends Thread {
    private final Track track;
    private boolean running;
    private final int moveAmount; // Amount to move notes by in each iteration
    private final long moveInterval; // Interval between each move in milliseconds

    public NoteMovingThread(Track track, int moveAmount, long moveInterval) {
        this.track = track;
        this.moveAmount = moveAmount;
        this.moveInterval = moveInterval;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            track.moveNotes(moveAmount);
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

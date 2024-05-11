package service;

public class AccuracyCalculator {

    private static int numberOfHits;
    private static double totalAccuracy;
    private static int totalNotes;

    // Compares distance with the max distance before miss to determine accuracy
    public static double determineAccuracy(int distance, int maxDistance) {
        double accuracyPercentage = 100.0 * (1.0 - (double) distance / maxDistance);

        if (distance == 0) {
            Score.PerfectHit();
        } else if (accuracyPercentage >= 50) {
            Score.GoodHit();
        } else {
            Score.BadHit();
        }

        numberOfHits++;
        totalAccuracy += accuracyPercentage;
        totalNotes++;

        return accuracyPercentage;
    }

    // Method to handle a miss
    public static void miss() {
        Score.Miss();
        totalNotes++;
    }

    // Method to get the average accuracy
    public static double getAverageAccuracy() {
        if (numberOfHits == 0) {
            return 0.0; // Avoid division by zero
        }
        return totalAccuracy / totalNotes;
    }

    // Method to reset accuracy-related variables
    public static void reset() {
        numberOfHits = 0;
        totalAccuracy = 0.0;
        totalNotes = 0;
    }
}

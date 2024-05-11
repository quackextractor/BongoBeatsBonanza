package service;

public class AccuracyCalculator {
    // Compares distance with the max distance before miss to determine Score
    public static double determineAccuracy(int distance, int maxDistance) {
        double accuracyPercentage = 100.0 * (1.0 - (double) distance / maxDistance);

        if (distance == 0) {
            Score.PerfectHit();
        } else if (accuracyPercentage >= 50) {
            Score.GoodHit();
        } else {
            Score.BadHit();
        }

        return accuracyPercentage;
    }
}

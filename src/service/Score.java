package service;

// TODO add health
public class Score {
    private static int totalScore = 0;
    private static int streakCount = 0;
    private static int multiplier = 1;
    private static int numberOfHits = 0;
    private static double totalAccuracy = 0.0;
    private static int totalNotes = 0;

    public static void setTotalScore(int totalScore) {
        Score.totalScore = totalScore;
    }

    public static void setStreakCount(int streakCount) {
        Score.streakCount = streakCount;
    }

    public static void increaseStreak() {
        Score.streakCount++;
    }

    public static void perfectHit() {
        totalScore += 100 * multiplier;
        streakCount++;
        updateMultiplier();
        System.out.println("Perfect hit! Score: " + totalScore);
    }

    public static void goodHit() {
        totalScore += 50 * multiplier;
        streakCount++;
        updateMultiplier();
        System.out.println("Good hit! Score: " + totalScore);
    }

    public static void badHit() {
        totalScore += 25 * multiplier;
        streakCount = 0;
        updateMultiplier();
        System.out.println("Bad hit! Score: " + totalScore);
    }

    public static void miss() {
        streakCount = 0;
        updateMultiplier();
        totalScore -= 100;
        totalNotes++;
        System.out.println("Missed hit! Score: " + totalScore);
    }

    // Compares distance with the max distance before miss to determine accuracy
    public static void determineAccuracy(int distance, int maxDistance) {
        MusicPlayer fxPlayer = new MusicPlayer(false, "src/resources/sounds/tamboHit.wav");
        double accuracyPercentage = 100.0 * (1.0 - (double) distance / maxDistance);
        if (distance == 0) {
            perfectHit();
        } else if (accuracyPercentage >= 50) {
            goodHit();
            fxPlayer.playDefault();
        } else {
            badHit();
            fxPlayer.playDefault();
        }

        numberOfHits++;
        totalAccuracy += accuracyPercentage;
        totalNotes++;
    }

    public static double getAverageAccuracy() {
        if (numberOfHits == 0) {
            return 0.0; // Avoid division by zero
        }
        return totalAccuracy / totalNotes;
    }

    public static void updateMultiplier() {
        multiplier = Math.min(1 + streakCount / 4, 4); // Maximum multiplier is 4x
    }

    public static int getTotalScore() {
        return totalScore;
    }

    public static int getStreakCount() {
        return streakCount;
    }

    public static int getMultiplier() {
        return multiplier;
    }

    public static void reset() {
        totalScore = 0;
        streakCount = 0;
        multiplier = 1;
        numberOfHits = 0;
        totalAccuracy = 0.0;
        totalNotes = 0;
    }
}

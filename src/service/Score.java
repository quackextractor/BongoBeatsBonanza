package service;

public class Score {
    private static int totalScore = 0;
    private static int streakCount = 0;
    private static int numberOfHits = 0;
    private static double totalAccuracy = 0.0;
    private static int totalNotes = 0;

    public enum HitType {
        PERFECT(100),
        GOOD(50),
        BAD(25),
        MISS(-100);

        private final int scoreValue;

        HitType(int scoreValue) {
            this.scoreValue = scoreValue;
        }

        public int getScoreValue() {
            return scoreValue;
        }
    }

    public static void setTotalScore(int totalScore) {
        Score.totalScore = totalScore;
    }

    public static void setStreakCount(int streakCount) {
        Score.streakCount = streakCount;
    }

    public static void increaseStreak() {
        Score.streakCount++;
    }

    public static void registerHit(HitType hitType) {
        if (hitType == HitType.MISS) {
            streakCount = 0;
        } else {
            streakCount++;
        }

        totalScore += hitType.getScoreValue();

        if (hitType != HitType.MISS) {
            numberOfHits++;
        }

        totalNotes++;
        System.out.println(hitType + " hit! Score: " + totalScore);
    }

    // Compares distance with the max distance before miss to determine accuracy
    public static void determineAccuracy(int distance, int maxDistance) {
        MusicPlayer fxPlayer = new MusicPlayer(false, "src/resources/sounds/tamboHit.wav");
        double accuracyPercentage = 100.0 * (1.0 - (double) distance / maxDistance);

        if (distance == 0) {
            registerHit(HitType.PERFECT);
        } else if (accuracyPercentage >= 50) {
            registerHit(HitType.GOOD);
            fxPlayer.playDefault();
        } else {
            registerHit(HitType.BAD);
            fxPlayer.playDefault();
        }

        totalAccuracy += accuracyPercentage;
    }

    public static double getAverageAccuracy() {
        if (totalNotes == 0) {
            return 0.0; // Avoid division by zero
        }
        return totalAccuracy / totalNotes;
    }

    public static int getTotalScore() {
        return totalScore;
    }

    public static int getStreakCount() {
        return streakCount;
    }

    public static void reset() {
        totalScore = 0;
        streakCount = 0;
        numberOfHits = 0;
        totalAccuracy = 0.0;
        totalNotes = 0;
    }

    public static void miss() {
        registerHit(HitType.MISS);
    }
}
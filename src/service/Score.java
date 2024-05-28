package service;

import view.GameJPanel;

public class Score {
    private static int totalScore = 0;
    private static int streakCount = 0;
    private static double totalAccuracy = 0.0;
    private static int highestStreak = 0;
    private static int totalNotes = 0;
    private static int health = 100;
    private static double difficultyModifier = 1;

    // Counters for each hit type
    private static int greatCount = 0;
    private static int goodCount = 0;
    private static int badCount = 0;
    private static int missCount = 0;

    public enum HitType {
        GREAT(80),
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

    public static boolean isComplete(){
        return MidiPlayer.totalNotes == totalNotes;
    }

    public static void increaseStreak() {
        streakCount++;
        if (streakCount > highestStreak) {
            highestStreak = streakCount;
        }
    }

    public static int getHighestStreak() {
        return highestStreak;
    }

    public static void registerHit(HitType hitType) {
        if (hitType == HitType.MISS || hitType==HitType.BAD) {
            streakCount = 0;
        } else {
            increaseStreak();
        }

        totalScore += hitType.getScoreValue();
        totalNotes++;
        System.out.println(hitType + " hit! Score: " + totalScore);
    }

    // Compares distance with the max distance before miss to determine accuracy
    public static String determineAccuracy(int distance, int maxDistance) {
        MusicPlayer fxPlayer = new MusicPlayer(false, "resources/sounds/tamboHit.wav");
        double accuracyPercentage = 100.0 * (1.0 - (double) distance / maxDistance);
        totalAccuracy += accuracyPercentage;

        if (distance <= 5) {
            registerHit(HitType.GREAT);
            greatCount++;
            fxPlayer.playDefault();
            changeHealth(20);
            return "GREAT";
        } else if (accuracyPercentage >= 50) {
            registerHit(HitType.GOOD);
            goodCount++;
            fxPlayer.playDefault();
            changeHealth(10);
            return "GOOD";
        } else {
            registerHit(HitType.BAD);
            badCount++;
            fxPlayer.playDefault();
            changeHealth(-5);
            return "BAD";
        }
    }

    public static void miss() {
        MusicPlayer musicPlayer = new MusicPlayer(false, "resources/sounds/leave.wav");
        musicPlayer.playDefault();
        registerHit(HitType.MISS);
        changeHealth(-10);
        missCount++;
    }

    public static double getAverageAccuracy() {
        // Avoid division by zero
        if (totalNotes == 0) {
            return 0.0;
        }
        return totalAccuracy / totalNotes;
    }

    public static int getTotalScore() {
        return totalScore;
    }

    public static int getStreakCount() {
        return streakCount;
    }

    public static int getHealth() {
        return health;
    }

    public static void setDifficultyModifier(double difficultyModifier) {
        Score.difficultyModifier = difficultyModifier;
    }

    /**
     * Adjusts the health by the specified amount, ensuring it stays within 0 and 100.
     *
     * @param amount The amount by which to change the health. Positive values increase health,
     *               negative values decrease health.
     */
    public static void changeHealth(int amount) {
        if (amount < 0){
            amount *= difficultyModifier;
        }
        health += amount;

        // Ensure health does not exceed 100
        if (health > 100) {
            health = 100;
        }

        // Ensure health does not fall below 0
        if (health < 0) {
            health = 0;
        }
    }

    public static int getGreatCount() {
        return greatCount;
    }

    public static int getGoodCount() {
        return goodCount;
    }

    public static int getBadCount() {
        return badCount;
    }

    public static int getMissCount() {
        return missCount;
    }

    public static void reset() {
        highestStreak = 0;
        missCount = 0;
        badCount = 0;
        goodCount = 0;
        greatCount = 0;
        totalScore = 0;
        streakCount = 0;
        totalAccuracy = 0.0;
        totalNotes = 0;
        health = 100;
    }
}
package service;

/**
 * The {@code Score} class manages the scoring and health mechanics of the game.
 * It tracks the player's score, streak count, accuracy, health, and hit counts.
 */
public class Score {
    private static int totalScore = 0;
    private static int streakCount = 0;
    private static double totalAccuracy = 0.0;
    private static int highestStreak = 0;
    private static int totalNotes = 0;
    private static int health = 100;
    private static double difficultyModifier = 1;

    private static int greatCount = 0;
    private static int goodCount = 0;
    private static int badCount = 0;
    private static int missCount = 0;

    /**
     * Enumerates the types of hits and their corresponding score values.
     */
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

    /**
     * Increases the streak count.
     */
    public static void increaseStreak() {
        streakCount++;
        if (streakCount > highestStreak) {
            highestStreak = streakCount;
        }
    }

    /**
     * Registers a hit and updates the score and streak count accordingly.
     *
     * @param hitType the type of hit
     */
    public static void registerHit(HitType hitType) {
        if (hitType == HitType.MISS || hitType == HitType.BAD) {
            streakCount = 0;
        } else {
            increaseStreak();
        }

        totalScore += hitType.getScoreValue();
        totalNotes++;
    }

    /**
     * Retrieves the highest streak achieved.
     *
     * @return the highest streak achieved
     */
    public static int getHighestStreak() {
        return highestStreak;
    }


    /**
     * Determines the accuracy of the hit based on the distance from the target.
     *
     * @param distance    the distance from the target
     * @param maxDistance the maximum distance allowed before a miss
     * @return the accuracy of the hit
     */
    public static String determineAccuracy(int distance, int maxDistance) {
        if (distance > maxDistance) {
            miss();
            return "MISS";
        }
        MusicPlayer fxPlayer = new MusicPlayer(false, "resources/sounds/tamboHit.wav");
        double accuracyPercentage = 100.0 * (1.0 - (double) distance / maxDistance);
        totalAccuracy += accuracyPercentage;

        if (distance <= 5) {
            registerHit(HitType.GREAT);
            fxPlayer.playDefault();
            changeHealth(20);
            greatCount++;
            return "GREAT";
        } else if (accuracyPercentage >= 50) {
            registerHit(HitType.GOOD);
            fxPlayer.playDefault();
            changeHealth(10);
            goodCount++;
            return "GOOD";
        } else {
            registerHit(HitType.BAD);
            fxPlayer.playDefault();
            changeHealth(-5);
            badCount++;
            return "BAD";
        }
    }

    /**
     * Registers a miss and updates the score and health.
     */
    public static void miss() {
        MusicPlayer musicPlayer = new MusicPlayer(false, "resources/sounds/leave.wav");
        musicPlayer.playDefault();
        registerHit(HitType.MISS);
        changeHealth(-10);
        missCount++;
    }

    /**
     * Retrieves the average accuracy.
     *
     * @return the average accuracy
     */
    public static double getAverageAccuracy() {
        if (totalNotes == 0) {
            return 0.0;
        }
        return totalAccuracy / totalNotes;
    }

    /**
     * Retrieves the total score.
     *
     * @return the total score
     */
    public static int getTotalScore() {
        return totalScore;
    }

    /**
     * Retrieves the streak count.
     *
     * @return the streak count
     */
    public static int getStreakCount() {
        return streakCount;
    }

    /**
     * Retrieves the health.
     *
     * @return the health
     */
    public static int getHealth() {
        return health;
    }

    /**
     * Sets the difficulty modifier.
     *
     * @param difficultyModifier the difficulty modifier
     */
    public static void setDifficultyModifier(double difficultyModifier) {
        Score.difficultyModifier = difficultyModifier;
    }

    /**
     * Adjusts the health by the specified amount, ensuring it stays within 0 and 100.
     *
     * @param amount The amount by which to change the health. Positive values increase health,
     *               negative values decrease health.
     */
    public static void changeHealth(double amount) {
        if (amount < 0) {
            amount *= difficultyModifier;
        }
        health += (int) amount;

        // Ensure health does not exceed 100
        if (health > 100) {
            health = 100;
        }

        // Ensure health does not fall below 0
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Retrieves the count of great hits.
     *
     * @return the count of great hits
     */
    public static int getGreatCount() {
        return greatCount;
    }

    /**
     * Retrieves the count of good hits.
     *
     * @return the count of good hits
     */
    public static int getGoodCount() {
        return goodCount;
    }

    /**
     * Retrieves the count of bad hits.
     *
     * @return the count of bad hits
     */
    public static int getBadCount() {
        return badCount;
    }

    /**
     * Retrieves the count of misses.
     *
     * @return the count of misses
     */
    public static int getMissCount() {
        return missCount;
    }

    /**
     * Resets all scoring and health parameters.
     */
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
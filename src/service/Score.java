package service;

import java.util.concurrent.atomic.AtomicInteger;

public class Score {
    private static int totalScore = 0;
    private static int streakCount = 0;
    private static int multiplier = 1;

    public static void PerfectHit() {
        totalScore += 100 * multiplier;
        streakCount++;
        updateMultiplier();
        System.out.println("Perfect hit! Score: " + totalScore);
    }

    public static void GoodHit() {
        totalScore += 50 * multiplier;
        streakCount++;
        updateMultiplier();
        System.out.println("Good hit! Score: " + totalScore);
    }

    public static void BadHit() {
        totalScore += 25 * multiplier;
        streakCount = 0;
        updateMultiplier();
        System.out.println("Bad hit! Score: " + totalScore);
    }

    public static void Miss() {
        streakCount = 0;
        updateMultiplier();
        System.out.println("Missed hit! Score: " + totalScore);
    }

    private static void updateMultiplier() {
        multiplier = Math.min(1 + streakCount / 4, 4); // Maximum multiplier is 4x
    }

    public static int getTotalScore() {
        return totalScore;
    }

    public static int getMultiplier() {
        return multiplier;
    }
}

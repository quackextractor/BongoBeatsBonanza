package service;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class AudioVerificationTest {

   private final String badLevelName = "test";
   private final String goodLevelName = "Memory Merge";

    @org.junit.jupiter.api.Test
    void isWavValid() {

        assertTrue(AudioVerification.isMidiValid(goodLevelName));

        try {
        assertFalse(AudioVerification.isWavValid(badLevelName));
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @org.junit.jupiter.api.Test
    void isMidiValid() {

        assertTrue(AudioVerification.isMidiValid(goodLevelName));

        try {
            assertFalse(AudioVerification.isWavValid(badLevelName));
        } catch (Exception e){
            Assertions.fail();
        }
    }
}
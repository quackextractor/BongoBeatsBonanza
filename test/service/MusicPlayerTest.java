package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MusicPlayerTest {

    private void waitALittle(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void playClipValidFilePlaybackSuccessful() {
        MusicPlayer musicPlayer = new MusicPlayer(true, "");
        String filePath = "resources/music/menu.wav";

        musicPlayer.play(filePath);
        waitALittle();

        assertTrue(musicPlayer.getMicroSecondPos() > 0);
        assertTrue(musicPlayer.getMicroSecondLength() > 0);
    }

    @Test
    void playClipIsPlaying() {
        String filePath = "invalid/path.wav";
        MusicPlayer musicPlayer = new MusicPlayer(true, filePath);
        musicPlayer.playDefault();

        assertFalse(musicPlayer.isPlaying());
    }

    @Test
    void stopPlaybackStopped() {
        MusicPlayer musicPlayer = new MusicPlayer(true, "");
        String filePath = "resources/music/menu.wav";
        musicPlayer.play(filePath);

        musicPlayer.stop();

        assertFalse(musicPlayer.getMicroSecondPos() > 0);
    }

    @Test
    void pausePlaybackPaused() {
        MusicPlayer musicPlayer = new MusicPlayer(true, "");
        String filePath = "resources/music/menu.wav";
        musicPlayer.play(filePath);

        waitALittle();

        musicPlayer.pause();
        long positionBeforePause = musicPlayer.getMicroSecondPos();

        assertTrue(positionBeforePause > 0);

        musicPlayer.resume();

        waitALittle();

        long positionAfterResume = musicPlayer.getMicroSecondPos();

        assertTrue(positionAfterResume > positionBeforePause);
    }

    @Test
    void setMusicVolumeValidVolumeVolumeSet() {
        float volume = -5.0f;

        MusicPlayer.setMusicVolume(volume);

        assertEquals(volume, MusicPlayer.getMusicVolume());
    }

    @Test
    void setFxVolumeValidVolumeVolumeSet() {
        float volume = -5.0f;

        MusicPlayer.setFxVolume(volume);

        assertEquals(volume, MusicPlayer.getFxVolume());
    }
}

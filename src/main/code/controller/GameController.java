package main.code.controller;

import main.code.model.Song;
import main.code.service.MusicPlayer;

public class GameController {
    private Song song;
    private MusicPlayer musicPlayer;

    public GameController(String songPath) {
        try {
            this.song = new Song(songPath);
            this.musicPlayer = new MusicPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame(String backgroundMusicPath) {
        try {
            // Start playing background music
            musicPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endGame() {
        // Stop playing background music
        musicPlayer.stop();

        // Add future cleanup logic here
    }
}

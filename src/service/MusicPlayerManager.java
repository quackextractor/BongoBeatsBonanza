package service;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerManager {
    private static final List<MusicPlayer> musicPlayers = new ArrayList<>();

    // Method for adding a MusicPlayer to the manager
    public static void addMusicPlayer(MusicPlayer musicPlayer) {
        musicPlayers.add(musicPlayer);
    }

    // Method for stopping all MusicPlayers
    public static void stopAllMusicPlayers() {
        for (MusicPlayer player : musicPlayers) {
            player.stop();
        }
    }

    // Method for removing a MusicPlayer from the manager
    public static void removeMusicPlayer(MusicPlayer musicPlayer) {
        musicPlayers.remove(musicPlayer);
    }
}

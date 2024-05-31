package service;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code MusicPlayerManager} class manages a list of {@link MusicPlayer} instances.
 * It provides methods to add, remove, and stop all music players.
 */
public class MusicPlayerManager {
    private static final List<MusicPlayer> musicPlayers = new ArrayList<>();

    /**
     * Adds a {@link MusicPlayer} to the manager.
     *
     * @param musicPlayer the music player to add
     */
    public static void addMusicPlayer(MusicPlayer musicPlayer) {
        musicPlayers.add(musicPlayer);
    }

    /**
     * Stops all music players managed by the manager.
     */
    public static void stopAllMusicPlayers() {
        for (MusicPlayer player : musicPlayers) {
            player.stop();
        }
    }

    /**
     * Removes a {@link MusicPlayer} from the manager.
     *
     * @param musicPlayer the music player to remove
     */
    public static void removeMusicPlayer(MusicPlayer musicPlayer) {
        musicPlayers.remove(musicPlayer);
    }
}

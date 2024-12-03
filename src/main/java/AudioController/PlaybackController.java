package AudioController;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaybackController {

    private static PlaybackController instance;
    private MediaPlayer mediaPlayer;
    private List<String> songQueue = new ArrayList<>();  // List to hold the queue of songs
    private int currentSongIndex = 0;  // Pointer to the current song
    private boolean isPlaying = false;
    private boolean isLooped = false;  // Flag for looped playback
    private boolean isShuffled = false;  // Flag for shuffled playback
    private double volume = 0.5; // Default volume (50%)

    private PlaybackController() {}

    public static PlaybackController getInstance() {
        if (instance == null) {
            instance = new PlaybackController();
        }
        return instance;
    }

    public void playAudio(String filePath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);

        // Set the volume when a new song is played
        mediaPlayer.setVolume(volume);
        mediaPlayer.setOnEndOfMedia(() -> {
            isPlaying = false;
            playNext();  // Automatically play the next song when current song ends
        });

        mediaPlayer.play();
        isPlaying = true;
    }

    public void addToQueue(String filePath) {
        songQueue.add(filePath);  // Add a song to the queue
    }

    public void togglePlayPause() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
            isPlaying = !isPlaying;
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public double getVolume() {
        return volume;
    }

    public boolean isLooped() {
        return isLooped;
    }

    public void setLooped(boolean isLooped) {
        this.isLooped = isLooped;
        if (isLooped) {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(javafx.util.Duration.ZERO);  // Loop back to the start
                mediaPlayer.play();
            });
        } else {
            mediaPlayer.setOnEndOfMedia(() -> playNext());  // Continue to next song
        }
    }

    public boolean isShuffled() {
        return isShuffled;
    }

    public void setShuffled(boolean isShuffled) {
        this.isShuffled = isShuffled;
        if (isShuffled) {
            Collections.shuffle(songQueue);  // Shuffle the song queue
        } else {
            // Optionally reset the queue to the original order (if needed)
        }
    }

    public void playNext() {
        if (!songQueue.isEmpty()) {
            if (isShuffled) {
                Collections.shuffle(songQueue);  // Shuffle the queue when playing next
            }
            currentSongIndex = (currentSongIndex + 1) % songQueue.size();  // Move to the next song
            playAudio(songQueue.get(currentSongIndex));  // Play the next song in the queue
        }
    }

    public void playPrevious() {
        if (!songQueue.isEmpty()) {
            currentSongIndex = (currentSongIndex - 1 + songQueue.size()) % songQueue.size();  // Move to the previous song
            playAudio(songQueue.get(currentSongIndex));  // Play the previous song in the queue
        }
    }

    public List<String> getSongQueue() {
        return songQueue;
    }

    public void removeFromQueue(String filePath) {
        songQueue.remove(filePath);  // Remove a specific song from the queue
        if (currentSongIndex >= songQueue.size()) {
            currentSongIndex = songQueue.size() - 1;  // Adjust index if needed
        }
    }

    public void clearQueue() {
        songQueue.clear();  // Clear the entire song queue
        currentSongIndex = 0;  // Reset the current song index
        if (mediaPlayer != null) {
            mediaPlayer.stop();  // Stop the current song if there is one
        }
        isPlaying = false;  // Set playing state to false
    }
}

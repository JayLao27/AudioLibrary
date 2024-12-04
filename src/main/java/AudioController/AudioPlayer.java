package AudioController;

import AudioController.controllers.HomeScene;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioPlayer implements SceneWithHomeContext {

    private HomeScene homeScene;
    private static AudioPlayer instance;
    private static MediaPlayer mediaPlayer;
    private List<Integer> audioQueue = new ArrayList<>();  // Queue for audio IDs
    private int currentIndex = 0;  // Current index in the queue
    private BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private boolean isLooped = false;
    private boolean isShuffled = false;
    private double volume = 0.5; // Default volume (50%)

    private AudioPlayer() {}

    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

    public void setQueue(List<Integer> queue) {
        audioQueue = queue;
        currentIndex = 0; // Reset to start of the queue
    }

    public void playAudio(int audioID) {
        currentIndex = audioQueue.indexOf(audioID);  // Set the correct index based on audioID
        System.out.println("Trying to play audioID: " + audioID + " at index: " + currentIndex);

        if (currentIndex != -1) {  // If the audioID is found in the queue
            String audioFileName = fetchAudioFileName(audioID);
            if (audioFileName != null) {
                String filePath = "/audioFiles/" + audioFileName;
                URL resource = AudioPlayer.class.getResource(filePath);

                if (resource != null) {
                    playMedia(resource.toString());  // Play the media

                    // After starting the media, update the UI
                    if (homeScene != null) {
                        Platform.runLater(() -> homeScene.loadCurrentSong(currentIndex));  // Ensure currentIndex is passed
                    }
                } else {
                    System.out.println("Audio file not found in resources: " + filePath);
                }
            } else {
                System.out.println("No audio found for audioID: " + audioID);
            }
        } else {
            System.out.println("AudioID not found in the queue: " + audioID);
        }
    }

    private String fetchAudioFileName(int audioID) {
        String query = "SELECT audioFileName FROM Audio WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, audioID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("audioFileName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

        private void playMedia(String filePath) {
        // Stop the current player if one exists
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        // Create a new Media object
        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);

        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
            mediaPlayer.setOnEndOfMedia(() -> {
                setPlaying(false);
                if (isLooped) {
                    mediaPlayer.seek(javafx.util.Duration.ZERO);
                    mediaPlayer.play();
                } else {
                    playNext();
                }
            });

            mediaPlayer.setOnReady(() -> {
                System.out.println("Media is ready with duration: " + mediaPlayer.getTotalDuration());
                if (homeScene != null) {
                    Platform.runLater(() -> homeScene.loadCurrentSong(currentIndex));
                }
            });

            mediaPlayer.play();
            setPlaying(true);
        } else {
            System.out.println("Error initializing MediaPlayer for file: " + filePath);
        }
    }

    public void playNext() {
        if (!audioQueue.isEmpty()) {
            if (isShuffled) {
                Collections.shuffle(audioQueue);
            }
            currentIndex = (currentIndex + 1) % audioQueue.size();
            playAudio(audioQueue.get(currentIndex));
        }
    }

    public void playPrevious() {
        if (!audioQueue.isEmpty()) {
            currentIndex = (currentIndex - 1 + audioQueue.size()) % audioQueue.size();
            playAudio(audioQueue.get(currentIndex));
        }
    }

    public void togglePlayPause() {
        if (mediaPlayer != null) {
            if (isPlaying()) {
                mediaPlayer.pause();
                setPlaying(false);
            } else {
                mediaPlayer.play();
                setPlaying(true);
            }
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public void setLooped(boolean isLooped) {
        this.isLooped = isLooped;
    }

    public void setShuffled(boolean isShuffled) {
        this.isShuffled = isShuffled;
        if (isShuffled) {
            Collections.shuffle(audioQueue);
        }
    }

    public void addToQueue(int audioID) {
        audioQueue.add(audioID);
    }

    public void removeFromQueue(int audioID) {
        audioQueue.remove((Integer) audioID);
        if (currentIndex >= audioQueue.size()) {
            currentIndex = Math.max(0, audioQueue.size() - 1);
        }
    }

    public void clearQueue() {
        audioQueue.clear();
        currentIndex = 0;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        setPlaying(false);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public List<Integer> getAudioQueue() {
        return new ArrayList<>(audioQueue);
    }

    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying.get();
    }

    public void setPlaying(boolean playing) {
        isPlaying.set(playing);
    }
}

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


/**
 * Manages the audio playback, including controlling playback, volume, shuffle, loop,
 * and managing the audio queue. This class provides methods to play, pause,
 * skip, and shuffle tracks, as well as retrieve and update the audio queue.
 *
 * The {@link AudioPlayer} class interacts with a {@link HomeScene} to update the user interface
 * and fetches audio file details from a database. It is a singleton class to ensure only one
 * instance of the player exists during the application's lifecycle.
 */
public class AudioPlayer implements SceneWithHomeContext {

    private HomeScene homeScene;
    private static AudioPlayer instance;
    private static MediaPlayer mediaPlayer;
    private List<Integer> audioQueue = new ArrayList<>();
    private List<Integer> originalQueue = new ArrayList<>();
    private int currentIndex = 0;
    private BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private boolean isLooped = false;
    private boolean isShuffled = false;
    private double volume = 0.5; // Default volume (50%)

    private AudioPlayer() {}


    //Playback Controllers
    /**
     * Starts playing the audio with the given audio ID. If the audio is found in the queue,
     * it fetches the audio file and plays it.
     * <p>
     * The method ensures that the correct audio file is loaded, and the current song index
     * is updated in the user interface via the {@link HomeScene}.
     * </p>
     *
     * @param audioID the ID of the audio to play.
     */
    public void playAudio(int audioID) {
        currentIndex = audioQueue.indexOf(audioID);
        System.out.println("Trying to play audioID: " + audioID + " at index: " + currentIndex);

        if (currentIndex != -1) { // If the audioID is found in the queue
            String audioFileName = ResourceLoader.getAudioFileName(audioID);
            if (audioFileName != null) {
                String filePath = "/audioFiles/" + audioFileName;
                URL resource = AudioPlayer.class.getResource(filePath);

                if (resource != null) {
                    playMedia(resource.toString());

                    if (homeScene != null) {
                        Platform.runLater(() -> homeScene.loadCurrentSong(currentIndex));
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

    /**
     * Initializes and starts playing the media from the specified file path.
     * Stops the current media player (if any) and creates a new one.
     * <p>
     * Also handles setting the volume and behavior when the media finishes playing.
     * </p>
     *
     * @param filePath the path to the audio file.
     */
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

    /**
     * Plays the next audio in the queue, either sequentially or shuffled, depending on the settings.
     * If looping is enabled, it will continue playing the queue cyclically.
     */
    public void playNext() {
        if (!audioQueue.isEmpty()) {
            if (isShuffled) {
                List<Integer> remainingQueue = new ArrayList<>(audioQueue);
                remainingQueue.remove(currentIndex);

                Collections.shuffle(remainingQueue);
                currentIndex = audioQueue.indexOf(remainingQueue.get(0));
            } else {
                if (!isLooped) {
                    if (currentIndex + 1 < audioQueue.size()) {
                        currentIndex++;
                    } else {
                        return;
                    }
                } else {
                    currentIndex = (currentIndex + 1) % audioQueue.size();
                }
            }

            playAudio(audioQueue.get(currentIndex));
        }
    }

    /**
     * Plays the previous audio in the queue, either sequentially or looped, depending on the settings.
     */
    public void playPrevious() {
        if (!audioQueue.isEmpty()) {
            if (!isLooped) {
                // If not looped, don't go beyond the first song
                if (currentIndex - 1 >= 0) {
                    currentIndex--;
                    playAudio(audioQueue.get(currentIndex));
                }
            } else {
                // If looped, cycle through the queue normally
                currentIndex = (currentIndex - 1 + audioQueue.size()) % audioQueue.size();
                playAudio(audioQueue.get(currentIndex));
            }
        }
    }

    /**
     * Toggles between play and pause states for the current audio.
     */
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


    //Queue Managers
    /**
     * Adds a song (identified by its audio ID) to the audio queue.
     *
     * @param audioID the ID of the audio to add to the queue.
     */
    public void addToQueue(int audioID) {
        audioQueue.add(audioID);
        if (!isShuffled) {
            originalQueue.add(audioID);
        }
    }
    /**
     * Removes a song (identified by its audio ID) from the audio queue.
     *
     * @param audioID the ID of the audio to remove from the queue.
     */
    public void removeFromQueue(int audioID) {
        audioQueue.remove((Integer) audioID);
        if (!isShuffled && originalQueue != null) {
            originalQueue.remove((Integer) audioID);
        }
        if (currentIndex >= audioQueue.size()) {
            currentIndex = Math.max(0, audioQueue.size() - 1);
        }
    }
    /**
     * Clears the audio queue and stops any currently playing audio.
     */
    public void clearQueue() {
        audioQueue.clear();
        originalQueue.clear();
        currentIndex = 0;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        setPlaying(false);
    }


    //Getters
    /**
     * Returns the singleton instance of the AudioPlayer.
     *
     * @return the AudioPlayer instance.
     */
    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

    /**
     * Returns the current {@link MediaPlayer} instance used for audio playback.
     *
     * This method provides access to the {@link MediaPlayer} object that is responsible for playing the audio.
     * It can be used to adjust playback settings or to directly control the media player, such as pausing,
     * resuming, or stopping playback.
     *
     * @return the current {@link MediaPlayer} instance, or {@code null} if no media player is initialized.
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Returns a copy of the current audio queue.
     *
     * This method returns a new {@link List} containing the IDs of the audio tracks in the current queue.
     * The returned list is a copy, so modifications to it will not affect the original queue. This is useful
     * for cases where you want to access the queue state without altering it.
     *
     * @return a new {@link List} containing the audio IDs in the current queue.
     */
    public List<Integer> getAudioQueue() {
        return new ArrayList<>(audioQueue);
    }

    /**
     * Returns a copy of the current audio queue.
     *
     * This method returns a new {@link List} containing the IDs of the audio tracks in the current queue.
     * The returned list is a copy, so modifications to it will not affect the original queue. This is useful
     * for cases where you want to access the queue state without altering it.
     *
     * @return a new {@link List} containing the audio IDs in the current queue.
     */
    public int getCurrentAudioID() {
        if (currentIndex >= 0 && currentIndex < audioQueue.size()) {
            return audioQueue.get(currentIndex);
        }
        return -1;
    }


    //Setters
    /**
     * Sets the volume for the audio player.
     *
     * @param volume the volume level, between 0.0 (muted) and 1.0 (maximum).
     */
    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    /**
     * Sets whether the audio queue should loop.
     *
     * @param isLooped true to enable looping, false to disable.
     */
    public void setLooped(boolean isLooped) {
        this.isLooped = isLooped;
    }

    /**
     * Sets whether the audio queue should be shuffled.
     *
     * @param isShuffled true to enable shuffle, false to disable.
     */
    public void setShuffled(boolean isShuffled) {
        if (this.isShuffled == isShuffled) {
            return; // No change needed if shuffle state is already the same
        }

        int currentAudioID = getCurrentAudioID(); // Track the currently playing song
        this.isShuffled = isShuffled;

        if (isShuffled) {
            // Backup the original queue order and shuffle the audioQueue
            originalQueue = new ArrayList<>(audioQueue);
            Collections.shuffle(audioQueue);
        } else {
            // Restore the original queue order
            if (originalQueue != null) {
                audioQueue = new ArrayList<>(originalQueue);
            }
        }

        // Update currentIndex to match the current song's new position
        if (currentAudioID != -1) {
            currentIndex = audioQueue.indexOf(currentAudioID);
        }
    }

    /**
     * Sets the playing state of the audio player.
     *
     * This method updates the internal state of the player to indicate whether it is currently playing audio.
     * The state is represented by a {@link BooleanProperty} which can be bound to UI components to reflect the
     * current playback status.
     *
     * @param playing {@code true} if the player is playing audio, {@code false} if the player is paused or stopped.
     */
    public void setPlaying(boolean playing) {
        isPlaying.set(playing);
    }

    /**
     * Sets the playback queue to a new list of audio IDs.
     * Resets the current index to the beginning of the queue.
     *
     * @param queue the new audio queue.
     */
    public void setQueue(List<Integer> queue) {
        audioQueue = queue;
        currentIndex = 0; // Reset to start of the queue
    }

    /**
     * Sets the {@link HomeScene} instance to be used for UI updates.
     *
     * @param homeScene the HomeScene instance to be set.
     */
    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }


    //Boolean getters
    /**
     * Returns a BooleanProperty representing whether the audio player is playing.
     *
     * @return the BooleanProperty for the playing state.
     */
    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    /**
     * Returns whether the audio player is currently playing.
     *
     * @return true if the player is playing, false otherwise.
     */
    public boolean isPlaying() {
        return isPlaying.get();
    }

    /**
     * Returns whether the audio queue is set to loop.
     *
     * @return true if looping is enabled, false otherwise.
     */
    public boolean isLooped() {
        return isLooped;
    }

    /**
     * Returns whether the audio queue is shuffled.
     *
     * @return true if shuffle is enabled, false otherwise.
     */
    public boolean isShuffled() {
        return isShuffled;
    }
}


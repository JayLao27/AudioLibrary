package AudioController.controllers;

import AudioController.AudioPlayer;
import AudioController.ResourceLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

/**
 * Controller for the current song scene in the audio player.
 * This class manages the UI elements that display information about the current song,
 * such as the song name, artist, duration, and the playback progress.
 */
public class CurrentSongScene {
    @FXML
    ImageView songImage;
    @FXML
    Label songNameLabel, artistNameLabel, currentTimeLabel, durationLabel;
    @FXML
    ProgressBar playbackProgressBar;
    @FXML
    Slider playbackSlider;

    private List<Integer> audioQueue = AudioPlayer.getInstance().getAudioQueue();

    @FXML
    public void initialize() {
        setupSlider();
    }

    /**
     * Sets up the playback slider to allow seeking through the audio and reflects the current playback position.
     * The slider is updated based on the current time of the audio being played.
     */
    private void setupSlider() {
        playbackSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                Duration seekTo = AudioPlayer.getInstance().getMediaPlayer().getMedia().getDuration()
                        .multiply(playbackSlider.getValue() / 100.0);
                AudioPlayer.getInstance().getMediaPlayer().seek(seekTo);
            }
        });

        playbackSlider.setOnMouseEntered(event -> {
            playbackProgressBar.getStyleClass().add("bar-enter");
            playbackProgressBar.getStyleClass().remove("bar-exit");
        });

        playbackSlider.setOnMouseExited(event -> {
            playbackProgressBar.getStyleClass().add("bar-exit");
            playbackProgressBar.getStyleClass().remove("bar-enter");
        });

        playbackSlider.setOnMousePressed(event -> {
            playbackProgressBar.getStyleClass().add("bar-enter");
            playbackProgressBar.getStyleClass().remove("bar-exit");
        });

        playbackSlider.setOnMouseReleased(event -> {
            if (AudioPlayer.getInstance().isPlaying()) {
                Duration seekTo = AudioPlayer.getInstance().getMediaPlayer().getMedia().getDuration()
                        .multiply(playbackSlider.getValue() / 100.0);
                AudioPlayer.getInstance().getMediaPlayer().seek(seekTo);
                playbackProgressBar.getStyleClass().add("bar-exit");
                playbackProgressBar.getStyleClass().remove("bar-enter");
            }
        });

        // Bind ProgressBar progress to Slider value
        playbackProgressBar.progressProperty().bind(playbackSlider.valueProperty().divide(100.0));

        // Update the slider's value and time labels only when the user is not dragging
        AudioPlayer.getInstance().getMediaPlayer().currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!playbackSlider.isValueChanging()) {
                Platform.runLater(() -> {
                    Duration totalDuration = AudioPlayer.getInstance().getMediaPlayer().getTotalDuration();
                    if (totalDuration != null) {
                        double newValue = newTime.toMillis() / totalDuration.toMillis() * 100;
                        playbackSlider.setValue(newValue);
                        updateTimeLabels(newTime);
                    }
                });
            }
        });

        // Add listener to update the playback position when clicked directly on the slider
        playbackSlider.setOnMouseClicked(event -> {
            if (AudioPlayer.getInstance().isPlaying()) {
                double clickX = event.getX();
                double sliderWidth = playbackSlider.getWidth();
                double percentage = clickX / sliderWidth;

                Duration totalDuration = AudioPlayer.getInstance().getMediaPlayer().getTotalDuration();
                if (totalDuration != null) {
                    Duration seekTo = totalDuration.multiply(percentage);
                    AudioPlayer.getInstance().getMediaPlayer().seek(seekTo);
                }
            }
        });
    }

    /**
     * Updates the time labels (current time) on the UI.
     *
     * @param currentTime the current playback time to be displayed
     */
    private void updateTimeLabels(Duration currentTime) {
        currentTimeLabel.setText(formatTime(currentTime));
    }

    /**
     * Formats the given time (in Duration) to a string in the format "MM:SS".
     *
     * @param duration the Duration to be formatted
     * @return a string representation of the duration in "MM:SS" format
     */
    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Formats the given time (in seconds) to a string in the format "MM:SS".
     *
     * @param durationSeconds the duration in seconds to be formatted
     * @return a string representation of the time in "MM:SS" format
     */
    private String formatTime(int durationSeconds) {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Loads the details of a song (name, artist, image, and duration) and displays them in the UI.
     *
     * @param index the index of the song in the audio queue to be loaded
     */
    public void loadSong(int index) {
        int audioID = audioQueue.get(index);
        String songName = ResourceLoader.getAudioName(audioID);
        String artistName = ResourceLoader.getArtistNamefromAudioID(audioID);
        String imagePath = ResourceLoader.getAudioImagePath(audioID);
        int durationSeconds = ResourceLoader.getAudioDuration(audioID);

        // Convert duration to "MM:SS" format
        String formattedDuration = formatTime(durationSeconds);

        // Update the labels and image view
        songNameLabel.setText(songName);
        artistNameLabel.setText(artistName);
        durationLabel.setText(formattedDuration);
        songImage.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    }
}

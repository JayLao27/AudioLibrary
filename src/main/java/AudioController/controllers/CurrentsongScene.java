package AudioController.controllers;

import AudioController.PlaybackController;
import AudioController.ResourceLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class CurrentsongScene {
    @FXML
    ImageView songImage;
    @FXML
    Label songNameLabel;
    @FXML
    Label artistNameLabel;
    @FXML
    Label currentTimeLabel;
    @FXML
    Label durationLabel;
    @FXML
    Slider playbackSlider;

    @FXML
    public void initialize() {
        // Initialize playback slider and bind to the media player's current time
        setupSlider();
    }

    private void setupSlider() {
        // Prevent media player updates when the user is dragging the slider
        playbackSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                // When the user stops changing the slider value (i.e., releases the knob)
                Duration seekTo = PlaybackController.getInstance().getMediaPlayer().getMedia().getDuration()
                        .multiply(playbackSlider.getValue() / 100.0);
                PlaybackController.getInstance().getMediaPlayer().seek(seekTo);
            }
        });

        // This ensures the media player position is updated when the slider knob is released
        playbackSlider.setOnMouseReleased(event -> {
            if (PlaybackController.getInstance().isPlaying()) {
                Duration seekTo = PlaybackController.getInstance().getMediaPlayer().getMedia().getDuration()
                        .multiply(playbackSlider.getValue() / 100.0);
                PlaybackController.getInstance().getMediaPlayer().seek(seekTo);
            }
        });

        // Update the slider's value and time labels only when the user is not dragging
        PlaybackController.getInstance().getMediaPlayer().currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            // Only update the slider value if the user is not interacting with it
            if (!playbackSlider.isValueChanging()) {
                Platform.runLater(() -> {
                    // Update the slider's position based on the current time
                    double newValue = newTime.toMillis() / PlaybackController.getInstance().getMediaPlayer().getTotalDuration().toMillis() * 100;
                    playbackSlider.setValue(newValue);

                    // Update the current time label
                    updateTimeLabels(newTime);

                    // Update the slider track color dynamically based on value
                    updateSliderTrackColor(newValue);
                });
            }
        });

        // Set the duration label when the media player's total duration changes
        PlaybackController.getInstance().getMediaPlayer().totalDurationProperty().addListener((obs, oldDuration, newDuration) -> {
            if (newDuration != null) {
                durationLabel.setText(formatTime(newDuration));
            }
        });
    }

    private void updateSliderTrackColor(double newValue) {
        double percentage = newValue;  // In percentage (0-100)
        String style = String.format(
                "-fx-control-inner-background: linear-gradient(to right, " +
                        "-fx-accent 0%%, " +
                        "-fx-accent %.1f%%, " +
                        "-default-track-color %.1f%%, " +
                        "-default-track-color 100%%);",
                percentage, percentage);
        playbackSlider.setStyle(style);  // Update slider's style dynamically
    }

    private void updateTimeLabels(Duration currentTime) {
        currentTimeLabel.setText(formatTime(currentTime));
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void loadSong(int audioID) {
        songNameLabel.setText(ResourceLoader.getAudioName(audioID));
        artistNameLabel.setText(ResourceLoader.getArtistNamefromAudioID(audioID));

        String imagePath = ResourceLoader.getAudioImagePath(audioID);

        songImage.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    }
}

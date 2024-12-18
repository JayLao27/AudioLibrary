package AudioController.controllers;

import AudioController.AudioPlayer;
import AudioController.DatabaseConnection;
import AudioController.DownloadManager;
import AudioController.MouseEffects;
import AudioController.ResourceLoader;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the library list template scene. This scene displays song details, such as the song name
 * and cover image, and provides functionality for playing and downloading the audio.
 */
public class PlaylistTemplateScene {
    @FXML
    private Label songNameLabel;
    @FXML
    private ImageView songCoverImage, removeImage, playImage, downloadImage;

    private int audioID, playlistID;

    private List<Integer> audioQueue = new ArrayList<>();

    private PlaylistContentsScene playlistContentsScene;

    /**
     * Sets the audio ID for the current scene and loads the song details.
     *
     * @param audioID the ID of the audio to be displayed
     */
    public void setAudioID(int audioID) {
        this.audioID = audioID;
        System.out.println("Initializing with audio ID: " + audioID);
        loadAudioDetails();
    }

    /**
     * Sets the playlist ID for the current scene and loads the song details.
     *
     * @param playlistID the ID of the audio to be displayed
     */
    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
        System.out.println("Initializing with playlistID: " + playlistID);
        loadAudioDetails();
    }

    /**
     * Sets the audio queue, which is a list of audio IDs.
     *
     * @param audioQueue the list of audio IDs to be set as the audio queue
     */
    public void setQueue(List audioQueue) {
        this.audioQueue = audioQueue;
    }

    /**
     * Sets the {@link PlaylistContentsScene} instance, to call methods from it.
     *
     * @param playlistContentsScene the list of audio IDs to be set as the audio queue
     */
    public void setPlaylistContentsScene(PlaylistContentsScene playlistContentsScene) {
        this.playlistContentsScene = playlistContentsScene;
    }

    /**
     * Initializes the scene with mouse effects for the remove, play, and download buttons, and sets up event handlers
     * for clicking on the play and download buttons.
     */
    public void initialize() {
        MouseEffects.addMouseEffects(playImage);
        MouseEffects.addMouseEffects(downloadImage);

        removeImage.setOnMouseClicked(event -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Remove Audio");
            confirmationAlert.setHeaderText("Are you sure you want to remove this audio?");
            confirmationAlert.setContentText("You can add it back to the playlist later if needed.");

            // Show the dialog and wait for user response
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("Removing audio with ID: " + audioID);

                try (Connection connection = new DatabaseConnection().getConnection();
                     PreparedStatement stmt = connection.prepareStatement(
                             "DELETE FROM PlaylistAudio WHERE playlistID = ? AND audioID = ?")) {
                    stmt.setInt(1, playlistID);
                    stmt.setInt(2, audioID);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Audio successfully removed from playlist.");
                    } else {
                        System.out.println("Audio not found in playlist or already removed.");
                    }
                } catch (SQLException e) {
                    System.err.println("Error while removing audio: " + e.getMessage());
                    e.printStackTrace();
                }

                // Clear and reset the queue, and reload the playlist UI
                AudioPlayer.getInstance().clearQueue();
                AudioPlayer.getInstance().setQueue(audioQueue);
                playlistContentsScene.reloadSongList();
            } else {
                System.out.println("Audio removal canceled by user.");
            }
        });



        playImage.setOnMouseClicked(event -> {
            System.out.println("Redirecting to song...");
            AudioPlayer.getInstance().clearQueue();
            AudioPlayer.getInstance().setQueue(audioQueue);
            AudioPlayer.getInstance().playAudio(audioID); // Start playback with the clicked song
        });

        downloadImage.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Audio File");
            fileChooser.setInitialFileName(ResourceLoader.getAudioName(audioID) + ".mp3");
            Path destination = fileChooser.showSaveDialog(songCoverImage.getScene().getWindow()).toPath();

            if (destination != null) {
                DownloadManager.downloadAudio(audioID, destination);
            }
        });
    }

    /**
     * Loads the details of the audio, such as the song name and cover image.
     * The song name is fetched using the `ResourceLoader`, and the cover image is set if available.
     */
    private void loadAudioDetails() {
        String songName = ResourceLoader.getAudioName(audioID);
        songNameLabel.setText(songName);

        String artistImagePath = ResourceLoader.getAudioImagePath(audioID);
        if (artistImagePath != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(artistImagePath));
                if (!image.isError()) {
                    songCoverImage.setImage(image);
                } else {
                    System.out.println("Error loading image: " + artistImagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception loading image: " + artistImagePath);
            }
        } else {
            System.out.println("Artist image path is null for artistID: " + audioID);
        }
    }

    @FXML
    private void onMouseEnteredImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), removeImage);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();

        Image hoverImage = new Image(getClass().getResourceAsStream("/ProjectImages/binred.png"));
        removeImage.setImage(hoverImage);
    }

    @FXML
    private void onMouseExitedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), removeImage);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();

        Image hoverImage = new Image(getClass().getResourceAsStream("/ProjectImages/binwhite.png"));
        removeImage.setImage(hoverImage);
    }

    @FXML
    private void onMousePressedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), removeImage);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);
        scaleTransition.play();

        Image hoverImage = new Image(getClass().getResourceAsStream("/ProjectImages/binred.png"));
        removeImage.setImage(hoverImage);
    }

    @FXML
    private void onMouseReleasedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), removeImage);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();
    }
}

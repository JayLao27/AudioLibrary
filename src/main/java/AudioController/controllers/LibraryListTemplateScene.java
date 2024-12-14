package AudioController.controllers;

import AudioController.AudioPlayer;
import AudioController.DownloadManager;
import AudioController.MouseEffects;
import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the library list template scene. This scene displays song details, such as the song name
 * and cover image, and provides functionality for playing and downloading the audio.
 */
public class LibraryListTemplateScene {
    @FXML
    private Label songNameLabel;
    @FXML
    private ImageView songCoverImage, playImage, downloadImage;

    private int audioID;

    private List<Integer> audioQueue = new ArrayList<>();

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
     * Sets the audio queue, which is a list of audio IDs.
     *
     * @param audioQueue the list of audio IDs to be set as the audio queue
     */
    public void setQueue(List audioQueue) {
        this.audioQueue = audioQueue;
    }

    /**
     * Initializes the scene with mouse effects for the play and download buttons, and sets up event handlers
     * for clicking on the play and download buttons.
     */
    public void initialize() {
        MouseEffects.addMouseEffects(playImage);
        MouseEffects.addMouseEffects(downloadImage);

        playImage.setOnMouseClicked(event -> {
            System.out.println("Redirecting to song...");
            AudioPlayer.getInstance().setQueue(audioQueue); // Set the entire queue
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
}

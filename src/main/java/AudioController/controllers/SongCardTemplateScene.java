package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller class for the SongCardTemplateScene, responsible for displaying
 * song details including the song name and cover image.
 */
public class SongCardTemplateScene {
    @FXML
    private ImageView songCoverImage;
    @FXML
    private Label songNameLabel;

    private int audioID;

    /**
     * Sets the audio ID for the song card and loads the corresponding song details.
     * This method is called to initialize the song card with specific details.
     *
     * @param audioID The ID of the audio to load details for.
     */
    public void setAudioID(int audioID) {
        this.audioID = audioID;
        System.out.println("Initializing with audio ID: " + audioID);
        loadAudioDetails();
    }

    public void initialize() {}

    /**
     * Loads the audio details including the song name and artist's cover image
     * based on the audio ID.
     * This method fetches the song name and image from the resource loader and
     * updates the corresponding UI elements.
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

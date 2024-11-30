package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SongpageScene {
    @FXML
    private ImageView songCoverImage;
    @FXML
    private Label songNameLabel;
    @FXML
    private Label artistNameLabel;
    @FXML
    private Label albumNameLabel;
    @FXML
    private Button addtocartButton;

    private int audioID;

    public void setAudioID(int audioID) {
        this.audioID = audioID;
        System.out.println("Initializing with audio ID: " + audioID);
        loadAudioDetails();
    }

    public void initialize() {}

    private void loadAudioDetails() {
        String songName = ResourceLoader.getAudioName(audioID);
        songNameLabel.setText(songName);

        String artistName = ResourceLoader.getArtistNamefromAudioID(audioID);
        artistNameLabel.setText(artistName);

        String albumName = ResourceLoader.getAlbumName(audioID);
        albumNameLabel.setText(albumName);

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

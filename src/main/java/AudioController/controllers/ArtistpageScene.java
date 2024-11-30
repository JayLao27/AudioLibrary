package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArtistpageScene {
    @FXML
    private ImageView artistImage;
    @FXML
    private Label artistNameLabel;

    private int artistID;

    public void setArtistID(int artistID) {
        this.artistID = artistID;
        System.out.println("1. Initializing with artist ID: " + artistID);
        loadArtistDetails();
    }

    public void initialize() {}

    private void loadArtistDetails() {
        String artistName = ResourceLoader.getArtistName(artistID);
        artistNameLabel.setText(artistName);

        String artistImagePath = ResourceLoader.getArtistImagePath(artistID);
        if (artistImagePath != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(artistImagePath));
                if (!image.isError()) {
                    artistImage.setImage(image);
                } else {
                    System.out.println("Error loading image: " + artistImagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception loading image: " + artistImagePath);
            }
        } else {
            System.out.println("Artist image path is null for artistID: " + artistID);
        }
    }
}

package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartlisttemplateScene {

    @FXML
    private Label songNameLabel;
    @FXML
    private ImageView songCoverImage;

    private int audioID;

    public void setAudioID(int audioID) {
        this.audioID = audioID;
        System.out.println("Initializing with audio ID: " + audioID);
        loadAudioDetails();
    }

    @FXML
    private void initialize() {}

    private void loadAudioDetails() {
        String songName = ResourceLoader.getAudioName(audioID);
        songNameLabel.setText(songName);

        String songImagePath = ResourceLoader.getAudioImagePath(audioID);
        if (songImagePath != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(songImagePath));
                if (!image.isError()) {
                    songCoverImage.setImage(image);
                } else {
                    System.out.println("Error loading image: " + songImagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception loading image: " + songImagePath);
            }
        } else {
            System.out.println("Song image path is null for audioID: " + audioID);
        }
    }
}

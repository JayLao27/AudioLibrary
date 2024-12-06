package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CheckoutListTemplateScene {

    @FXML
    private Label songNameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private ImageView songCoverImage;

    private CheckoutScene checkoutScene;

    private int audioID;

    @FXML
    private void initialize() {}

    public void setAudioID(int audioID, CheckoutScene checkoutScene) {
        this.audioID = audioID;
        this.checkoutScene = checkoutScene;
        System.out.println("Initializing with audio ID: " + audioID);
        loadAudioDetails();
    }

    private void loadAudioDetails() {
        String songName = ResourceLoader.getAudioName(audioID);
        songNameLabel.setText(songName);

        Double price = ResourceLoader.getAudioPrice(audioID);
        if (price == 0) {
            priceLabel.setText("FREE");
        } else if (price > 0){
            priceLabel.setText("₱ " + String.format("%.2f", price));
        } else {
            priceLabel.setText("???");
        }

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

package AudioController.controllers;

import AudioController.AudioPlayer;
import AudioController.ResourceLoader;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class LibrarylisttemplateScene {


    @FXML
    private Label songNameLabel;
    @FXML
    private ImageView songCoverImage;
    @FXML
    private ImageView playImage;

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

    //Logic
    @FXML
    private void handlePlayClicked(MouseEvent event) {
        AudioPlayer.playAudio(audioID);
        event.consume();
    }


    //Play Button UX
    @FXML
    private void onMouseEnteredImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), playImage);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();

    }

    @FXML
    private void onMouseExitedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), playImage);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    @FXML
    private void onMousePressedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), playImage);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);
        scaleTransition.play();

    }

    @FXML
    private void onMouseReleasedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), playImage);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();
    }
}

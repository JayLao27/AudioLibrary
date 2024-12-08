package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class PlaylistCardTemplateScene {
    @FXML
    private ImageView playlistImage;
    @FXML
    private Label playlistNameLabel;

    private int playlistID;

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID; // Correct assignment
        System.out.println("Initializing with playlist ID: " + this.playlistID);
        loadAudioDetails();
    }

    public void initialize() {}

    private void loadAudioDetails() {
        String playlistName = ResourceLoader.getPlaylistName(playlistID);
        if (playlistName != null) {
            playlistNameLabel.setText(playlistName);
        } else {
            System.out.println("Playlist name is null for playlistID: " + playlistID);
        }

        // Get the playlist image path from ResourceLoader
        String playlistImagePath = ResourceLoader.getPlaylistImagePath(playlistID);

        // Try to load the image using absolute file path
        try {
            File imageFile = new File(playlistImagePath);

            // Check if file exists
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());

                // Check image loading status
                if (!image.isError()) {
                    playlistImage.setImage(image);
                } else {
                    System.out.println("Error loading image from path: " + playlistImagePath);
                    setDefaultImage();
                }
            } else {
                System.out.println("Image file does not exist: " + playlistImagePath);
                setDefaultImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception loading image: " + playlistImagePath);
            setDefaultImage();
        }
    }

    // Helper method to set a default image
    private void setDefaultImage() {
        try {
            // More robust path construction
            String defaultImagePath = System.getProperty("user.dir") +
                    File.separator + "src" +
                    File.separator + "main" +
                    File.separator + "resources" +
                    File.separator + "ProjectImages" +
                    File.separator + "Vector.png";

            File defaultImageFile = new File(defaultImagePath);

            if (defaultImageFile.exists()) {
                // Use URL encoding to handle potential spaces in path
                Image defaultImage = new Image(defaultImageFile.toURI().toString());

                if (!defaultImage.isError()) {
                    playlistImage.setImage(defaultImage);
                } else {
                    System.out.println("Error loading default image");
                    playlistImage.setImage(null);
                }
            } else {
                System.out.println("Default image file not found at: " + defaultImagePath);
                playlistImage.setImage(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            playlistImage.setImage(null);
        }
    }
}

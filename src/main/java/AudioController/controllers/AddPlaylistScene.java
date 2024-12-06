package AudioController.controllers;

import AudioController.Playlist; // Import the Playlist model
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.File;

public class AddplaylistScene {

    @FXML
    private TextField playlistNameField; // TextField for the playlist name

    @FXML
    private ImageView playlistImageView; // ImageView to display the uploaded image

    @FXML
    private Button uploadButton; // Button to upload the image

    @FXML
    private Button saveButton; // Button to save the playlist

    private File selectedImageFile; // To store the selected image file

    @FXML
    public void initialize() {
        // No need to initialize a ListView
    }

    @FXML
    private void handleUploadButtonClicked(MouseEvent event) {
        // Create a FileChooser to allow the user to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg")
        );

        // Show the file chooser and get the selected file
        File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (file != null) {
            // Load the selected image and set it to the ImageView
            Image image = new Image(file.toURI().toString());
            playlistImageView.setImage(image); // Set the uploaded image to the ImageView
            selectedImageFile = file; // Store the selected image file
        }
    }

    @FXML
    private void handleSaveButtonClicked(MouseEvent event) {
        String playlistName = playlistNameField.getText();

        // Logic to save the playlist (e.g., create a new Playlist object)
        if (selectedImageFile != null && !playlistName.isEmpty()) {
            // Call a method to update the PlaylistScene with the new playlist
            updatePlaylistScene(playlistName, selectedImageFile);
        } else {
            System.out.println("Please enter a playlist name and select an image.");
        }
    }

    private void updatePlaylistScene(String playlistName, File imageFile) {
        // This method is currently empty. You can add logic here later if needed.
        // For example, you could create a new Playlist object here.
        Playlist newPlaylist = new Playlist(playlistName, imageFile);
        System.out.println("Playlist created: " + playlistName + " with image " + imageFile.getAbsolutePath());
    }
}
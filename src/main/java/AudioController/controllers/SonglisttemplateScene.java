package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.ResourceLoader;
import AudioController.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SonglisttemplateScene {

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

    public void addToCart() {
        int userID = UserSession.getInstance().getUserID();

        String query = """
            INSERT INTO CartAudio (userID, audioID)
            VALUES (?, ?)
            """;

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the userID and audioID in the prepared statement
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, audioID);

            // Execute the query to insert the audioID into the CartAudio table
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Audio added to cart successfully!");
            } else {
                System.out.println("Failed to add audio to cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding audio to cart: " + e.getMessage());
        }
    }
}

package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.ResourceLoader;
import AudioController.UserSession;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongPageScene {
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

        int userID = UserSession.getInstance().getUserID();
        Double price = ResourceLoader.getAudioPrice(audioID);

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

        if (isSongInLibrary(userID, audioID)) {
            addtocartButton.setText("Already Bought");
            addtocartButton.setDisable(true);
            return; // Exit after updating if the song is bought
        }

        if (isSongInCart(userID, audioID)) {
            addtocartButton.setText("In Cart");
            addtocartButton.setDisable(true);
            return; // Exit if the song is in the cart
        }

        if(price == 0) {
            addtocartButton.setText("Add to Cart (FREE)");
        } else if (price > 0) {
            addtocartButton.setText("Add to Cart (₱ " + String.valueOf(price) +")");
        } else {
            addtocartButton.setText("Add to Cart (₱ ???)");
        }

    }

    private boolean isSongInLibrary(int userID, int audioID) {
        String query = "SELECT COUNT(*) FROM LibraryAudio WHERE userID = ? AND audioID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, audioID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking if song is in library: " + e.getMessage());
        }
        return false;
    }

    private boolean isSongInCart(int userID, int audioID) {
        String query = "SELECT COUNT(*) FROM CartAudio WHERE userID = ? AND audioID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, audioID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking if song is in cart: " + e.getMessage());
        }
        return false;
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

                // Refresh UI after adding the song to the cart
                loadAudioDetails();
            } else {
                System.out.println("Failed to add audio to cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding audio to cart: " + e.getMessage());
        }
    }

    //Button UX
    @FXML
    private void handleButtonEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setToX(1.11);
        scaleTransition.setToY(1.11);
        scaleTransition.play();
    }

    @FXML
    private void handleButtonExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    @FXML
    private void handleButtonPressed(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
    }

    @FXML
    private void handleButtonReleased(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), button);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();
    }
}

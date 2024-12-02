package AudioController.controllers;

import AudioController.ResourceLoader;
import AudioController.DatabaseConnection;
import AudioController.UserSession;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static AudioController.ResourceLoader.getAudioPrice;

public class CartlisttemplateScene {

    @FXML
    private CheckBox cartCheckBox;
    @FXML
    private Label songNameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private ImageView songCoverImage;
    @FXML
    private ImageView deleteImage;

    private CartScene cartScene;

    private int audioID;

    public void setAudioID(int audioID, CartScene cartScene) {
        this.audioID = audioID;
        this.cartScene = cartScene;
        System.out.println("Initializing with audio ID: " + audioID);
        loadAudioDetails();
    }

    @FXML
    private void initialize() {}

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

    public void handleDeleteClicked(MouseEvent event) {
        deleteAudioFromCart(UserSession.getInstance().getUserID(), audioID);

        if (cartScene != null) {
            cartScene.reloadCart();
        }

        event.consume();
    }

    private void deleteAudioFromCart(int userID, int audioID) {
        try (Connection connection = new DatabaseConnection().getConnection()) {
            String sql = "DELETE FROM CartAudio WHERE userID = ? AND audioID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userID);
                stmt.setInt(2, audioID);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Successfully deleted audio ID " + audioID + " from user " + userID + "'s cart.");
                } else {
                    System.out.println("Failed to delete audio ID " + audioID + " from user " + userID + "'s cart.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting audio from cart: " + e.getMessage());
        }
    }

    //Delete Button UX
    @FXML
    private void onMouseEnteredImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), deleteImage);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();

        Image hoverImage = new Image(getClass().getResourceAsStream("/ProjectImages/binred.png"));
        deleteImage.setImage(hoverImage);
    }

    @FXML
    private void onMouseExitedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), deleteImage);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();

        Image hoverImage = new Image(getClass().getResourceAsStream("/ProjectImages/binwhite.png"));
        deleteImage.setImage(hoverImage);
    }

    @FXML
    private void onMousePressedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), deleteImage);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);
        scaleTransition.play();

        Image hoverImage = new Image(getClass().getResourceAsStream("/ProjectImages/binred.png"));
        deleteImage.setImage(hoverImage);
    }

    @FXML
    private void onMouseReleasedImage(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), deleteImage);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();
    }

    //Checkbox logic
    @FXML
    private void handleCheckboxChange(ActionEvent event) {
        double price = getAudioPrice(audioID);
        if (cartCheckBox.isSelected()) {
            cartScene.incrementCheckoutDetails(audioID, price);
        } else {
            cartScene.decrementCheckoutDetails(audioID, price);
        }
        cartScene.updateCheckoutDetails();
    }
}

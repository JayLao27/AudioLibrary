package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.ResourceLoader;
import AudioController.SceneWithHomeContext;
import AudioController.UserSession;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CheckoutScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    @FXML
    private Label totalamountLabel;
    @FXML
    private VBox checkoutlistVBox;

    private List<Integer> checkedAudio = new ArrayList<>();

    @FXML
    private void initialize() {}

    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    public void loadCheckoutList(List<Integer> checkedAudio) {
        this.checkedAudio = checkedAudio;
        checkoutlistVBox.getChildren().clear(); // Clear the VBox before loading new items
        double total = 0;

        for (int audioID : checkedAudio) {
            try {
                // Load the FXML template for the cart list item
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/checkoutlisttemplateScene.fxml"));
                AnchorPane checkoutItem = fxmlLoader.load();

                // Get the controller for the loaded FXML and set the audioID
                CheckoutlisttemplateScene controller = fxmlLoader.getController();
                controller.setAudioID(audioID, this);

                // Add the checkout item to the VBox
                checkoutlistVBox.getChildren().add(checkoutItem);
                total += ResourceLoader.getAudioPrice(audioID);
                totalamountLabel.setText(String.format("%.2f", total));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Logic
    @FXML
    private void handlePurchaseClicked(MouseEvent event) {
        // Assume userID and balance are placeholders for the logged-in user's details
        int userID = UserSession.getInstance().getUserID(); // Replace with the actual logged-in user's ID
        double userBalance = 100.00; // Replace with a method to retrieve the actual balance

        double totalPrice = 0.0;
        String priceQuery = "SELECT audioPrice FROM Audio WHERE audioID = ?";
        String insertQuery = "INSERT IGNORE INTO LibraryAudio (userID, audioID) VALUES (?, ?)";
        String deleteQuery = "DELETE FROM CartAudio WHERE userID = ? AND audioID = ?";  // Query to remove from CartAudio

        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement priceStmt = conn.prepareStatement(priceQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

            // Calculate the total price
            for (int audioID : checkedAudio) {
                priceStmt.setInt(1, audioID);
                try (ResultSet rs = priceStmt.executeQuery()) {
                    if (rs.next()) {
                        totalPrice += rs.getDouble("audioPrice");
                    }
                }
            }

            // Check if the user has enough balance
            if (userBalance >= totalPrice) {
                // Deduct balance (placeholder logic)
                userBalance -= totalPrice;
                System.out.println("Purchase successful! Remaining balance: " + userBalance);

                // Update LibraryAudio table
                for (int audioID : checkedAudio) {
                    insertStmt.setInt(1, userID);
                    insertStmt.setInt(2, audioID);
                    insertStmt.executeUpdate();
                }

                // Remove the purchased audio from the CartAudio table
                for (int audioID : checkedAudio) {
                    deleteStmt.setInt(1, userID);
                    deleteStmt.setInt(2, audioID);
                    deleteStmt.executeUpdate();
                }

                System.out.println("Library updated with purchased audio, and CartAudio removed.");

                // Success prompt
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                        "Would you like to generate a receipt for this purchase?",
                        ButtonType.YES, ButtonType.NO);

                successAlert.setTitle("Purchase Successful");
                successAlert.setHeaderText("Your purchase was successful!");

                // Wait for the user response (yes or no)
                double finalTotalPrice = totalPrice;
                successAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        // Generate a receipt (Placeholder logic)
                        displayReceiptDetails(userID, finalTotalPrice);
                    } else {
                        System.out.println("Receipt generation canceled.");
                    }
                });

                if(homeScene != null) {
                    homeScene.loadScene("/FXMLs/libraryScene.fxml");
                }

            } else {
                // Failure prompt for insufficient balance
                Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                failureAlert.setTitle("Insufficient Balance");
                failureAlert.setHeaderText("Purchase failed!");
                failureAlert.setContentText("You don't have enough balance for this purchase. Please add funds.");

                failureAlert.showAndWait();
                System.out.println("Insufficient balance. Total price: " + totalPrice + ", Available balance: " + userBalance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayReceiptDetails(int userID, double totalPrice) {
        StringBuilder audioNames = new StringBuilder();
        String audioNamesQuery = "SELECT audioName FROM Audio WHERE audioID = ?";

        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(audioNamesQuery)) {

            // Collect the names of the audio files purchased
            for (int audioID : checkedAudio) {
                stmt.setInt(1, audioID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        if (audioNames.length() > 0) {
                            audioNames.append(", ");
                        }
                        audioNames.append(rs.getString("audioName"));
                    }
                }
            }

            // Format the current time to a readable format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = LocalDateTime.now().format(formatter);

            // Generate the receipt display
            Alert receiptAlert = new Alert(Alert.AlertType.INFORMATION);
            receiptAlert.setTitle("Receipt");
            receiptAlert.setHeaderText("Purchase Receipt");

            String receiptMessage = "User ID: " + userID + "\n" +
                    "Total Amount: $" + totalPrice + "\n" +
                    "Purchased Items: " + audioNames.toString() + "\n" +
                    "Purchase Time: " + formattedTime;

            receiptAlert.setContentText(receiptMessage);
            receiptAlert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Button UX
    @FXML
    private void handleCancelClicked(MouseEvent event) {
        if(homeScene != null) {
            homeScene.loadScene("/FXMLs/CartScene.fxml");
        } else  {
            System.out.println("HomeScene is null!");
        }
    }

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

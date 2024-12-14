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
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The CheckoutScene class handles the user interface and logic for the checkout process in the audio library application.
 * It manages the display of the checkout list, the purchase logic, balance verification, and receipt generation.
 * The class interacts with the database to retrieve user balance, calculate the total price of selected audio items,
 * and update the user's library and payment records.
 * Implements the {@link SceneWithHomeContext} interface for managing navigation.
 */
public class CheckoutScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    @FXML
    private Label totalamountLabel;
    @FXML
    private VBox checkoutlistVBox;

    private List<Integer> checkedAudio = new ArrayList<>();

    @FXML
    private void initialize() {}

    /**
     * Sets the {@link HomeScene} instance to enable navigation from the artist page.
     *
     * @param homeScene The home scene instance for navigation purposes.
     */
    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    /**
     * Loads the checkout list by fetching audio items selected for purchase.
     * It updates the checkout list and calculates the total price.
     *
     * @param checkedAudio a list of audio IDs that the user has selected for purchase
     */
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
                CheckoutListTemplateScene controller = fxmlLoader.getController();
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

    /**
     * Handles the purchase button click event.
     * Verifies the user's balance, processes the payment, updates the user's library, and generates a receipt if successful.
     *
     * @param event the mouse event triggered by the user clicking the purchase button
     */
    //Logic
    @FXML
    private void handlePurchaseClicked(MouseEvent event) {
        int userID = UserSession.getInstance().getUserID();
        String paymentMethod = "Credit Card"; // Placeholder for the payment method from the UI

        double totalPrice = 0.0;
        String getUserBalanceQuery = "SELECT balance FROM User WHERE userID = ?";
        String updateUserBalanceQuery = "UPDATE User SET balance = ? WHERE userID = ?";
        String priceQuery = "SELECT audioPrice FROM Audio WHERE audioID = ?";
        String insertQuery = "INSERT IGNORE INTO LibraryAudio (userID, audioID) VALUES (?, ?)";
        String deleteQuery = "DELETE FROM CartAudio WHERE userID = ? AND audioID = ?";
        String insertPaymentQuery = "INSERT INTO Payments (userID, amount, paymentMethod) VALUES (?, ?, ?)";
        String insertPaymentAudioQuery = "INSERT INTO PaymentAudio (paymentID, audioID) VALUES (?, ?)";

        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement getBalanceStmt = conn.prepareStatement(getUserBalanceQuery);
             PreparedStatement updateBalanceStmt = conn.prepareStatement(updateUserBalanceQuery);
             PreparedStatement priceStmt = conn.prepareStatement(priceQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement insertPaymentStmt = conn.prepareStatement(insertPaymentQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertPaymentAudioStmt = conn.prepareStatement(insertPaymentAudioQuery)) {

            // Retrieve the user's current balance
            getBalanceStmt.setInt(1, userID);
            double userBalance;
            try (ResultSet rs = getBalanceStmt.executeQuery()) {
                if (rs.next()) {
                    userBalance = rs.getDouble("balance");
                } else {
                    throw new SQLException("User not found.");
                }
            }

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
                // Deduct balance and update in the database
                double newBalance = userBalance - totalPrice;
                updateBalanceStmt.setDouble(1, newBalance);
                updateBalanceStmt.setInt(2, userID);
                updateBalanceStmt.executeUpdate();

                System.out.println("Purchase successful! Remaining balance: " + newBalance);

                // Insert the payment record into the Payments table
                insertPaymentStmt.setInt(1, userID);
                insertPaymentStmt.setDouble(2, totalPrice);
                insertPaymentStmt.setString(3, paymentMethod);
                insertPaymentStmt.executeUpdate();

                // Get the generated paymentID
                try (ResultSet generatedKeys = insertPaymentStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int paymentID = generatedKeys.getInt(1);

                        // Insert the audio into LibraryAudio table and PaymentAudio table
                        for (int audioID : checkedAudio) {
                            insertStmt.setInt(1, userID);
                            insertStmt.setInt(2, audioID);
                            insertStmt.executeUpdate();

                            insertPaymentAudioStmt.setInt(1, paymentID);
                            insertPaymentAudioStmt.setInt(2, audioID);
                            insertPaymentAudioStmt.executeUpdate();
                        }

                        // Remove the purchased audio from the CartAudio table
                        for (int audioID : checkedAudio) {
                            deleteStmt.setInt(1, userID);
                            deleteStmt.setInt(2, audioID);
                            deleteStmt.executeUpdate();
                        }

                        System.out.println("Library updated with purchased audio, CartAudio removed, and PaymentAudio entries added.");

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
                                // Generate a receipt
                                displayReceiptDetails(userID, finalTotalPrice);
                            } else {
                                System.out.println("Receipt generation canceled.");
                            }
                        });

                        if (homeScene != null) {
                            homeScene.loadScene("/FXMLs/libraryScene.fxml");
                        }

                    } else {
                        System.out.println("Failed to retrieve generated payment ID.");
                    }
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

    /**
     * Displays the receipt details in an alert dialog after a successful purchase.
     *
     * @param userID the ID of the user who made the purchase
     * @param totalPrice the total amount paid for the purchase
     */
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = LocalDateTime.now().format(formatter);

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


    /**
     * Handles the cancel button click event, which returns the user to the cart scene.
     *
     * @param event the mouse event triggered by the user clicking the cancel button
     */
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

package AudioController.controllers;

import AudioController.*;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfileScene implements SceneWithHomeContext {
    private HomeScene homeScene;

    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }
    private ResourceLoader resourceLoader = new ResourceLoader();

    @FXML
    private Label usernameLabel, firstnameLabel, lastnameLabel,  emailLabel, thankYouLabel, balanceLabel;
    @FXML
    private Button logoutButton;


    @FXML
    public void onViewPaymentClicked(javafx.event.ActionEvent actionEvent) {
        System.out.println("Button clicked!");
        if (homeScene != null) {
            homeScene.loadScene("/FXMLs/paymenthistoryScene.fxml");
        }
    }

    @FXML
    public void initialize() {
        setupLogoutTooltip();
        displayUserProfile(); // Ensure this method is called to display profile data
    }

    private void setupLogoutTooltip() {
        Tooltip tooltip = new Tooltip("Click to Logout");
        logoutButton.setTooltip(tooltip);
    }

    private void displayUserProfile() {
        int userID = UserSession.getInstance().getUserID();
        if (userID != 0) {
            User user = resourceLoader.getProfile(userID);
            if (user != null) {
                // Set user information in the labels
                usernameLabel.setText(user.getUserName());
                firstnameLabel.setText(user.getFirstName());
                lastnameLabel.setText(user.getLastName());
                emailLabel.setText(user.getEmail());

                // Update balance label
                balanceLabel.setText("Balance: " + user.getBalance());
            } else {
                // If user not found in DB
                usernameLabel.setText("User not found.");
                firstnameLabel.setText("-");
                lastnameLabel.setText("-");
                emailLabel.setText("-");
                balanceLabel.setText("Balance: -");
            }
        } else {
            // No valid userID in the session
            usernameLabel.setText("No user logged in.");
            firstnameLabel.setText("-");
            lastnameLabel.setText("-");
            emailLabel.setText("-");
            balanceLabel.setText("Balance: -");
        }
    }

    @FXML
    private void logout(MouseEvent event) {
        displayThankYouMessage("Thank you for using the app!");
        UserSession.getInstance().clearSession(); // Clear user session
        loadLoginScene();
    }

    private void displayThankYouMessage(String message) {
        thankYouLabel.setText(message);

        thankYouLabel.setOpacity(1.0);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(3000), thankYouLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setDelay(Duration.seconds(1));

        fadeTransition.play();
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

    private void scaleButton(Button button, double scale, int durationMillis) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(durationMillis), button);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.play();
    }

    private void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/loginScene.fxml"));
            Parent loginScene = loader.load();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.setScene(new Scene(loginScene));
            currentStage.show();
            System.out.println("Login Scene loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading loginScene.fxml");
        }
    }

    @FXML
    private void handleTopUpClicked(MouseEvent event) {
        // Create a dialog to prompt for the top-up amount
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Top-Up Balance");
        dialog.setHeaderText("Add Balance");
        dialog.setContentText("Enter the amount to top-up:");

        // Show the dialog and capture the result
        dialog.showAndWait().ifPresent(input -> {
            try {
                // Parse the input as a double
                double topUpAmount = Double.parseDouble(input);

                if (topUpAmount <= 0) {
                    throw new NumberFormatException("Amount must be positive.");
                }

                // Get the user ID
                int userID = UserSession.getInstance().getUserID();

                // Update the balance in the database
                String updateQuery = "UPDATE User SET balance = balance + ? WHERE userID = ?";
                try (Connection connection = new DatabaseConnection().getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                    preparedStatement.setDouble(1, topUpAmount);
                    preparedStatement.setInt(2, userID);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Top-up successful. Amount added: " + topUpAmount);
                        displayThankYouMessage("Balance updated successfully!");

                        // Update the UI with the new balance
                        displayUserProfile();
                    } else {
                        System.out.println("Failed to update balance. User ID not found.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error updating the balance.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + input);
                displayThankYouMessage("Invalid amount entered. Please try again.");
            }
        });
    }

}

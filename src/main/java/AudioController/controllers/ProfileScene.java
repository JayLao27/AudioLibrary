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

/**
 * Controller for the ProfileScene of the application.
 * This class manages the profile view of the user, displaying user information,
 * handling user interactions such as logout, viewing payment history, and top-up balance.
 */
public class ProfileScene implements SceneWithHomeContext {
    private HomeScene homeScene;

    /**
     * Sets the {@link HomeScene} instance to enable navigation from the artist page.
     *
     * @param homeScene The home scene instance for navigation purposes.
     */
    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    private ResourceLoader resourceLoader = new ResourceLoader();

    @FXML
    private Label usernameLabel, firstnameLabel, lastnameLabel,  emailLabel, thankYouLabel, balanceLabel;
    @FXML
    private Button logoutButton;

    /**
     * Loads a payment history scene on click.
     */
    @FXML
    public void onViewPaymentClicked(javafx.event.ActionEvent actionEvent) {
        System.out.println("Button clicked!");
        if (homeScene != null) {
            homeScene.loadScene("/FXMLs/paymenthistoryScene.fxml");
        }
    }

    /**
     * Initializes the ProfileScene by setting up tooltips for buttons and displaying user profile information.
     */
    @FXML
    public void initialize() {
        setupLogoutTooltip();
        displayUserProfile(); // Ensure this method is called to display profile data
    }

    /**
     * Sets up the tooltip for the logout button.
     */
    private void setupLogoutTooltip() {
        Tooltip tooltip = new Tooltip("Click to Logout");
        logoutButton.setTooltip(tooltip);
    }

    /**
     * Displays the profile information of the current user.
     * Retrieves user details from the database and populates the labels.
     */
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

    /**
     * Logs the user out by clearing the session and loading the login scene.
     * @param event The mouse event triggered by the logout action.
     */
    @FXML
    private void logout(MouseEvent event) {
        displayThankYouMessage("Thank you for using the app!");
        UserSession.getInstance().clearSession(); // Clear user session
        loadLoginScene();
    }

    /**
     * Displays a thank you message with a fade-out effect after logout.
     * @param message The message to display.
     */
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

    /**
     * Loads the login scene after logging out the user.
     */
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

    /**
     * Handles the top-up balance action triggered by the user.
     * Prompts the user for an amount and updates the balance in the database.
     * @param event The mouse event triggered when the top-up action is initiated.
     */
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

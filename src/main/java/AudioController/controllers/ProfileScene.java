package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.controllers.User;
import AudioController.UserSession;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileScene {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label firstnameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Label thankYouLabel;

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
            User user = fetchUserProfileFromDatabase(userID);
            if (user != null) {
                // Set user information in the labels
                usernameLabel.setText(user.getUserName());
                firstnameLabel.setText(user.getFirstName());
                lastnameLabel.setText(user.getLastName());
                emailLabel.setText(user.getEmail());
            } else {
                // If user not found in DB
                usernameLabel.setText("User not found.");
                firstnameLabel.setText("-");
                lastnameLabel.setText("-");
                emailLabel.setText("-");
            }
        } else {
            // No valid userID in the session
            usernameLabel.setText("No user logged in.");
            firstnameLabel.setText("-");
            lastnameLabel.setText("-");
            emailLabel.setText("-");
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

    private User fetchUserProfileFromDatabase(int userID) {
        String query = "SELECT userName, firstName, lastName, email FROM User WHERE userID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID); // Set userID in the query
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("userName"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving user profile from database.");
        }
        return null;
    }
}

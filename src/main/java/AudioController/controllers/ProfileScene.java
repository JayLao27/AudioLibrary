package AudioController.controllers;

import AudioController.DatabaseConnection;
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
    private Button logoutButton;

    @FXML
    private Label thankYouLabel;

    @FXML
    public void initialize() {
        setupLogoutTooltip();
        displayUsername();
    }

    private void setupLogoutTooltip() {
        Tooltip tooltip = new Tooltip("Click to Logout");
        logoutButton.setTooltip(tooltip);
    }

    private void displayUsername() {
        int userID = UserSession.getInstance().getUserID();
        if (userID != 0) {
            String userName = fetchUsernameFromDatabase(userID);
            usernameLabel.setText(userName != null ? userName : "User not found.");
        } else {
            usernameLabel.setText("Guest");
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
            System.out.println("Login Scene loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading loginScene.fxml");
        }
    }

    private String fetchUsernameFromDatabase(int userID) {
        String query = "SELECT userID FROM User WHERE userName = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("userName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving username from database.");
        }
        return null;
    }
}

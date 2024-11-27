package AudioController.controllers;

import javafx.animation.Interpolator;
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

public class ProfileScene {

    @FXML
    private Button logoutButton;

    @FXML
    private Label thankYouLabel;

    @FXML
    public void initialize() {
        Tooltip tooltip = new Tooltip("Click to Logout");
        logoutButton.setTooltip(tooltip);
    }

    @FXML
    private void logout(MouseEvent event) {
        setThankYouMessage("Thank you for using the app!");
        LogoutScene();
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

    private void setThankYouMessage(String message) {
        thankYouLabel.setText(message);
    }

    @FXML
    public void LogoutScene() {
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
}

package AudioController.controllers;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.awt.event.ActionEvent;

public class ProfileScene {

    @FXML
    private Button logoutButton;

    @FXML
    private void logout (MouseEvent Event){
        handleLogoutAction();
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
    private void handleLogoutAction() {
        try {
            // Load the LoginScene FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AudioController/views/loginScene.fxml"));
            Parent loginScene = loader.load();

            // Get the current stage (window) and set the new scene
            logoutButton.getScene().setRoot(loginScene);
        } catch (Exception e) {
            e.printStackTrace(); // Print any errors to the console for debugging
        }
    }

}

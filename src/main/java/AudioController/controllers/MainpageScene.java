package AudioController.controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class MainpageScene {

    @FXML
    private void handleImageEntered(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        // Apply a smooth transition to scale the image
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(120), imageView);
        scaleTransition.setToX(1.1); // Scale 1.1 times on the X-axis
        scaleTransition.setToY(1.1); // Scale 1.1 times on the Y-axis
        scaleTransition.play(); // Play the transition
    }

    @FXML
    private void handleImageExited(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        // Apply a smooth transition to reset the scale back to 1
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(120), imageView);
        scaleTransition.setToX(1.0); // Reset scale to original size on the X-axis
        scaleTransition.setToY(1.0); // Reset scale to original size on the Y-axis
        scaleTransition.play(); // Play the transition
    }

    @FXML
    private void handleImageReleased(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        // Apply a smooth transition to scale the image to 1.1 again when released
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), imageView);
        scaleTransition.setToX(1.1); // Scale 1.1 times on the X-axis
        scaleTransition.setToY(1.1); // Scale 1.1 times on the Y-axis
        scaleTransition.play(); // Play the transition
    }

    @FXML
    private void handleImagePressed(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        // Apply a smooth transition to scale the image to 1.05 when pressed
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), imageView);
        scaleTransition.setToX(1.05); // Scale 1.05 times on the X-axis
        scaleTransition.setToY(1.05); // Scale 1.05 times on the Y-axis
        scaleTransition.play(); // Play the transition
    }
}

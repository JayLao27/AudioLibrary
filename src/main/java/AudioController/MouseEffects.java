package AudioController;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MouseEffects {

    public static void addMouseEffects(Pane pane) {
        pane.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), pane);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();

            pane.setStyle("-fx-background-color: #d3d3d32b;");
        });

        pane.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), pane);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();

            pane.setStyle("-fx-background-color: #cfcfcf1b");
        });

        // Mouse pressed: scale slightly down and darken background
        pane.setOnMousePressed(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), pane);
            scaleTransition.setToX(1.01);
            scaleTransition.setToY(1.01);
            scaleTransition.play();

            // Darken background color
            pane.setStyle("-fx-background-color: #dfdfdf2b;");
        });

        pane.setOnMouseReleased(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), pane);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();

            // Restore hover background color
            pane.setStyle("-fx-background-color: #d3d3d32b;");
        });
    }

    public static void addMouseEffects(Button button) {
        button.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();
        });

        button.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();

        });

        button.setOnMousePressed(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), button);
            scaleTransition.setToX(1.01);
            scaleTransition.setToY(1.01);
            scaleTransition.play();
        });

        button.setOnMouseReleased(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), button);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();
        });
    }
}

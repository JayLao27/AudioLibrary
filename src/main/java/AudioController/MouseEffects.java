package AudioController;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


/**
 * Utility class that provides mouse effects for interactive elements such as {@link Pane} and {@link Button}.
 * The effects include scaling and background color changes on mouse events (enter, exit, press, release).
 */
public class MouseEffects {

    /**
     * Adds mouse effects (scale transition and background color change) to a given {@link Pane}.
     * Effects include:
     * - Scaling when mouse enters or exits
     * - Scaling and background color change when mouse is pressed or released
     *
     * @param pane the {@link Pane} to which mouse effects will be applied
     */
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


    /**
     * Adds mouse effects (scale transition) to a given {@link Button}.
     * Effects include:
     * - Scaling when mouse enters or exits
     * - Scaling when mouse is pressed or released
     *
     * @param button the {@link Button} to which mouse effects will be applied
     */
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

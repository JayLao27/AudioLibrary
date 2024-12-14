package AudioController;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Utility class that provides mouse interaction effects for UI elements like {@link Pane}, {@link Button},
 * and {@link ImageView}. The effects include scaling and background color changes on mouse events
 * (e.g., mouse enter, exit, press, and release).
 */
public class MouseEffects {

    /**
     * Adds mouse interaction effects to a given {@link Pane}.
     * <p>
     * Effects include:
     * <ul>
     *     <li>Scaling up when the mouse enters the pane.</li>
     *     <li>Scaling back to normal when the mouse exits the pane.</li>
     *     <li>Scaling slightly down and darkening the background color when the pane is pressed.</li>
     *     <li>Restoring the hover effect when the mouse is released.</li>
     * </ul>
     *
     * @param pane the {@link Pane} to which mouse effects will be applied.
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

            pane.setStyle("-fx-background-color: #cfcfcf1b;");
        });

        pane.setOnMousePressed(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), pane);
            scaleTransition.setToX(1.01);
            scaleTransition.setToY(1.01);
            scaleTransition.play();

            pane.setStyle("-fx-background-color: #dfdfdf2b;");
        });

        pane.setOnMouseReleased(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), pane);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();

            pane.setStyle("-fx-background-color: #d3d3d32b;");
        });
    }

    /**
     * Adds mouse interaction effects to a given {@link Button}.
     * <p>
     * Effects include:
     * <ul>
     *     <li>Scaling up when the mouse enters the button.</li>
     *     <li>Scaling back to normal when the mouse exits the button.</li>
     *     <li>Scaling slightly down when the button is pressed.</li>
     *     <li>Restoring the hover scale when the mouse is released.</li>
     * </ul>
     *
     * @param button the {@link Button} to which mouse effects will be applied.
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

    /**
     * Adds mouse interaction effects to a given {@link ImageView}.
     * <p>
     * Effects include:
     * <ul>
     *     <li>Scaling up when the mouse enters the image view.</li>
     *     <li>Scaling back to normal when the mouse exits the image view.</li>
     *     <li>Scaling slightly down when the image view is pressed.</li>
     *     <li>Restoring the hover scale when the mouse is released.</li>
     * </ul>
     *
     * @param imageView the {@link ImageView} to which mouse effects will be applied.
     */
    public static void addMouseEffects(ImageView imageView) {
        imageView.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), imageView);
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.play();
        });

        imageView.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), imageView);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });

        imageView.setOnMousePressed(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), imageView);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });

        imageView.setOnMouseReleased(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), imageView);
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.play();
        });
    }
}

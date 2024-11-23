package AudioController.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class CartScene {
    @FXML
    private Button checkoutButton;

    @FXML
    private Label totalamountLabel;

    @FXML
    public void initialize() {
        updateCheckoutButtonText();

        // Create and set Tooltip for checkoutButton
        Tooltip tooltip = new Tooltip("Click to complete your purchase");
        checkoutButton.setTooltip(tooltip);

    }

    //Button Function
    private void onCheckoutButtonClicked() {
        //Logic
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
    private void handleButtonReleased(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), button);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();
    }

    private int orderCount = 0;

    public void updateCheckoutButtonText() {
        checkoutButton.setText("Check Out (" + orderCount + ")");
    }

    public void addOrder() {
        orderCount++;
        updateCheckoutButtonText();
    }

    public void clearOrders() {
        orderCount = 0;
        updateCheckoutButtonText();
    }
}

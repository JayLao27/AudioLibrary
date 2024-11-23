package AudioController.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class CartScene {

    @FXML
    private Button checkoutButton;

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

    @FXML
    public void initialize() {
        updateCheckoutButtonText();

        // Create and set Tooltip for checkoutButton
        Tooltip tooltip = new Tooltip("Click to complete your purchase");
        checkoutButton.setTooltip(tooltip);

    }
}

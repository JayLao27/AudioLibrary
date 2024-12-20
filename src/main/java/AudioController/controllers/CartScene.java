package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.MouseEffects;
import AudioController.SceneWithHomeContext;
import AudioController.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.animation.ScaleTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Cart Scene in the application.
 * Manages the display of items in the user's cart and handles checkout functionality.
 * Implements the {@link SceneWithHomeContext} interface for managing navigation.
 */
public class CartScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    /**
     * Sets the {@link HomeScene} instance to enable navigation from the artist page.
     *
     * @param homeScene The home scene instance for navigation purposes.
     */
    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    @FXML
    private Button checkoutButton;
    @FXML
    private Label totalamountLabel;
    @FXML
    private VBox cartlistVBox;

    private int checkedItems = 0;
    private double totalAmount = 0.0;
    private List<Integer> checkedAudio = new ArrayList<>();

    /**
     * Initializes the CartScene by setting up tooltips and loading the cart list.
     */
    @FXML
    public void initialize() {

        Tooltip tooltip = new Tooltip("Click to proceed to checkout");
        checkoutButton.setTooltip(tooltip);

        loadCartList(UserSession.getInstance().getUserID());
    }

    /**
     * Loads the user's cart list from the database and populates the cart UI.
     *
     * @param userID The user ID to fetch the cart items for.
     */
    private void loadCartList(int userID) {
        cartlistVBox.getChildren().clear();

        String query = """
            SELECT ca.audioID
            FROM CartAudio ca
            INNER JOIN User u ON ca.userID = u.userID
            WHERE u.userID = ?
            ORDER BY ca.audioID ASC
            """;

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int audioID = rs.getInt("audioID");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/cartlisttemplateScene.fxml"));
                    AnchorPane cartItem = fxmlLoader.load();
                    CartListTemplateScene controller = fxmlLoader.getController();
                    controller.setAudioID(audioID, this);

                    MouseEffects.addMouseEffects(cartItem);

                    cartItem.setOnMouseClicked(event -> {
                        System.out.println("Redirecting to song...");

                        if (homeScene != null) {
                            homeScene.loadSongScene("/FXMLs/songpageScene.fxml", audioID);
                        } else {
                            System.out.println("HomeScene is null!");
                        }
                    });

                    cartlistVBox.getChildren().add(cartItem);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the checkout button click event.
     * If items are selected, redirects to the checkout page; otherwise, shows a warning.
     */
    @FXML
    private void handleCheckoutClicked() {
        if (checkedItems > 0) {
            CheckoutScene checkoutScene = null;
            if (homeScene != null) {
                checkoutScene = homeScene.loadScene("/FXMLs/checkoutScene.fxml");
            } else {
                System.out.println("HomeScene is null!");
            }
            checkoutScene.loadCheckoutList(checkedAudio);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Items Selected");
            alert.setHeaderText(null); // Optional, can be set to provide more context
            alert.setContentText("Please select at least one item to proceed with checkout.");

            // Show the alert and wait for the user to acknowledge
            alert.showAndWait();
        }
    }

    // Cart Logic
    public void incrementCheckoutDetails(int audioID, double price) {
        checkedAudio.add(audioID);
        totalAmount += price;
        checkedItems++;
        updateCheckoutDetails();
    }

    public void decrementCheckoutDetails(int audioID, double price) {
        checkedAudio.remove((Integer) audioID);
        totalAmount -= price;
        checkedItems--;
        updateCheckoutDetails();
    }

    public void updateCheckoutDetails() {
        totalamountLabel.setText(String.format("%.2f", totalAmount));
        checkoutButton.setText("Checkout(" + checkedItems + ")");
    }

    public void reloadCart() {
        loadCartList(UserSession.getInstance().getUserID());
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

    @FXML
    private void handleButtonReleased(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), button);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();
    }
}

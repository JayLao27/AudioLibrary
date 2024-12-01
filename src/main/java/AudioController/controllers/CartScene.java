package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.SceneWithHomeContext;
import AudioController.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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

public class CartScene implements SceneWithHomeContext {

    private HomeScene homeScene;

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

    private int audioID;

    @FXML
    public void initialize() {
        updateCheckoutButtonText();

        Tooltip tooltip = new Tooltip("Click to complete your purchase");
        checkoutButton.setTooltip(tooltip);

        loadCartList(UserSession.getInstance().getUserID());
    }

    private void loadCartList(int userID) {
        String query = """
            SELECT ca.audioID
            FROM CartAudio ca
            INNER JOIN Cart c ON ca.userID = c.userID
            WHERE c.userID = ?
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

                    CartlisttemplateScene controller = fxmlLoader.getController();
                    controller.setAudioID(audioID);

                    addMouseEffects(cartItem);

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

    // Button Function
    private void onCheckoutButtonClicked() {
        // Logic for checkout
    }

    private void addMouseEffects(Pane songList) {
        // Mouse effects for cart items
        songList.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), songList);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();

            songList.setStyle("-fx-background-color: #d3d3d32b;");
        });

        songList.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), songList);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();

            songList.setStyle("-fx-background-color: #cfcfcf1b");
        });

        songList.setOnMousePressed(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), songList);
            scaleTransition.setToX(1.01);
            scaleTransition.setToY(1.01);
            scaleTransition.play();

            songList.setStyle("-fx-background-color: #dfdfdf2b;");
        });

        songList.setOnMouseReleased(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), songList);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();

            songList.setStyle("-fx-background-color: #d3d3d32b;");
        });
    }

    // Button UX
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

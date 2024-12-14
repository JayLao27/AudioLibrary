package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.MouseEffects;
import AudioController.SceneWithHomeContext;
import AudioController.UserSession;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for the payment history scene. This class is responsible for displaying
 * the list of payments made by the user and allowing navigation to the details of each payment.
 */
public class PaymentHistoryScene implements SceneWithHomeContext {
    private HomeScene homeScene;

    @FXML
    private FlowPane paymentsFlowPane;

    /**
     * Sets the {@link HomeScene} instance to enable navigation from the artist page.
     *
     * @param homeScene The home scene instance for navigation purposes.
     */
    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    /**
     * Initializes the payment history scene by loading the list of payments made by the user.
     * This method is called when the scene is loaded and ready for interaction.
     */
    @FXML
    private void initialize() {
        loadPaymentsList();
    }

    /**
     * Loads the list of payments made by the current user and adds them to the payment history view.
     * Each payment is displayed as a list item that can be clicked to view more details.
     * The payments are fetched from the database based on the current user's ID.
     */
    private void loadPaymentsList() {
        int userID = UserSession.getInstance().getUserID();

        String query = "SELECT paymentID FROM Payments WHERE userID = ? ORDER BY paymentID DESC";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int paymentID = rs.getInt("paymentID");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/paymentslisttemplateScene.fxml"));
                    AnchorPane paymentsList = fxmlLoader.load();

                    PaymentsListTemplateScene controller = fxmlLoader.getController();
                    controller.setpaymentID(paymentID);

                    MouseEffects.addMouseEffects(paymentsList);

                    paymentsList.setOnMouseClicked(event -> {
                        System.out.println("Redirecting to song...");

                        if (homeScene != null) {
                            homeScene.loadPaymentScene("/FXMLs/paymentdetailsScene.fxml", paymentID);
                        } else {
                            System.out.println("HomeScene is null!");
                        }
                    });

                    // Add the song to the flow pane
                    paymentsFlowPane.getChildren().add(paymentsList);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

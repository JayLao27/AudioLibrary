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

public class PaymentHistoryScene implements SceneWithHomeContext {
    private HomeScene homeScene;

    @FXML
    private FlowPane paymentsFlowPane;

    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    @FXML
    private void initialize() {
        loadPaymentsList();
    }

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

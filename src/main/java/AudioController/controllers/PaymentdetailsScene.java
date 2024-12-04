package AudioController.controllers;

import AudioController.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentdetailsScene {

    @FXML
    private Label paymentIDLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label purchasedAudioLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private ListView<String> audioList;  // Specify the type for ListView

    private int paymentID;

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
        System.out.println("Initializing with paymentID: " + paymentID);
        loadPaymentDetails(paymentID);
    }

    // Method to populate the labels and ListView based on paymentID
    public void loadPaymentDetails(int paymentID) {
        String paymentQuery = "SELECT p.paymentID, u.firstName, u.lastName, p.amount, a.audioName, a.audioPrice " +
                "FROM Payments p " +
                "JOIN User u ON p.userID = u.userID " +
                "JOIN PaymentAudio pa ON p.paymentID = pa.paymentID " +
                "JOIN Audio a ON pa.audioID = a.audioID " +
                "WHERE p.paymentID = ?";

        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(paymentQuery)) {

            stmt.setInt(1, paymentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Populate the labels
                    paymentIDLabel.setText("Payment ID: " + rs.getInt("paymentID"));
                    firstNameLabel.setText("First Name: " + rs.getString("firstName"));
                    lastNameLabel.setText("Last Name: " + rs.getString("lastName"));
                    totalLabel.setText("Total Amount: $" + rs.getDouble("amount"));

                    // Add purchased audio to ListView
                    ObservableList<String> audioItems = FXCollections.observableArrayList();
                    do {
                        String audioInfo = rs.getString("audioName") + " - $" + rs.getDouble("audioPrice");
                        audioItems.add(audioInfo);
                    } while (rs.next());

                    // Set the audio items to the ListView
                    audioList.setItems(audioItems);
                    purchasedAudioLabel.setText("Purchased Audio:");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {}
}

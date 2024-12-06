package AudioController.controllers;

import AudioController.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class PaymentDetailsScene {

    @FXML
    private Label titleLabel, paymentIDLabel, firstNameLabel, lastNameLabel, purchasedAudioLabel, totalLabel;
    @FXML
    private ListView<String> audioList;

    private int paymentID;

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
        System.out.println("Initializing with paymentID: " + paymentID);
        loadPaymentDetails(paymentID);
    }

    public void loadPaymentDetails(int paymentID) {
        String paymentQuery = "SELECT p.paymentID, u.firstName, u.lastName, p.amount, p.paymentDate, a.audioName, a.audioPrice " +
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
                    Timestamp paymentDate = rs.getTimestamp("paymentDate");
                    String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(paymentDate);

                    titleLabel.setText("Payment History - " + formattedDate);
                    paymentIDLabel.setText("Payment ID: " + rs.getInt("paymentID"));
                    firstNameLabel.setText("First Name: " + rs.getString("firstName"));
                    lastNameLabel.setText("Last Name: " + rs.getString("lastName"));
                    totalLabel.setText("Total Amount: $" + rs.getDouble("amount"));

                    ObservableList<String> audioItems = FXCollections.observableArrayList();
                    do {
                        String audioInfo = rs.getString("audioName") + " - $" + rs.getDouble("audioPrice");
                        audioItems.add(audioInfo);
                    } while (rs.next());

                    audioList.setItems(audioItems);
                    purchasedAudioLabel.setText("Purchased Audio:");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

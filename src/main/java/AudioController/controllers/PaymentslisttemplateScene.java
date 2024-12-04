package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentslisttemplateScene {
    @FXML
    private Label paymentLabel;

    private int paymentID;

    public void setpaymentID(int paymentID) {
        this.paymentID = paymentID;
        loadPaymentDetails();
    }

    public void initialize() {}

    private void loadPaymentDetails() {
        String paymentDate = ResourceLoader.getPaymentDate(paymentID);

        if (paymentDate != null) {
            try {
                // Assuming the paymentDate is returned in a standard format, e.g., "yyyy-MM-dd"
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(paymentDate);

                // Format the date to the desired output format (e.g., "MMM dd, yyyy")
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
                String formattedDate = outputFormat.format(date);

                // Set the label text to "Purchase on: formattedDate"
                paymentLabel.setText("Purchase on: " + formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
                paymentLabel.setText("Error parsing date");
            }
        } else {
            paymentLabel.setText("Date not available");
        }
    }

}






package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentsListTemplateScene {
    @FXML
    private Label paymentLabel;

    private int paymentID;

    public void setpaymentID(int paymentID) {
        this.paymentID = paymentID;
        loadPaymentDetails();
    }

    public void initialize() {}

    private void loadPaymentDetails() {
        String paymentDateTime = ResourceLoader.getPaymentDate(paymentID);

        if (paymentDateTime != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = inputFormat.parse(paymentDateTime);

                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                String formattedDate = outputFormat.format(date);

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






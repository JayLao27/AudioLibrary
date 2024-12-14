package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller for the payment list template scene. This class is responsible for displaying
 * the payment date of a specific payment in the payment history view.
 */
public class PaymentsListTemplateScene {
    @FXML
    private Label paymentLabel;

    private int paymentID;

    /**
     * Sets the payment ID and loads the payment details.
     * This method is called when the payment ID is available and needs to be displayed.
     *
     * @param paymentID The ID of the payment whose details are to be displayed.
     */
    public void setpaymentID(int paymentID) {
        this.paymentID = paymentID;
        loadPaymentDetails();
    }

    public void initialize() {}

    /**
     * Loads and formats the payment date for the given payment ID, and updates the
     * paymentLabel with the formatted date. The date is retrieved from the `ResourceLoader`.
     * If the date is successfully retrieved and parsed, it is displayed in a readable format.
     * Otherwise, an error message is displayed.
     */
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






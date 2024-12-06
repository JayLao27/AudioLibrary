package AudioController.controllers;

import AudioController.ResourceLoader;
import AudioController.SceneWithHomeContext;
import AudioController.User;
import AudioController.UserSession;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.event.ActionEvent;

public class ProfileScene implements SceneWithHomeContext {
    private HomeScene homeScene;

    @FXML
    private FlowPane paymentsFlowPane;

    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }
    private ResourceLoader resourceLoader = new ResourceLoader();

    @FXML
    private Label usernameLabel;
    @FXML
    private Label firstnameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Label thankYouLabel;
    @FXML
    private Hyperlink viewhistoryLink;

    @FXML
    public void onViewPaymentClicked(javafx.event.ActionEvent actionEvent) {
        System.out.println("Button clicked!");
        if (homeScene != null) {
            homeScene.loadScene("/FXMLs/paymenthistoryScene.fxml");
        }
    }

    @FXML
    public void initialize() {
        setupLogoutTooltip();
        displayUserProfile(); // Ensure this method is called to display profile data
    }

    private void setupLogoutTooltip() {
        Tooltip tooltip = new Tooltip("Click to Logout");
        logoutButton.setTooltip(tooltip);
    }

    private void displayUserProfile() {
        int userID = UserSession.getInstance().getUserID();
        if (userID != 0) {
            User user = resourceLoader.getProfile(userID);
            if (user != null) {
                // Set user information in the labels
                usernameLabel.setText(user.getUserName());
                firstnameLabel.setText(user.getFirstName());
                lastnameLabel.setText(user.getLastName());
                emailLabel.setText(user.getEmail());
            } else {
                // If user not found in DB
                usernameLabel.setText("User not found.");
                firstnameLabel.setText("-");
                lastnameLabel.setText("-");
                emailLabel.setText("-");
            }
        } else {
            // No valid userID in the session
            usernameLabel.setText("No user logged in.");
            firstnameLabel.setText("-");
            lastnameLabel.setText("-");
            emailLabel.setText("-");
        }
    }

    @FXML
    private void logout(MouseEvent event) {
        displayThankYouMessage("Thank you for using the app!");
        UserSession.getInstance().clearSession(); // Clear user session
        loadLoginScene();
    }

    private void displayThankYouMessage(String message) {
        thankYouLabel.setText(message);
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

    private void scaleButton(Button button, double scale, int durationMillis) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(durationMillis), button);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.play();
    }

    private void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/loginScene.fxml"));
            Parent loginScene = loader.load();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.setScene(new Scene(loginScene));
            currentStage.show();
            System.out.println("Login Scene loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading loginScene.fxml");
        }
    }


}

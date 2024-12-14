package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.UserSession;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for the Login scene. This class manages the user login process,
 * including validating the username and password, transitioning to the home scene,
 * and handling button interactions with animations for a better user experience.
 */
public class LoginScene {

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink signupLink;

    /**
     * Handles the login action when the login button is pressed. It validates the username
     * and password by querying the database. If the credentials are valid, the user is logged in
     * and the home scene is displayed. Otherwise, an alert is shown indicating failure.
     *
     * @param event The action event triggered by clicking the login button.
     */
    public void login(ActionEvent event) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();

        try {
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, loginUsernameField.getText().trim());
            preparedStatement.setString(2, loginPasswordField.getText().trim());

            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                // Display an alert if login fails
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.showAndWait();
            } else {
                // Set user session and navigate to home scene
                int userID = result.getInt("userID");
                UserSession.getInstance().setUserID(userID);
                System.out.println("User logged in with ID: " + userID);

                Stage window = (Stage) loginButton.getScene().getWindow();
                window.close(); // Close the login window

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/homeScene.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Home Interface");
                stage.setScene(scene);
                stage.show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event on the signup hyperlink. It transitions the user to the signup scene.
     */
    @FXML
    private void onSignupLinkClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/signupScene.fxml"));
            Scene signupScene = new Scene(loader.load());

            Stage stage = (Stage) signupLink.getScene().getWindow();
            stage.setScene(signupScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Button UX
    @FXML
    private void handleButtonEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.play();
    }

    @FXML
    private void handleButtonExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.play();
    }

    @FXML
    private void handleButtonPressed(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), button);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.play();
    }

    @FXML
    private void handleButtonReleased(MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.play();
    }
}

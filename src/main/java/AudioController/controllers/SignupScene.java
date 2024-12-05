package AudioController.controllers;

import AudioController.DatabaseConnection;
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
import java.math.BigDecimal;
import java.sql.*;

public class SignupScene {

    @FXML
    private TextField signupFirstnameField;
    @FXML
    private TextField signupLastnameField;
    @FXML
    private TextField signupUsernameField;
    @FXML
    private TextField signupEmailField;
    @FXML
    private PasswordField signupPasswordField;
    @FXML
    private Button createAccountButton;
    @FXML
    private Hyperlink loginLink;

    @FXML
    private void onLoginLinkClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/loginScene.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void register(ActionEvent event) {
        DatabaseConnection db = new DatabaseConnection();

        String firstname = signupFirstnameField.getText().trim();
        String lastname = signupLastnameField.getText().trim();
        String username = signupUsernameField.getText().trim();
        String email = signupEmailField.getText().trim();
        String password = signupPasswordField.getText().trim();

        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            highlightEmptyFields();
            return;
        }

        Connection conn = null;
        PreparedStatement userStmt = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert user with a balance of 0.00 (or any default value you prefer)
            String userSql = "INSERT INTO User (firstName, lastName, userName, email, password, balance) VALUES (?, ?, ?, ?, ?, ?)";
            userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, firstname);
            userStmt.setString(2, lastname);
            userStmt.setString(3, username);
            userStmt.setString(4, email);
            userStmt.setString(5, password);
            userStmt.setBigDecimal(6, BigDecimal.ZERO); // Default balance of 0.00
            userStmt.executeUpdate();

            // Commit transaction
            conn.commit();
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (userStmt != null) userStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

    private void highlightEmptyFields() {
        if (signupFirstnameField.getText().trim().isEmpty()) {
            signupFirstnameField.setStyle("-fx-border-color: red;");
        }
        if (signupLastnameField.getText().trim().isEmpty()) {
            signupLastnameField.setStyle("-fx-border-color: red;");
        }
        if (signupUsernameField.getText().trim().isEmpty()) {
            signupUsernameField.setStyle("-fx-border-color: red;");
        }
        if (signupEmailField.getText().trim().isEmpty()) {
            signupEmailField.setStyle("-fx-border-color: red;");
        }
        if (signupPasswordField.getText().trim().isEmpty()) {
            signupPasswordField.setStyle("-fx-border-color: red;");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.play();
    }

}

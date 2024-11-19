package AudioController.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

        } else {

            try {

                Statement stmt = db.getConnection().createStatement();
                String sql = "INSERT INTO signup (firstname, lastname, username,email,password) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = db.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, firstname);
                preparedStatement.setString(2, lastname);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, password);
                preparedStatement.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
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

    private void navigateToLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/loginScene.fxml"));
            Scene loginScene = new Scene(loader.load());

            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

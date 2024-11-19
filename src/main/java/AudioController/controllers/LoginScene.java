package AudioController.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScene {

    @FXML
    private TextField loginUsernameField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signupLink;

    public void login(ActionEvent event) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();

        try {
            String sql = "SELECT * FROM Signup WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, loginUsernameField.getText().trim());
            preparedStatement.setString(2, loginPasswordField.getText().trim());

            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.showAndWait();
            } else {
                Stage window = (Stage) loginButton.getScene().getWindow();
                window.close();

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
}

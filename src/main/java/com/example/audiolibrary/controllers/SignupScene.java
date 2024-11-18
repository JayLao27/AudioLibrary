package com.example.audiolibrary.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
            Scene signupScene = new Scene(loader.load());

            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(signupScene);
            stage.show();

            System.out.println("wow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

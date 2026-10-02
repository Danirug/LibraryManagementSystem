package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    // Demo credentials (no database in this assignment)
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    @FXML private TextField txtUsername;
    @FXML private PasswordField pwdPassword;
    @FXML private Label lblError;

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = pwdPassword.getText();
        Validator.clearInvalid(txtUsername, pwdPassword);
        lblError.setText("");

        if (username.isEmpty() && password.isEmpty()) {
            Validator.markInvalid(txtUsername, pwdPassword);
            lblError.setText("Please enter your username and password.");
            return;
        }
        if (username.isEmpty()) {
            Validator.markInvalid(txtUsername);
            lblError.setText("Username is required.");
            return;
        }
        if (password.isEmpty()) {
            Validator.markInvalid(pwdPassword);
            lblError.setText("Password is required.");
            return;
        }

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            SceneManager.switchScene("Dashboard.fxml", "Library Management System - Dashboard");
        } else {
            lblError.setText("Invalid username or password.");
            AlertUtil.showError("Login Failed",
                    "The username or password you entered is incorrect. Please try again.");
            pwdPassword.clear();
            pwdPassword.requestFocus();
        }
    }

    @FXML
    private void handleClear() {
        txtUsername.clear();
        pwdPassword.clear();
        lblError.setText("");
        Validator.clearInvalid(txtUsername, pwdPassword);
        txtUsername.requestFocus();
    }
}
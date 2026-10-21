package com.library.controller;

import com.library.service.AuthService;
import com.library.util.AlertUtil;
import com.library.util.SceneManager;
import com.library.util.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField pwdPassword;
    @FXML private Label lblError;

    private final AuthService authService = new AuthService();

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

        boolean valid;
        try {
            valid = authService.login(username, password);
        } catch (SQLException e) {
            AlertUtil.showDatabaseError(e);
            return;
        }

        if (valid) {
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
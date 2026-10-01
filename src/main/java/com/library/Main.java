package com.library;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);
        SceneManager.switchScene("Login.fxml", "Library Management System - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
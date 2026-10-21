package com.library;

import com.library.db.DBConnection;
import com.library.util.AlertUtil;
import com.library.util.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Check the MySQL connection before showing any page
        try {
            DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            AlertUtil.showError("Database Connection Failed",
                    "Could not connect to MySQL. Make sure the server is running, library_db has been "
                            + "created, and the details in config/db.properties are correct.\n\n" + e.getMessage());
            Platform.exit();
            return;
        }

        SceneManager.init(stage);

        // Stops the window being dragged small enough to clip the pages
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        SceneManager.switchScene("Login.fxml", "Library Management System - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

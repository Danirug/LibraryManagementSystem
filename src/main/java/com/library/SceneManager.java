package com.library;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class SceneManager {

    private static final String VIEW_PATH = "/view/";   // = src/main/resources/view/
    private static Stage primaryStage;

    private SceneManager() { }

    public static void init(Stage stage) {
        primaryStage = stage;
    }

    /** Replaces the whole window content (Login <-> Dashboard). */
    public static void switchScene(String fxmlFile, String title) {
        Parent root = loadView(fxmlFile);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle(title);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
    }

    /** Loads an FXML file and returns its root node (pages shown inside the dashboard). */
    public static Parent loadView(String fxmlFile) {
        URL url = SceneManager.class.getResource(VIEW_PATH + fxmlFile);
        if (url == null) {
            throw new IllegalStateException("FXML file not found: " + VIEW_PATH + fxmlFile);
        }
        try {
            return FXMLLoader.load(url);
        } catch (IOException e) {
            throw new RuntimeException("Could not load " + fxmlFile, e);
        }
    }
}
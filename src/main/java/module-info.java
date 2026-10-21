module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // FXMLLoader needs reflective access to the controllers
    opens com.library.controller to javafx.fxml;
    // TableView's PropertyValueFactory reads the model getters by reflection
    opens com.library.model to javafx.base;

    exports com.library;
}

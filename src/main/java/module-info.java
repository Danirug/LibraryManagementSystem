module com.library {
    requires javafx.controls;
    requires javafx.fxml;

    // FXMLLoader (controllers) and TableView's PropertyValueFactory (model getters)
    // both need reflective access to this package
    opens com.library to javafx.fxml, javafx.base;

    exports com.library;
}

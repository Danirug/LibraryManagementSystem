module com.library {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.library to javafx.fxml;
    exports com.library;
}
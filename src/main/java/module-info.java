module com.example.audiolibrary {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.audiolibrary to javafx.fxml;
    opens com.example.audiolibrary.controllers to javafx.fxml;

    exports com.example.audiolibrary;
    exports com.example.audiolibrary.controllers;
}
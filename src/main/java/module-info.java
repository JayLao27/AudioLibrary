module com.example.audiolibrary {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens AudioController to javafx.fxml;
    opens AudioController.controllers to javafx.fxml;

    exports AudioController;
    exports AudioController.controllers;
}
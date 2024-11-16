module com.example.audiolibrary {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.audiolibrary to javafx.fxml;
    exports com.example.audiolibrary;
}
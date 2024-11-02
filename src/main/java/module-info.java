module com.example.server_parkinson {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.server_parkinson to javafx.fxml;
    exports com.example.server_parkinson;
}
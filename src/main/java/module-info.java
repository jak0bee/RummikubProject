module com.example.rumikub_demo {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.rumikub_demo to javafx.graphics, javafx.fxml;
}
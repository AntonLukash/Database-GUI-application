module com.example.lab5task1_3sem4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires json.simple;


    opens com.example.lab5task1_3sem4 to javafx.fxml;
    exports com.example.lab5task1_3sem4;
}
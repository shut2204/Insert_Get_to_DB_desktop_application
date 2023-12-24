module com.jester2204.best_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.jester2204.best_app to javafx.fxml;
    exports com.jester2204.best_app;
}
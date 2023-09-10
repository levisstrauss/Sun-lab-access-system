module com.levisstrauss.sunlabaccesssystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    opens com.levisstrauss.sunlabaccesssystem to javafx.fxml;
    exports com.levisstrauss.sunlabaccesssystem;
    exports com.levisstrauss.sunlabaccesssystem.Controllers;
    opens com.levisstrauss.sunlabaccesssystem.Controllers to javafx.fxml;
    exports com.levisstrauss.sunlabaccesssystem.Objects;
    opens com.levisstrauss.sunlabaccesssystem.Objects to javafx.fxml;
}
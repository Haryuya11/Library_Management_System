module com.library_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.library_management_system to javafx.fxml;
    exports com.library_management_system;
    exports com.library_management_system.Controller;
    exports com.library_management_system.Connection;
    exports com.library_management_system.Model;
    exports com.library_management_system.DAO;
    opens com.library_management_system.Controller to javafx.fxml;
}
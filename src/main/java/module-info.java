module com.example.tabinas_coolandnormal_login {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.tabinas_coolandnormal_login to javafx.fxml;
    exports com.example.tabinas_coolandnormal_login;
}
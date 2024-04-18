package com.example.tabinas_coolandnormal_login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    public Button registerButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all fields.");
            return;
        }

        try {
            if (registerUser(username, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Error", "Username already exists. Please choose a different one.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while accessing the database.");
            e.printStackTrace();
        }
    }

    private boolean registerUser(String username, String password) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String dbUser = "your_db_username";
        String dbPassword = "your_db_password";

        Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);

        String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setString(1, username);
        var resultSet = checkStatement.executeQuery();
        resultSet.next();

        int count = resultSet.getInt(1);
        if (count > 0) {
            return false;
        }

        String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, username);
        insertStatement.setString(2, password); // Ideally, you should hash the password using a secure hashing algorithm like bcrypt.

        int rowsInserted = insertStatement.executeUpdate();

        connection.close();

        return rowsInserted > 0;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

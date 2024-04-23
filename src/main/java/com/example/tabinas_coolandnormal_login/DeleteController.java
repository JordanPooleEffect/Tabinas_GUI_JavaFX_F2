package com.example.tabinas_coolandnormal_login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class DeleteController {
    @FXML
    public Button btnBack;
    @FXML
    public Button btnDeleteAccount;

    @FXML
    public void handleDashboard(ActionEvent event) {
        try {
            Parent dashboardView = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
            Scene scene = btnBack.getScene();
            scene.setRoot(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the Dashboard view.");
        }
    }

    @FXML
    public void handleDeleteAccount(ActionEvent event) {
        String user = HelloController.getCurrentUser();
        if (user == null || user.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Deletion Error", "No user is currently logged in.");
            return;
        }

        try {
            if (deleteUser(user)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account deleted successfully.");
                HelloController.clearCurrentUser();
                switchToLoginView();
            } else {
                showAlert(Alert.AlertType.ERROR, "Deletion Error", "Account could not be deleted.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error while accessing the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean deleteUser(String username) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void switchToLoginView() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            Scene scene = btnDeleteAccount.getScene();
            scene.setRoot(loginView);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the login view.");
        }
    }

}

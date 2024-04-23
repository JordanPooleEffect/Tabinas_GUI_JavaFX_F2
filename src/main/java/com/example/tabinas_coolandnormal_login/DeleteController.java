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
                showAlert(Alert.AlertType.INFORMATION, "Success", "Get Scammed, Account Is a GONER.");
                HelloController.clearCurrentUser();
                switchToLoginView();
            } else {
                showAlert(Alert.AlertType.ERROR, "Deletion Error", "Account could not be deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteUser(String username) throws SQLException {
        deleteAlarmsForUser(username);
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    private void deleteAlarmsForUser(String username) throws SQLException {
        int userId = AlarmClockController.getUserIdByUsername(username);
        if (userId != -1) {
            try (Connection connection = MySQLConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("DELETE FROM alarms WHERE user_id = ?")) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }
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
        }
    }

}

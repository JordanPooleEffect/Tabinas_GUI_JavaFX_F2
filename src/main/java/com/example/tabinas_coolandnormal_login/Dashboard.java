package com.example.tabinas_coolandnormal_login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

import static com.example.tabinas_coolandnormal_login.HelloController.clearCurrentUser;

public class Dashboard {

    public VBox pnDashboard;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnAlarmClock;
    public Button btnLogout;

    public void GoToCredentials(ActionEvent actionEvent) {
        try {
            Parent updateview = FXMLLoader.load(getClass().getResource("update-view.fxml"));
            Scene scene = btnUpdate.getScene();
            scene.setRoot(updateview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GoToDelete(ActionEvent actionEvent) {
        try {
            Parent deleteview = FXMLLoader.load(getClass().getResource("delete-view.fxml"));
            Scene scene = btnDelete.getScene();
            scene.setRoot(deleteview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GoToAlarmClock(ActionEvent actionEvent) {
        try {
            Parent alarmview = FXMLLoader.load(getClass().getResource("alarm-view.fxml"));
            Scene scene = btnAlarmClock.getScene();
            scene.setRoot(alarmview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Parent loginView = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("login-view.fxml")));
        Scene scene = btnLogout.getScene();
        scene.setRoot(loginView);
        clearCurrentUser();
    }
}

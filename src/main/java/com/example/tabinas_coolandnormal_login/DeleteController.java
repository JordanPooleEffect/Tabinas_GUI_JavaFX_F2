package com.example.tabinas_coolandnormal_login;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class DeleteController {
    public Button btnBack;

    public void handleDashboard(ActionEvent actionEvent) {
        try {
            Parent dashboardview1 = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
            Scene scene = btnBack.getScene();
            scene.setRoot(dashboardview1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


package com.example.tabinas_coolandnormal_login;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Dashboard {

    public VBox pnDashboard;
    public Button btnUpdate;
    public Button btnDelete;

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
}

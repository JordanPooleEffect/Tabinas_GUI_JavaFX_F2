package com.example.tabinas_coolandnormal_login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    public TextField fieldUsername;
    public TextField fieldPassword;
    public Button btnSignin;
    public Label MessageText;
    public Button btnLogout;
    public GridPane pnLogout;
    public ColorPicker cpPicker;
    @FXML
    private VBox pnLogin;
    private List<User> users;

    public HelloController() {
        users = new ArrayList<>();
        users.add(new User("zedric", "gwapo"));
        users.add(new User("derrick", "gamerdog03"));
        users.add(new User("kenneth", "coolandnormal"));
    }

    public void userLogin(MouseEvent mouseEvent) throws IOException {
        String username = fieldUsername.getText();
        String password = fieldPassword.getText();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println(username + " " + password);
                MessageText.setText("Successfully logged in!");
                AnchorPane p = (AnchorPane) pnLogin.getParent();
                p.getScene().getStylesheets().clear();
                Parent scene = FXMLLoader.load(HelloApplication.class.getResource("home-view.fxml"));
                p.getChildren().clear();
                p.getChildren().add(scene);
                return;
            }
        }
        MessageText.setText("SAYUP IMONG USER OR IMONG PASS UY");
    }

    public void userLogout(MouseEvent mouseEvent) throws IOException {
        AnchorPane L = (AnchorPane) pnLogout.getParent();
        Parent scene = FXMLLoader.load(HelloApplication.class.getResource("hello-view.fxml"));
        String color = cpPicker.getValue().toString().substring(2, cpPicker.getValue().toString().length() - 2);
        System.out.println(cpPicker.getValue());
        String temp = ".button {\n" + "\t-fx-background-color: #" + color + ";\n" + "\n" + "}";

            File cssFile = new File("src/main/resources/com/example/tabinas_coolandnormal_login/user1.css");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(cssFile))) {
                bw.write(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            L.getStylesheets().add(getClass().getResource("user1.css").toExternalForm());

            L.getChildren().clear();
            L.getChildren().add(scene);
    }

    private static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}

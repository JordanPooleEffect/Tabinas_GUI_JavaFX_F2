package com.example.tabinas_coolandnormal_login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HelloController {

    public GridPane pnLogout;
    public Button btnDarkMode;
    @FXML
    private TextField fieldUsername;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private Button btnSignin;
    @FXML
    private Label MessageText;
    @FXML
    private VBox pnLogin;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button changeColorButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button registerButton;

    private final Map<String, String> users = new HashMap<>();
    private final Map<String, String> userCssPaths = new HashMap<>();
    private String currentUser;
    private Scene currentScene;

    public HelloController() {
        initializeUsers();
    }

    @FXML
    private void initialize() {
        if (registerButton != null) {
            registerButton.setOnAction(event -> {
                handleRegister(event);
            });
        } else {
            System.err.println("Error: registerButton is null. Check if it is properly defined in the FXML file.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            Parent registerView = FXMLLoader.load(getClass().getResource("register-view.fxml"));
            Scene scene = registerButton.getScene();
            scene.setRoot(registerView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeUsers() {
        userCssPaths.put("cool", "cool.css");
        userCssPaths.put("and", "and.css");
        userCssPaths.put("normal", "normal.css");
        users.put("cool", "pass");
        users.put("and", "pass");
        users.put("normal", "pass");
    }

    @FXML
    private void userLogin(ActionEvent event) throws IOException {
        String username = fieldUsername.getText();
        String password = fieldPassword.getText();
        if (users.containsKey(username) && users.get(username).equals(password)) {
            currentUser = username;
            MessageText.setText("Successfully logged in!");
            applyUserCss(username);
            switchToMainScene();
        } else {
            MessageText.setText("Incorrect username or password.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Parent loginView = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("hello-view.fxml")));
        Scene scene = logoutButton.getScene();
        scene.setRoot(loginView);
        currentUser = null;
    }

    @FXML
    private void handleColorChange(ActionEvent event) {
        Color selectedColor = colorPicker.getValue();
        applyButtonColors(selectedColor);
        try (FileWriter writer = new FileWriter(currentUser + ".css")) {
            writer.write(String.format(".button {\n-fx-background-color: %s;\n}\n", toHexString(selectedColor)));
        } catch (IOException e) {
            System.err.println("Error saving color: " + e.getMessage());
        }
    }

    private void applyButtonColors(Color color) {
        String hexColor = toHexString(color);
        String style = "-fx-background-color: " + hexColor + ";";
        if (btnSignin != null) {
            btnSignin.setStyle(style);
        }
        if (changeColorButton != null) {
            changeColorButton.setStyle(style);
        }
        if (logoutButton != null) {
            logoutButton.setStyle(style);
        }
        if (btnDarkMode != null) {
            btnDarkMode.setStyle(style);
        }
    }

    private void applyUserCss(String username) {
        if (currentScene == null || userCssPaths == null) {
            return;
        }
        currentScene.getStylesheets().clear();
        String cssPath = userCssPaths.get(username);
        if (cssPath != null) {
            String cssURI = getClass().getResource("/" + cssPath).toExternalForm();
            currentScene.getStylesheets().add(cssURI);
        } else {
            System.err.println("CSS path not found for user: " + username);
        }
    }

    private void switchToMainScene() throws IOException {
        Parent mainView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-view.fxml")));
        currentScene = pnLogin.getScene();
        currentScene.setRoot(mainView);
    }

    private String toHexString(Color color) {
        return String.format("#%02x%02x%02x", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));
    }

    private boolean isDarkMode = false;

    public void toggleDarkMode(MouseEvent mouseEvent) {
        if (!isDarkMode) {
            pnLogout.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
            changeColorButton.setStyle("-fx-background-color: #555555; -fx-background-color: #333333; -fx-text-fill: white;");
            logoutButton.setStyle("-fx-background-color: #555555; -fx-background-color: #333333; -fx-text-fill: white;");
            btnDarkMode.setStyle("-fx-background-color: #555555; -fx-background-color: #333333; -fx-text-fill: white;");
            isDarkMode = true;
        } else {
            pnLogout.setStyle(null);
            changeColorButton.setStyle(null);
            logoutButton.setStyle(null);
            btnDarkMode.setStyle(null);
            isDarkMode = false;
        }
    }
}

package com.example.tabinas_coolandnormal_login;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.Optional;


import java.io.IOException;
import java.sql.*;

import static java.sql.Types.NULL;

public class AlarmClockController {
    public TextField hourField;
    public TextField minuteField;
    public Button btnBack;
    public Button btnRefresh;
    @FXML
    private TableView<ObservableList<Object>> alarmTable;
    @FXML
    private TableColumn<ObservableList<Object>, Object> idColumn;
    @FXML
    private TableColumn<ObservableList<Object>, Object> userIdColumn;
    @FXML
    private TableColumn<ObservableList<Object>, Object> timeColumn;
    @FXML
    private TableColumn<ObservableList<Object>, Void> actionColumn;

    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(0)));
        userIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(1)));
        timeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(2)));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");

            {
                deleteButton.setOnAction(event -> {
                    ObservableList<Object> rowData = getTableRow().getItem();
                    if (rowData != null) {
                        int id = (int) rowData.get(0);
                        deleteAlarm(id);
                        alarmTable.getItems().remove(rowData);
                    }
                });

                updateButton.setOnAction(event -> {
                    ObservableList<Object> rowData = getTableRow().getItem();
                    if (rowData != null) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Update Time");
                        dialog.setHeaderText("Enter new time (HH:MM)");

                        Optional<String> result = dialog.showAndWait();
                        result.ifPresent(newTime -> {

                        String[] timeParts = newTime.split(":");
                        int hour = Integer.parseInt(timeParts[0]);
                        int minute = Integer.parseInt(timeParts[1]);

                            if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
                                int id = (int) rowData.get(0);
                                String formattedTime = String.format("%02d:%02d:00", hour, minute);
                                updateAlarmTime(id, Time.valueOf(formattedTime));
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Invalid time format. Please enter time in HH:MM format.");
                                alert.showAndWait();
                            }
                        });
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton, updateButton);
                    setGraphic(buttons);
                }
            }
        });

        AlarmsTable();
    }

    @FXML
    private void setAlarm() {
        int hour = Integer.parseInt(hourField.getText());
        int minute = Integer.parseInt(minuteField.getText());
        String username = HelloController.getCurrentUser();
        int userId = getUserIdByUsername(username);

        long currentTimeMillis = System.currentTimeMillis();
        Time alarmTime = new Time(currentTimeMillis);
        alarmTime.setHours(hour);
        alarmTime.setMinutes(minute);
        createAlarm(userId, alarmTime);

        transactionOperation();

        try {
            Parent dashboardview = FXMLLoader.load(getClass().getResource("alarm-view.fxml"));
            Scene scene = btnBack.getScene();
            scene.setRoot(dashboardview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AlarmsTable() {
        String username = HelloController.getCurrentUser();
        int userId = getUserIdByUsername(username);

        if (userId != -1) {
            try (Connection c = MySQLConnection.getConnection();
                 PreparedStatement statement = c.prepareStatement("SELECT * FROM alarms WHERE user_id = ?");
            ) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        ObservableList<Object> row = FXCollections.observableArrayList();
                        row.add(resultSet.getInt("id"));
                        row.add(resultSet.getInt("user_id"));
                        row.add(resultSet.getTime("time"));
                        alarmTable.getItems().add(row);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createAlarm(int userId, Time time) {
        String query = "INSERT INTO alarms (user_id, time) VALUES (?, ?)";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setTime(2, time);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAlarmTime(int id, Time newTime) {
        String query = "UPDATE alarms SET time=? WHERE id=?";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setTime(1, newTime);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAlarm(int id) {
        String query = "DELETE FROM alarms WHERE id=?";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static int getUserIdByUsername(String username) {
        int userId = -1;
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public void toBack(ActionEvent actionEvent) {
        try {
            Parent dashboardview = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
            Scene scene = btnBack.getScene();
            scene.setRoot(dashboardview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toRefresh(ActionEvent actionEvent) {
        try {
            Parent dashboardview = FXMLLoader.load(getClass().getResource("alarm-view.fxml"));
            Scene scene = btnRefresh.getScene();
            scene.setRoot(dashboardview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transactionOperation() {
        Connection connection = null;
        try {
            connection = MySQLConnection.getConnection();
            connection.setAutoCommit(false);

            int alarmIdToUpdate = idToUpdate();
            Time newAlarmTime = newTimefromDB();

            updateAlarmTime(alarmIdToUpdate, newAlarmTime);

            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }

    private int idToUpdate() {
        int alarmId = -1;
        String username = HelloController.getCurrentUser();
        int userId = getUserIdByUsername(username);

        if (userId != -1) {
            try (Connection connection = MySQLConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT id FROM alarms WHERE user_id = ? LIMIT 1");
            ) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        alarmId = resultSet.getInt("id");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return alarmId;
    }

    private Time newTimefromDB() {
        Time newAlarmTime = null;
        String username = HelloController.getCurrentUser();
        int userId = getUserIdByUsername(username);

        if (userId != -1) {
            try (Connection connection = MySQLConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT time FROM alarms WHERE user_id = ? ORDER BY id DESC LIMIT 1");
            ) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        newAlarmTime = resultSet.getTime("time");
                    } else {
                        System.out.println("No alarm found for user with ID: " + userId);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User ID not found.");
        }
        return newAlarmTime;
    }
}

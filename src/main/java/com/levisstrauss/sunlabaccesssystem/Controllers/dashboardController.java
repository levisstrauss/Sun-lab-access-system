package com.levisstrauss.sunlabaccesssystem.Controllers;

import com.levisstrauss.sunlabaccesssystem.Database.DatabaseConnection;
import com.levisstrauss.sunlabaccesssystem.Main;
import com.levisstrauss.sunlabaccesssystem.Objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class dashboardController {
    @FXML
    private TableColumn<User, String> actionColumn;
    @FXML
    private Button logout;
    @FXML
    private TextField endTimeTextField;

    @FXML
    private TextField startTimeTextField;
    @FXML
    private TableColumn<User, String> dateColumn;
    @FXML
    private TableView<User> tableView;
    @FXML
    private DatePicker dateTime;
    @FXML
    private TableColumn<User, String> idColumn;
    @FXML
    private Button search;
    @FXML
    private TableColumn<User, String> timeColumn;
    @FXML
    private TextField userIdField;

    @FXML
    private TableColumn<User, String> userTypeColumn;
    @FXML
    void logoutOnActionBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleSearchButtonAction(ActionEvent event) {

        String userId = userIdField.getText().trim();

        LocalDate selectedDate = null;
        if (dateTime.getValue() != null) {
            selectedDate = dateTime.getValue();
            System.out.println(selectedDate);
        }

        String startTime = startTimeTextField.getText().trim();
        String endTime = endTimeTextField.getText().trim();

        // Check if the user entered a time range correctly
        if ((!startTime.isEmpty() && endTime.isEmpty()) || (startTime.isEmpty() && !endTime.isEmpty())) {
            // Display an error message to the user, you could use an Alert for this
            // Assuming you have some method showAlert to display alerts
            showAlert("Please enter both start and end times or leave both empty.");
            return;
        }

        ObservableList<User> users = fetchUsersBasedOnCriteria(userId, selectedDate, startTime, endTime);
        tableView.setItems(users);

    }

    @FXML
    void handleRefreshTableAction(ActionEvent event) {
        tableView.setItems(getAllUsers());
    }


    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        tableView.setItems(getAllUsers());
    }

    public ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connection = connectionNow.getConnection();
        String query = "SELECT * FROM user";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String userType = resultSet.getString("userType");
                String action = resultSet.getString("action");
                LocalDate date = LocalDate.parse(String.valueOf(resultSet.getDate("date")));
                LocalTime time = LocalTime.parse(String.valueOf(resultSet.getTime("time")));
                User user = new User(id, userType, action, date, time);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    private ObservableList<User> fetchUsersBasedOnCriteria(String userId, LocalDate date, String startTime, String endTime) {
        ObservableList<User> users = FXCollections.observableArrayList();
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connection = connectionNow.getConnection();
        StringBuilder query = new StringBuilder("SELECT * FROM user WHERE 1=1");

        // Adding conditions based on provided criteria
        if (!userId.isEmpty()) {
            query.append(" AND id = ?");
        }
        if (date != null) {
            query.append(" AND date = ?");
        }

        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            query.append(" AND time BETWEEN ? AND ?");
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());

            int index = 1;
            if (!userId.isEmpty()) {
                preparedStatement.setString(index++, userId);
            }
            if (date != null) {
                preparedStatement.setDate(index++, java.sql.Date.valueOf(date));
            }
            if (!startTime.isEmpty() && !endTime.isEmpty()) {
                preparedStatement.setString(index++, startTime);
                preparedStatement.setString(index++, endTime);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String userType = resultSet.getString("userType");
                String action = resultSet.getString("action");
                LocalDate retrievedDate = resultSet.getDate("date").toLocalDate();
                LocalTime time = resultSet.getTime("time").toLocalTime();
                User user = new User(id, userType, action, retrievedDate, time);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

}

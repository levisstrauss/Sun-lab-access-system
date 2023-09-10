
package com.levisstrauss.sunlabaccesssystem.Controllers;
        import com.levisstrauss.sunlabaccesssystem.Database.DatabaseConnection;
        import com.levisstrauss.sunlabaccesssystem.Main;
        import com.levisstrauss.sunlabaccesssystem.Objects.User;
        import javafx.collections.FXCollections;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.ComboBox;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextField;
        import javafx.stage.Stage;
        import java.io.IOException;
        import java.net.URL;
        import java.sql.*;
        import java.time.LocalDate;
        import java.time.LocalTime;
        import java.util.ResourceBundle;

public class userController implements Initializable {
    @FXML
    private Button adminNavigationBtn;
    @FXML
    private Label errorLabel;
    @FXML
    private ComboBox<String> userComboBoxAction;
    @FXML
    private ComboBox<String> userComboBoxType;
    @FXML
    private TextField userIdTextField;
    @FXML
    private Button userSwipeBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userComboBoxAction.setItems(FXCollections.observableArrayList("in", "out"));
        userComboBoxType.setItems(FXCollections.observableArrayList("students", "faculty members", "staff member", " janitors"));
    }
    @FXML
    void handleUserSwipeActionBtn(ActionEvent event) {
        String id = userIdTextField.getText();
        String action = userComboBoxAction.getSelectionModel().getSelectedItem();
        String userType =  userComboBoxType.getSelectionModel().getSelectedItem();
        LocalDate date = LocalDate.now();
        LocalTime timeNow = LocalTime.now();
        LocalTime time = LocalTime.of(timeNow.getHour(), 0);


        User user = new User(id, userType, action, date, time);

        insertUserIntoDatabase(user);
    }
    public void insertUserIntoDatabase(User user){
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connection = connectionNow.getConnection();
        String query = "INSERT INTO user (id, userType, action, date, time) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getUserType());
            preparedStatement.setString(3, user.getAction());
            preparedStatement.setDate(4, java.sql.Date.valueOf(user.getDate()));
            preparedStatement.setTime(5, java.sql.Time.valueOf(user.getTime()));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    void handleAdminNavigationBtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("ADMIN LOGIN");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
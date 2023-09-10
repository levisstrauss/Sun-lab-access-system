package com.levisstrauss.sunlabaccesssystem.Controllers;
import com.levisstrauss.sunlabaccesssystem.Database.DatabaseConnection;
import com.levisstrauss.sunlabaccesssystem.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class loginController {
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    void loginButtonOnAction(ActionEvent event) {
      if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
          validateLogin();
      } else {
          loginMessageLabel.setText("Please enter username and password");
      }
    }
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin(){
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectDB = connectionNow.getConnection();
        String verifyLogin = "SELECT count(1) FROM AuthorizedUsers WHERE username = '"
                + usernameTextField.getText()
                + "' AND password = '" + passwordTextField.getText() + "'";
        try {
            Statement  statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if (queryResult.getInt(1) == 1){
                    // Close the current window
                    Stage stage = (Stage) loginMessageLabel.getScene().getWindow();
                    stage.close();
                    // Open the dashboard window
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
                    Parent root = loader.load();
                    Stage dashboardStage = new Stage();
                    dashboardStage.setScene(new Scene(root));
                    dashboardStage.show();
                }else{
                    loginMessageLabel.setText("Invalid login: Please try again");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}

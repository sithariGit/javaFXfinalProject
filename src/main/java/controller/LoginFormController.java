package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.jasypt.util.text.BasicTextEncryptor;
import util.Role;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws SQLException, IOException {

        String key = "#379gj97Yu";

        // Create an instance of BasicTextEncryptor
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        // Use PreparedStatement to prevent SQL Injection
        String SQL="SELECT * FROM user WHERE email="+"'"+txtEmail.getText()+"'";

        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(SQL);

        // User found check
        if(resultSet.next()){
            User user = new User(
                    resultSet.getInt("user_id"),  // Fetching user_id as Integer
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role").toUpperCase())
            );

            if(basicTextEncryptor.decrypt(user.getPassword()).equals(txtPassword.getText())){
                Stage stage=new Stage();
                if(user.getRole()==Role.ADMIN) {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().
                            getResource("/view/dashBoard_admin_form.fxml"))));

                }else {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().
                            getResource("/view/dashBoard_employee_form.fxml"))));
                }
                stage.show();
                // Clear fields after successful login
                txtEmail.clear();
                txtPassword.clear();

                // Close login window
                ((Stage) txtEmail.getScene().getWindow()).close();

            }else {
                new Alert(Alert.AlertType.ERROR,"Incorrect Password").show();
            }

        }else {
            new Alert(Alert.AlertType.ERROR, "User Not Found.").show();
        }
    }

}

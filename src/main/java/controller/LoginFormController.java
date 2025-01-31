package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Employee;
import model.Role;
import model.User;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static model.Role.*;

public class LoginFormController {

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws SQLException, IOException {
        String key = "#376sd97B";
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        // Use PreparedStatement to prevent SQL Injection
        String SQL = "SELECT * FROM User WHERE email = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, txtEmail.getText());
        ResultSet resultSet = preparedStatement.executeQuery();

        // User found check
        if (resultSet.next()) {
            int userId = resultSet.getInt("user_id"); // Fetch user_id
            String email = resultSet.getString("email");
            String encryptedPassword = resultSet.getString("password");
            Role role = Role.valueOf(resultSet.getString("role").toUpperCase()); // Convert role correctly
            int employeeId = resultSet.getInt("employee_id"); // Get employee_id

            // Fetch the Employee details
            Employee employee = fetchEmployeeById(employeeId);

            // Create the User object
            User user = new User(userId, email, encryptedPassword, role, employee);

            // Validate password
            if (basicTextEncryptor.decrypt(user.getPassword()).equals(txtPassword.getText())) {
                Stage stage = new Stage();
                if (user.getRole() == Role.ADMIN) {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashBoard_admin_form.fxml"))));
                } else {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashBoard_employee_form.fxml"))));
                }
                stage.show();

                // Clear fields after successful login
                txtEmail.clear();
                txtPassword.clear();

                // Close login window
                ((Stage) txtEmail.getScene().getWindow()).close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Incorrect Password").show();
            }
            System.out.println(user);

        } else {
            new Alert(Alert.AlertType.ERROR, "User Not Found.").show();
        }
    }

    /**
     * Fetch Employee details based on employee_id.
     */
    private Employee fetchEmployeeById(int employeeId) throws SQLException {
        String SQL = "SELECT * FROM Employee WHERE employee_id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setInt(1, employeeId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new Employee(
                    resultSet.getInt("employee_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
            );
        }
        return null; // If employee not found, return null
    }
}

package controller.registerForm;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import model.Employee;
import model.User;
import org.jasypt.util.text.BasicTextEncryptor;

import java.sql.*;

public class UserRegisterFormController {

    @FXML
    private JFXPasswordField txtCPassword;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtRole;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    void btnRegisterOnAction(ActionEvent event) throws SQLException {
        String key = "#376sd97B"; // Encryption key

        // Create an instance of BasicTextEncryptor
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        // SQL query for inserting user and employee data
        String userSQL = "INSERT INTO user (email,password,role) VALUES (?,?,?)";
        String employeeSQL = "INSERT INTO employee (name,email) VALUES (?,?)"; // Fixed syntax error

        // Check if all required fields are filled
        if (txtUserName.getText().isEmpty() || txtEmail.getText().isEmpty() ||
                txtPassword.getText().isEmpty() || txtCPassword.getText().isEmpty() || txtRole.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields must be filled!").show();
            return; // Exit the method if any field is empty
        }

        // Check if the passwords match
        if (!txtPassword.getText().equals(txtCPassword.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match!").show();
            return; // Exit the method if passwords don't match
        }

        // Proceed with registration
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            // Check if the email already exists in the database
            PreparedStatement checkUser = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            checkUser.setString(1, txtEmail.getText());
            ResultSet resultSet = checkUser.executeQuery();

            if (!resultSet.next()) { // If no result, the email doesn't exist, proceed with the registration

                // Create a new employee
                PreparedStatement prTmEmployee =
                        connection.prepareStatement(employeeSQL, Statement.RETURN_GENERATED_KEYS);
                prTmEmployee.setString(1, txtUserName.getText());
                prTmEmployee.setString(2, txtEmail.getText());
                prTmEmployee.executeUpdate();

                // Retrieve the auto-generated employee_id
                ResultSet generatedKeys = prTmEmployee.getGeneratedKeys();
                int employeeId = 0;
                if (generatedKeys.next()) {
                    employeeId = generatedKeys.getInt(1);
                }

                // Create a new user linked to the employee
                PreparedStatement prTmUser = connection.prepareStatement(userSQL);
                prTmUser.setString(1, txtEmail.getText());
                prTmUser.setString(2, basicTextEncryptor.encrypt(txtPassword.getText())); // Encrypt the password
                prTmUser.setString(3, txtRole.getText());
                prTmUser.executeUpdate(); // Execute the query

                // Show success message
                new Alert(Alert.AlertType.CONFIRMATION, "Registration successful!").show();

                // Clear the form fields after successful registration
                txtUserName.clear();
                txtEmail.clear();
                txtPassword.clear();
                txtCPassword.clear();
                txtRole.clear();

            } else { // If the email exists, show an error message
                new Alert(Alert.AlertType.ERROR, "This email already exists. Please use a different email.").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Registration failed!").show();
        }
    }
}




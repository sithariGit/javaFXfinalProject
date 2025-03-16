package controller.registerForm;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        String key = "#379gj97Yu";

        // Create an instance of BasicTextEncryptor
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);


        // SQL query for inserting user
        String SQL = "INSERT INTO user (name, email, password, role) VALUES (?,?,?,?)";


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
        Statement statement = connection.createStatement();

        // Check if the email already exists in the database
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE email="
                + "'" + txtEmail.getText() + "'");

        if (!resultSet.next()) {

            PreparedStatement preparedStatement =
                    connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, txtUserName.getText());
            preparedStatement.setString(2, txtEmail.getText());
            preparedStatement.setString(3, basicTextEncryptor.encrypt(txtPassword.getText()));
            preparedStatement.setString(4, txtRole.getText());
            preparedStatement.executeUpdate();

            // Show success message
            new Alert(Alert.AlertType.CONFIRMATION, "Registration successful!").show();

            // Clear the form fields after successful registration
            txtUserName.clear();
            txtEmail.clear();
            txtPassword.clear();
            txtCPassword.clear();
            txtRole.clear();

        } else {// If the email exists, show an error message
            new Alert(Alert.AlertType.ERROR, "This email already exists. Please use a different email.").show();
        }

    }

}


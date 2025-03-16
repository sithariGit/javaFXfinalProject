package controller.employee;

import com.jfoenix.controls.JFXButton;
import controller.registerForm.UserRegisterImpl;
import dto.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    @FXML
    private JFXButton btnClose;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colRole;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableView<User> tblUsers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
     colUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
     colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
     colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        ObservableList<User> employeeList = FXCollections.observableArrayList();
        employeeList.addAll(new UserRegisterImpl().getAllUsers());
        tblUsers.setItems(employeeList);

    }
}

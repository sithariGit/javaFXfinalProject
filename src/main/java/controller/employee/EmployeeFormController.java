package controller.employee;

import com.jfoenix.controls.JFXTextField;
import model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class EmployeeFormController {

    @FXML
    private TableView<Employee> tblEmployees; // Corrected TableView name

    @FXML
    private TableColumn<Employee, Integer> colId; // Fixed generics

    @FXML
    private TableColumn<Employee, String> colName;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private JFXTextField txtName; // Removed txtId since ID is auto-generated

    @FXML
    private JFXTextField txtEmail;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String name = txtName.getText();
        String email = txtEmail.getText();

        if (name.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter both name and email.").show();
            return;
        }

        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);

        boolean isAdded = new EmployeeController().addEmployee(employee);
        if (isAdded) {
            loadTable();
            new Alert(Alert.AlertType.INFORMATION, "Successfully Added").show();

            // Clear the form fields after successful registration
            txtName.clear();
            txtEmail.clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Sorry, Try again...").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        // Get the selected employee from the TableView
        Employee selectedEmployee = tblEmployees.getSelectionModel().getSelectedItem(); // Fixed reference

        if (selectedEmployee != null) {
            int employeeId = Integer.parseInt(String.valueOf(selectedEmployee.getEmployeeId())); // Get the auto-generated ID

            try {
                boolean isDeleted = new EmployeeController().deleteEmployee(employeeId);
                if (isDeleted) {
                    loadTable(); // Refresh the table
                    new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Delete failed. Please try again.").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select an employee to delete.").show();
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        // Implement update functionality
    }

    public void loadTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

        new EmployeeController().getAllEmployees().forEach(employee -> {
            employeeObservableList.add(employee);
        });

        tblEmployees.setItems(employeeObservableList);
    }
}




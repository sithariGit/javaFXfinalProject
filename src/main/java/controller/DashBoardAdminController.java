package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class DashBoardAdminController {

    @FXML
    private AnchorPane loadContent; // This is where the content will be loaded dynamically.

    // Handle the "Employee Form" button click
    @FXML
    private void btnLoadEmployeeForm(ActionEvent event) throws IOException {
        loadForm("/view/employee_form.fxml"); // Load the Employee form
    }

    // Handle the "Product Form" button click
    @FXML
    private void btnLoadProductForm(ActionEvent event) throws IOException {
        loadForm("/view/product_form.fxml"); // Load the Product form
    }

    // Handle the "Order Form" button click
    @FXML
    private void btnLoadOrderForm(ActionEvent event) throws IOException {
        loadForm("/view/order_form.fxml"); // Load the Order form
    }

    // Handle the "Supplier Form" button click
    @FXML
    private void btnLoadSupplierForm(ActionEvent event) throws IOException {
        loadForm("/view/supplier_form.fxml"); // Load the Supplier form
    }

    // Handle the "User Register" button click
    @FXML
    private void btnUserRegisterForm(ActionEvent event) throws IOException {
        loadForm("/view/user_register_form.fxml"); // Load the User Register form
    }

    @FXML
    public void btnInventoryForm(ActionEvent actionEvent) throws IOException {
        loadForm("/view/inventory_form.fxml"); // Load the User Register form
    }

    // Utility method to load forms into the loadContent pane dynamically
    private void loadForm(String formPath) throws IOException {
        AnchorPane form = FXMLLoader.load(getClass().getResource(formPath));
        loadContent.getChildren().setAll(form); // Replace content of loadContent with the form
    }


}

package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashBoardAdminFormController {

    @FXML
    private AnchorPane loadContent;

    @FXML
    void btnInventoryForm(ActionEvent event) throws IOException {
        loadForm("/view/inventory_Form.fxml");
    }

    @FXML
    void btnLoadEmployeeForm(ActionEvent event) throws IOException {
        loadForm("/view/employee_form.fxml");
    }

    @FXML
    void btnLoadOrderForm(ActionEvent event) throws IOException {
        loadForm("/view/order_form.fxml");
    }

    @FXML
    void btnLoadProductForm(ActionEvent event) throws IOException {
        loadForm("/view/product_form.fxml");
    }

    @FXML
    void btnLoadSupplierForm(ActionEvent event) throws IOException {
        loadForm("/view/supplier_form.fxml");
    }

    @FXML
    void btnUserRegisterForm(ActionEvent event) throws IOException {
        loadForm("/view/register_form.fxml");
    }

    // Utility method to load forms into the loadContent pane dynamically
    private void loadForm(String formPath) throws IOException {
        AnchorPane form = FXMLLoader.load(getClass().getResource(formPath));
        loadContent.getChildren().setAll(form); // Replace content of loadContent with the form
    }

}

package controller.inventory;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class InventoryFormController {

    @FXML
    private JFXComboBox<?> cmbSupplier;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colProductId;

    @FXML
    private TableColumn<?, ?> colProductName;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colSupplier;

    @FXML
    private TableView<?> inventoryTable;

    @FXML
    private JFXTextField txtCategory;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtProductName;

    @FXML
    private JFXTextField txtQuantity;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtUpdateStock;

    @FXML
    void btnAddProductOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateStockOnAction(ActionEvent event) {

    }

}

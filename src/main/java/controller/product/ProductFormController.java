package controller.product;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProductFormController {

    @FXML
    private JFXComboBox<?> cmbCategory;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colProductId;

    @FXML
    private TableColumn<?, ?> colProductName;

    @FXML
    private TableColumn<?, ?> colStock;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableView<?> productTable;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtProductName;

    @FXML
    private JFXTextField txtSearchProduct;

    @FXML
    private JFXTextField txtStock;

    @FXML
    void btnAddProductOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteProductOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchProductOnAction(ActionEvent event) {

    }

}

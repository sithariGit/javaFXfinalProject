package controller.orderItem;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class OrderItemFormController {

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colProductId;

    @FXML
    private TableColumn<?, ?> colProductName;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colTotalPrice;

    @FXML
    private TableView<?> orderItemTable;

    @FXML
    private JFXTextField txtProductId;

    @FXML
    private JFXTextField txtProductName;

    @FXML
    private JFXTextField txtQuantity;

    @FXML
    private JFXTextField txtSearchOrderItem;

    @FXML
    private JFXTextField txtTotalPrice;

    @FXML
    void btnAddOrderItemOnAction(ActionEvent event) {

    }

    @FXML
    void btnClearFieldsOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOrderItemOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOrderItemOnAction(ActionEvent event) {

    }

}

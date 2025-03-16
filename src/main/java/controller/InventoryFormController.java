package controller;

import controller.product.ProductServiceImpl;
import dto.Product;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colBuyingPrice;

    @FXML
    private TableColumn<?, ?> colInStock;

    @FXML
    private TableColumn<Product, Integer> colProductId;

    @FXML
    private TableColumn<Product, String> colProductName;

    @FXML
    private TableColumn<?, ?> colSellingPrice;

    @FXML
    private TableColumn<Product, Integer> colSupplierId;

    @FXML
    private TableView<Product> tblInventory;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colSupplierId.setCellValueFactory(cellData ->
               new SimpleIntegerProperty(cellData.getValue().getSupplier().getSupplierId()).asObject());
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buyingUnitPrice"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingUnitPrice"));
        colInStock.setCellValueFactory(new PropertyValueFactory<>("inStock"));

        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        productObservableList.addAll(new ProductServiceImpl().getAllProducts());
        tblInventory.setItems(productObservableList);
    }

}

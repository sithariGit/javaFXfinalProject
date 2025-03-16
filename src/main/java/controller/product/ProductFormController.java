package controller.product;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.supplier.SupplierServiceImpl;
import dto.Product;
import dto.Supplier;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import util.Category;

import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    @FXML
    private JFXComboBox<Category> cmbCategory;

    @FXML
    private JFXComboBox<Integer> cmbSupplierID;

    @FXML
    private TableColumn<Product, String> colCategory;

    @FXML
    private TableColumn<Product, String> colDate;

    @FXML
    private TableColumn<Product, Integer> colProductId;

    @FXML
    private TableColumn<Product, String> colProductName;

    @FXML
    private TableColumn<Product, Integer> colQuantity;

    @FXML
    private TableColumn<Product, Integer> colSupplierId;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<Product> tblProductList;

    @FXML
    private JFXTextField txtBuyingPrice;

    @FXML
    private JFXTextField txtBuyingQuantity;

    @FXML
    private JFXTextField txtProductName;

    @FXML
    private JFXTextField txtSellingPrice;

    @FXML
    void btnAddProductOnAction(ActionEvent event) {
        try {
            Product newProduct = new Product();
            newProduct.setName(txtProductName.getText());
            newProduct.setCategory(cmbCategory.getValue());
            newProduct.setBuyingUnitPrice(new BigDecimal(txtBuyingPrice.getText()));
            newProduct.setSellingUnitPrice(new BigDecimal(txtSellingPrice.getText()));
            newProduct.setBuyingQuantity(Integer.parseInt(txtBuyingQuantity.getText()));
            newProduct.setInStock(Integer.parseInt(txtBuyingQuantity.getText()));

            // Convert date format
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date parsedDate = inputFormat.parse(lblDate.getText());
            newProduct.setDate(java.sql.Date.valueOf(outputFormat.format(parsedDate)));

            // Assign selected supplier
            Integer selectedSupplierId = cmbSupplierID.getValue();
            if (selectedSupplierId != null) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(selectedSupplierId);
                newProduct.setSupplier(supplier);
            }

            boolean isAdded = new ProductServiceImpl().addProduct(newProduct);
            if (isAdded) {
                System.out.println("Product added successfully!");
                loadProductTable();
                clearFields();
            } else {
                System.out.println("Failed to add product.");
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format in price or quantity fields.").show();
        } catch (ParseException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format.").show();
        }
    }


    private void clearFields() {
        txtProductName.clear();
        txtBuyingPrice.clear();
        txtSellingPrice.clear();
        txtBuyingQuantity.clear();
    }

    private void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        lblDate.setText(dateFormat.format(new Date()));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    LocalTime now = LocalTime.now();
                    lblTime.setText(String.format("%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
                }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadCategoryComboBox() {
        ObservableList<Category> categories = FXCollections.observableArrayList(Category.values());
        cmbCategory.setItems(categories);
    }

    private void loadProductTable() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colSupplierId.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getSupplier().getSupplierId()).asObject()
        );
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("buyingQuantity"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        productObservableList.addAll(new ProductServiceImpl().getAllProducts());
        tblProductList.setItems(productObservableList);
    }

    private void loadSupplierIds() {
        SupplierServiceImpl supplierServiceImpl = new SupplierServiceImpl();
        ObservableList<Integer> supplierIds = FXCollections.observableArrayList();
        List<Supplier> suppliers = supplierServiceImpl.getAllSuppliers();

        for (Supplier supplier : suppliers) {
            supplierIds.add(supplier.getSupplierId());
        }

        cmbSupplierID.setItems(supplierIds);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProductTable();
        setDateAndTime();
        loadSupplierIds();
        loadCategoryComboBox();
    }

    @FXML
    void btnUpdateProductOnAction(ActionEvent event) {
        Product selectedProduct = tblProductList.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            try {
                // Validate input fields
                if (txtProductName.getText().isEmpty() ||
                        txtBuyingPrice.getText().isEmpty() ||
                        txtSellingPrice.getText().isEmpty() ||
                        txtBuyingQuantity.getText().isEmpty() ||
                        cmbCategory.getValue() == null ||
                        cmbSupplierID.getValue() == null) {

                    new Alert(Alert.AlertType.ERROR, "Please fill in all fields before updating.").show();
                    return;
                }

                // Convert price and quantity safely
                BigDecimal buyingPrice;
                BigDecimal sellingPrice;
                int buyingQuantity;

                try {
                    buyingPrice = new BigDecimal(txtBuyingPrice.getText().trim());
                    sellingPrice = new BigDecimal(txtSellingPrice.getText().trim());
                    buyingQuantity = Integer.parseInt(txtBuyingQuantity.getText().trim());
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid number format! Enter correct price and quantity values.").show();
                    return;
                }

                // Set updated product details
                selectedProduct.setName(txtProductName.getText().trim());
                selectedProduct.setCategory(cmbCategory.getValue());
                selectedProduct.setBuyingUnitPrice(buyingPrice);
                selectedProduct.setSellingUnitPrice(sellingPrice);
                selectedProduct.setBuyingQuantity(buyingQuantity);
                selectedProduct.setInStock(buyingQuantity); // Update stock

                // Assign selected supplier
                Integer selectedSupplierId = cmbSupplierID.getValue();
                Supplier supplier = new Supplier();
                supplier.setSupplierId(selectedSupplierId);
                selectedProduct.setSupplier(supplier);

                // Parse and update the date
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date parsedDate = inputFormat.parse(lblDate.getText());
                    selectedProduct.setDate(java.sql.Date.valueOf(outputFormat.format(parsedDate)));

                } catch (ParseException e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid date format!").show();
                    return;
                }

                // Call the update method in service
                boolean isUpdated = new ProductServiceImpl().updateProduct(selectedProduct);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Product updated successfully!").show();
                    loadProductTable();  // Refresh table
                    clearFields();       // Clear input fields
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update product.").show();
                }

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            }

        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a product to update.").show();
        }
    }


    @FXML
    public void btnSelectOnAction(ActionEvent actionEvent) {
        Product selectedProduct = tblProductList.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            txtProductName.setText(selectedProduct.getName());
            cmbCategory.setValue(selectedProduct.getCategory());
            txtBuyingPrice.setText(String.valueOf(selectedProduct.getBuyingUnitPrice()));
            txtBuyingQuantity.setText(String.valueOf(selectedProduct.getBuyingQuantity()));
            txtSellingPrice.setText(String.valueOf(selectedProduct.getSellingUnitPrice()));

            // Set supplier ID ComboBox value
            if (selectedProduct.getSupplier() != null) {
                cmbSupplierID.setValue(selectedProduct.getSupplier().getSupplierId());
            }

            // Store selected product details (optional)
            txtProductName.setUserData(selectedProduct);

        } else {
            new Alert(Alert.AlertType.ERROR, "No product selected.").show();
        }
    }
}

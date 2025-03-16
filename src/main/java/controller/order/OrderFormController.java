package controller.order;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerServiceImpl;
import controller.product.ProductServiceImpl;
import controller.registerForm.UserRegisterImpl;
import db.DBConnection;
import dto.*;
import dto.tm.AddToCart;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import util.PaymentType;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {

    @FXML
    private JFXComboBox<PaymentType> cmbPaymentType;

    @FXML
    private JFXComboBox<Integer> cmbProduct;

    @FXML
    private JFXComboBox<Integer> cmbUserId;

    @FXML
    private TableColumn<?, ?> colProductId;

    @FXML
    private TableColumn<?, ?> colProductName;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colTotalCost;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNetPrice;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<AddToCart> tblOrderList;

    @FXML
    private JFXTextField txtCustomerEmail;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtQuantity;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXTextField txtUnitPrice;

    ObservableList<AddToCart> addItemToCart = FXCollections.observableArrayList();

    @FXML
    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        Integer productId = cmbProduct.getSelectionModel().getSelectedItem();
        if (productId == null) return;

        Product product = new ProductServiceImpl().searchProduct(productId);
        if (product == null) return;

        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText().trim());
        Integer quantity = Integer.parseInt(txtQuantity.getText().trim());
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

        addItemToCart.add(new AddToCart(productId, product.getName(), unitPrice, quantity, totalPrice));
        tblOrderList.setItems(addItemToCart);
        calNetTotal();
    }

    private void calNetTotal() {
        Double netTotal = 0.0;

        for (AddToCart tm : addItemToCart) {
            netTotal += tm.getTotalPrice().doubleValue();
        }

        lblNetPrice.setText(netTotal.toString());
    }

    private void setDateAndTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(date);
        lblDate.setText(format);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    LocalTime now = LocalTime.now();
                    lblTime.setText(now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
                }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadPaymentTypeComboBox() {
        ObservableList<PaymentType> paymentTypes = FXCollections.observableArrayList(PaymentType.values());
        cmbPaymentType.setItems(paymentTypes);
    }

    private void loadUserIds() {
        cmbUserId.setItems(new UserRegisterImpl().getUserById());
    }

    private void loadProductIds() {
        cmbProduct.setItems(new ProductServiceImpl().getProductById());
    }

    private void searchProductDetails(Integer productId) {
        Product product = new ProductServiceImpl().searchProduct(productId);

        txtStock.setText(String.valueOf(product.getInStock()));
        txtUnitPrice.setText(String.valueOf(product.getSellingUnitPrice()));
    }

    @FXML
    void btnCommitOnAction(ActionEvent event) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.commit();
    }

    @FXML
    void btnDeleteOrderOnAction(ActionEvent event) {
        AddToCart selectedItem = tblOrderList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            addItemToCart.remove(selectedItem);
            tblOrderList.setItems(addItemToCart);
            calNetTotal();
        }
    }

    @FXML
    boolean btnPlaceOrderOnAction(ActionEvent event) {
        boolean transactionSuccess = false;

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            if (connection == null || connection.isClosed()) {
                new Alert(Alert.AlertType.ERROR, "Failed to establish a database connection.").show();
                return false;
            }
            // Log connection status
            System.out.println("Connection is closed: " + connection.isClosed());
            System.out.println("Auto commit mode: " + connection.getAutoCommit());

            connection.setAutoCommit(false); // Disable auto-commit mode

            String name = txtCustomerName.getText();
            String email = txtCustomerEmail.getText();
            Double netPrice = Double.valueOf(lblNetPrice.getText().trim());
            PaymentType paymentType = cmbPaymentType.getValue();
            Integer userId = cmbUserId.getValue();
            Integer productId = cmbProduct.getValue();

            String orderQuantityText = txtQuantity.getText().trim();
            if (orderQuantityText.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Quantity field is empty! Please enter a valid number.").show();
                connection.rollback(); // Rollback transaction on error
                return false;
            }

            Integer orderQuantityInt = Integer.parseInt(orderQuantityText);

            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);

            CustomerServiceImpl customerService = new CustomerServiceImpl();
            boolean customerInserted = customerService.addOrUpdateCustomer(customer, connection);

            if (!customerInserted) {
                new Alert(Alert.AlertType.ERROR, "Customer already exists or failed to insert.").show();
                connection.rollback(); // Rollback transaction on error
                return false;
            }

            User user = new UserRegisterImpl().getUserById(userId);
            if (user == null) {
                new Alert(Alert.AlertType.ERROR, "Invalid User ID!").show();
                connection.rollback(); // Rollback transaction on error
                return false;
            }

            Orders order = new Orders();
            order.setNetPrice(BigDecimal.valueOf(netPrice));
            order.setPaymentType(paymentType);
            order.setOrderDate(new Date());
            order.setUser(user);
            order.setCustomer(customer);

            boolean orderInserted = new OrderService().addOrder(order, connection);
            if (orderInserted) {
                for (AddToCart item : addItemToCart) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setOrderQuantity(item.getQuantity());

                    Product product = new ProductServiceImpl().getProductId(item.getProductId());
                    if (product != null) {
                        orderItem.setProduct(product);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Product not found for ID: " + item.getProductId()).show();
                        connection.rollback(); // Rollback transaction on error
                        return false;
                    }

                    OrderItemService orderItemService = new OrderItemService();
                    boolean orderItemInserted = orderItemService.addOrderItem(orderItem, connection);
                    if (!orderItemInserted) {
                        new Alert(Alert.AlertType.ERROR, "Failed to insert order item for product ID: " + item.getProductId()).show();
                        connection.rollback(); // Rollback transaction on error
                        return false;
                    }

                    boolean isUpdateStock = new ProductServiceImpl().updateStock(orderItem, connection);
                    if (!isUpdateStock) {
                        new Alert(Alert.AlertType.ERROR, "Failed to update stock for product ID: " + item.getProductId()).show();
                        connection.rollback(); // Rollback transaction on error
                        return false;
                    }

                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to insert order.").show();
                connection.rollback(); // Rollback transaction on error
                return false;
            }

            connection.commit(); // Commit transaction
            transactionSuccess = true;
            new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!").show();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to place order: " + e.getMessage()).show();
        }

        return transactionSuccess;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        tblOrderList.setItems(addItemToCart);

        cmbProduct.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                searchProductDetails(Integer.valueOf(newValue.toString()));
            }
        });

        setDateAndTime();
        loadUserIds();
        loadPaymentTypeComboBox();
        loadProductIds();
    }
}
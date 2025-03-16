package controller.supplier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dto.Supplier;
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

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {
    @FXML
    public Label lblTime;

    @FXML
    public Label lblDate;

    @FXML
    private JFXButton btnAddSupplier;

    @FXML
    private JFXButton btnClearFields;

    @FXML
    private JFXButton btnDeleteSupplier;

    @FXML
    private JFXButton btnUpdateSupplier;

    @FXML
    private TableColumn<?, ?> colCompanyName;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableColumn<?, ?> colSupplierName;

    @FXML
    private TableView<Supplier> tblSuppliers;

    @FXML
    private JFXTextField txtCompanyName;

    @FXML
    private JFXTextField txtSupplierEmail;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    void btnAddSupplierOnAction(ActionEvent event) {
        String name =txtSupplierName.getText();
        String email=txtSupplierEmail.getText();
        String companyName=txtCompanyName.getText();

        if (name.isEmpty() || email.isEmpty() || companyName.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Incomplete Filling").show();
            return;
        }

        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setEmail(email);
        supplier.setCompanyName(companyName);

        boolean isAdded=new SupplierServiceImpl().addSupplier(supplier);
        if(isAdded){
            loadSupplierTable();
            new Alert(Alert.AlertType.INFORMATION, "Successfully Added").show();

            // Clear the form fields after successful registration
            txtSupplierName.clear();
            txtSupplierEmail.clear();
            txtCompanyName.clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Sorry, Try again...").show();
        }
    }

    @FXML
    void btnClearFieldsOnAction(ActionEvent event) {
        txtSupplierName.clear();
        txtSupplierEmail.clear();
        txtCompanyName.clear();
    }

    @FXML
    void btnDeleteSupplierOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateSupplierOnAction(ActionEvent event) {
        // Retrieve Supplier ID from stored data
        Integer supplierId = (Integer) txtSupplierName.getUserData();

        if (supplierId == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a supplier first!").show();
            return;
        }

        String name = txtSupplierName.getText();
        String email = txtSupplierEmail.getText();
        String companyName = txtCompanyName.getText();

        if (name.isEmpty() || email.isEmpty() || companyName.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Incomplete Filling").show();
            return;
        }

        Supplier selectedSupplier = new Supplier();
        selectedSupplier.setSupplierId(supplierId);
        selectedSupplier.setName(name);
        selectedSupplier.setEmail(email);
        selectedSupplier.setCompanyName(companyName);

        boolean isUpdated=new SupplierServiceImpl().updateSupplier(selectedSupplier);
            if(isUpdated){
                loadSupplierTable();
                new Alert(Alert.AlertType.INFORMATION, "Successfully Updated").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Sorry, Try again...").show();
            }
    }

    public void loadSupplierTable(){
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));

        ObservableList<Supplier> supplierObservableList= FXCollections.observableArrayList();

        new SupplierServiceImpl().getAllSuppliers().forEach(supplier -> {
            supplierObservableList.add(supplier);
        });

        tblSuppliers.setItems(supplierObservableList);
    }

    private void setDateAndTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = dateFormat.format(date);
        lblDate.setText(format);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e->{
                    LocalTime now=LocalTime.now();
                    lblTime.setText(now.getHour()+":"+now.getMinute()+":"+ now.getSecond());
                }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      setDateAndTime();
      loadSupplierTable();
    }

    @FXML
    public void btnSelectOnAction(ActionEvent actionEvent) {
        Supplier selectedSupplier = tblSuppliers.getSelectionModel().getSelectedItem();

        if (selectedSupplier != null) {
            txtSupplierName.setText(selectedSupplier.getName());
            txtSupplierEmail.setText(selectedSupplier.getEmail());
            txtCompanyName.setText(selectedSupplier.getCompanyName());

            // Store the Supplier ID in a hidden field or variable
            txtSupplierName.setUserData(selectedSupplier.getSupplierId()); // Store ID in UserData
        } else {
            new Alert(Alert.AlertType.ERROR, "Not SelectedSupplier.").show();
        }
    }
}

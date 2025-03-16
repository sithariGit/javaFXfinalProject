package controller.product;

import controller.supplier.SupplierServiceImpl;
import db.DBConnection;
import dto.OrderItem;
import dto.Product;
import dto.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import util.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    @Override
    public boolean addProduct(Product product) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String SQL = "INSERT INTO product (name, category, buying_unit_price, selling_unit_price, buying_quantity, in_stock, date, supplier_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            var preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getCategory().name());
            preparedStatement.setBigDecimal(3, product.getBuyingUnitPrice());
            preparedStatement.setBigDecimal(4, product.getSellingUnitPrice());
            preparedStatement.setInt(5, product.getBuyingQuantity());
            preparedStatement.setInt(6, product.getInStock());
            preparedStatement.setDate(7, java.sql.Date.valueOf(String.valueOf(product.getDate())));
            preparedStatement.setInt(8, product.getSupplier().getSupplierId());

            int rowsAffected = preparedStatement.executeUpdate();
            new Alert(Alert.AlertType.CONFIRMATION,"✅ Product added successfully").show();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"❌ Add Product failed. No rows affected.").show();
            return false;
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        String query = "UPDATE product SET name = ?, category = ?, buying_unit_price = ?, " +
                "selling_unit_price = ?, buying_quantity = ?, in_stock = ?, supplier_id = ?, date = ? " +
                "WHERE product_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory().toString());
            pstmt.setBigDecimal(3, product.getBuyingUnitPrice());
            pstmt.setBigDecimal(4, product.getSellingUnitPrice());
            pstmt.setInt(5, product.getBuyingQuantity());
            pstmt.setInt(6, product.getInStock());
            pstmt.setInt(7, product.getSupplier().getSupplierId());
            pstmt.setDate(8, new java.sql.Date(product.getDate().getTime())); // Ensure to set the date
            pstmt.setInt(9, product.getProductId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Product updated successfully: " + product.getProductId());
                return true;
            } else {
                System.out.println("❌ Update failed. No rows affected.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ SQL Error: " + e.getMessage());
            return false;
        }

    }

    @Override
    public Product searchProduct(Integer id) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            if (connection != null && connection.isValid(2)) {
                ResultSet resultSet = connection.createStatement().executeQuery(
                        "SELECT * FROM product WHERE product_id=" + id
                );

                if (resultSet.next()) {
                    LocalDate localDate = resultSet.getDate("date").toLocalDate();
                    int supplierId = resultSet.getInt("supplier_id");
                    Supplier supplier = new SupplierServiceImpl().getSupplierById(supplierId);

                    return new Product(
                            resultSet.getInt("product_id"),
                            resultSet.getString("name"),
                            Category.valueOf(resultSet.getString("category").toUpperCase()),
                            resultSet.getBigDecimal("buying_unit_price"),
                            resultSet.getBigDecimal("selling_unit_price"),
                            resultSet.getInt("buying_quantity"),
                            resultSet.getInt("in_stock"),
                            resultSet.getDate("date"),
                            supplier
                    );
                }
            } else {
                throw new SQLException("Connection is not valid.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching product: " + e.getMessage(), e);
        }
        return null;
    }

    public ObservableList<Integer> getProductById() {
        List<Product> productList = getAllProducts();
        ObservableList<Integer> productIdList = FXCollections.observableArrayList();

        productList.forEach(product -> {
            productIdList.add(product.getProductId());
        });

        return productIdList;
    }

    @Override
    public boolean deleteProduct(Integer id) {
        return false;
    }

    public Product getProductId(Integer productId) {
        if (productId == null) {
            return null;
        }

        String query = "SELECT * FROM product p JOIN supplier s ON p.supplier_id = s.supplier_id WHERE p.product_id = ?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            if (connection == null || connection.isClosed()) {
                throw new RuntimeException("Database connection is closed.");
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("s.name"),
                        resultSet.getString("s.email"),
                        resultSet.getString("s.company_name")
                );

                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("p.name"),
                        Category.valueOf(resultSet.getString("category").toUpperCase()),
                        resultSet.getBigDecimal("buying_unit_price"),
                        resultSet.getBigDecimal("selling_unit_price"),
                        resultSet.getInt("buying_quantity"),
                        resultSet.getInt("in_stock"),
                        resultSet.getDate("date"),
                        supplier
                );

                resultSet.close();
                preparedStatement.close();
                return product;
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product by ID: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean updateStock(OrderItem orderItem, Connection connection) throws SQLException {
        String query = "UPDATE product SET in_stock = in_stock - ? WHERE product_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderItem.getOrderQuantity());
            stmt.setInt(2, orderItem.getProduct().getProductId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Failed to update stock for product ID: " + orderItem.getProduct().getProductId());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error while updating stock for product ID: " + orderItem.getProduct().getProductId());
            throw new SQLException("Error while updating stock for product ID: " + orderItem.getProduct().getProductId(), e);
        }

        return true;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Database connection is closed.");
            }

            String SQL = "SELECT * FROM product p JOIN supplier s ON p.supplier_id = s.supplier_id";
            ResultSet resultSet = connection.createStatement().executeQuery(SQL);

            while (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("company_name")
                );

                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        Category.valueOf(resultSet.getString("category").toUpperCase()),
                        resultSet.getBigDecimal("buying_unit_price"),
                        resultSet.getBigDecimal("selling_unit_price"),
                        resultSet.getInt("buying_quantity"),
                        resultSet.getInt("in_stock"),
                        resultSet.getDate("date"),
                        supplier
                );
                products.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching products: " + e.getMessage(), e);
        }
        return products;
    }

}

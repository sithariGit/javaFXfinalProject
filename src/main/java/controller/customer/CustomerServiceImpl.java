package controller.customer;

import db.DBConnection;
import dto.Customer;

import java.sql.*;

public class CustomerServiceImpl implements CustomerService{

    // Check if a customer already exists in the database by email
    @Override
    public boolean isCustomerExist(String email) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT * FROM customer WHERE email = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next();  // If a record exists, return true (customer exists)
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Add or update a customer's information
    @Override
    public boolean addOrUpdateCustomer(Customer customer, Connection connection) throws SQLException {
        // Check if the customer exists
        if (isCustomerExist(customer.getEmail())) {
            // If customer exists, update their information
            String query = "UPDATE customer SET name = ?, email = ? WHERE customer_id = ?";
            //Connection connection = DBConnection.getInstance().getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, customer.getName());
                stmt.setString(2, customer.getEmail());
                stmt.setInt(3, customer.getCustomerId());

                // Execute the update and return true if successful
                return stmt.executeUpdate() > 0;
            }
        } else {
            // If customer doesn't exist, insert a new one
            String query = "INSERT INTO customer (name, email) VALUES (?, ?)";
            //Connection connection = DBConnection.getInstance().getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, customer.getName());
                stmt.setString(2, customer.getEmail());

                // Execute the insert and get the auto-generated customer ID
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    return false;  // No rows inserted
                }

                // Get the auto-generated customer ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int customerId = generatedKeys.getInt(1);
                        customer.setCustomerId(customerId);  // Set the generated ID to the customer object
                        return true;  // Successfully inserted and got the ID
                    } else {
                        return false;  // Failed to retrieve the generated ID
                    }
                }
            }
        }
    }


    @Override
    public boolean deleteCustomer(int id) {
        return false;
    }
}

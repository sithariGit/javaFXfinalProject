package controller.order;

import dto.Orders;

import java.sql.*;

public class OrderService {
    public boolean addOrder(Orders order, Connection connection) throws SQLException {
        String query = "INSERT INTO orders (net_price, payment_type, order_date, user_id, customer_id) VALUES (?, ?, ?, ?, ?)";

        try (//Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setBigDecimal(1, order.getNetPrice());
            stmt.setString(2, order.getPaymentType().toString());
            stmt.setDate(3, new Date(order.getOrderDate().getTime()));
            stmt.setInt(4, order.getUser().getUserId());

            // 🔹 Handle possible null customer
            if (order.getCustomer() != null) {
                stmt.setInt(5, order.getCustomer().getCustomerId());
            } else {
                stmt.setNull(5, Types.INTEGER); // ✅ Set NULL if no customer
            }

            boolean isOrderAdded = stmt.executeUpdate() > 0;
            if (!isOrderAdded) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            // Retrieve the generated order_id
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1)); // Set order_id in the object
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
        return true;
    }
}

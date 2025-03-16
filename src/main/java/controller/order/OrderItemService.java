package controller.order;

import dto.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemService {

    public boolean addOrderItem(OrderItem orderItem, Connection connection) throws SQLException {
        String query = "INSERT INTO order_item (order_id, product_id, order_quantity) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            // Log the values before attempting to insert
            System.out.println("Attempting to insert Order Item:");
            System.out.println("Order ID: " + orderItem.getOrder().getOrderId());
            System.out.println("Product ID: " + orderItem.getProduct().getProductId());
            System.out.println("Order Quantity: " + orderItem.getOrderQuantity());

            stmt.setObject(1, orderItem.getOrder().getOrderId());
            stmt.setObject(2, orderItem.getProduct().getProductId());
            stmt.setObject(3, orderItem.getOrderQuantity());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Failed to insert order item, check your data: ");
                System.out.println("Order ID: " + orderItem.getOrder().getOrderId());
                System.out.println("Product ID: " + orderItem.getProduct().getProductId());
                System.out.println("Order Quantity: " + orderItem.getOrderQuantity());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error while adding order item:");
            System.out.println("Order ID: " + orderItem.getOrder().getOrderId());
            System.out.println("Product ID: " + orderItem.getProduct().getProductId());
            System.out.println("Order Quantity: " + orderItem.getOrderQuantity());
            throw new SQLException("Error while adding order item: " + e.getMessage(), e);
        }

        return true;
    }
}
package controller.customer;

import dto.Customer;

import java.sql.Connection;
import java.sql.SQLException;

public interface CustomerService {
    boolean addOrUpdateCustomer(Customer customer, Connection connection) throws SQLException;

    boolean deleteCustomer(int id);

    public boolean isCustomerExist(String email);
}

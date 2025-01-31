package controller.employee;

import db.DBConnection;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController implements EmployeeService {

    @Override
    public boolean addEmployee(Employee employee) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO Employee (name, email) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "UPDATE Employee SET name = ?, email = ? WHERE employee_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setString(3, String.valueOf(employee.getEmployeeId()));

            return preparedStatement.executeUpdate() > 0;



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT * FROM Employee";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public boolean deleteEmployee(int employeeId) throws SQLException {
        // Step 1: Delete the related User record first (if there's a related user)
        String deleteUserQuery = "DELETE FROM User WHERE user_id = (SELECT user_id FROM Employee WHERE employee_id = ?)";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserQuery)) {

            // Set employee ID to find the associated user
            deleteUserStatement.setInt(1, employeeId);

            // Execute the delete for the User
            int userDeleted = deleteUserStatement.executeUpdate();

            // Step 2: Now delete the employee record
            String deleteEmployeeQuery = "DELETE FROM Employee WHERE employee_id = ?";

            try (PreparedStatement deleteEmployeeStatement = connection.prepareStatement(deleteEmployeeQuery)) {
                // Set the employee ID
                deleteEmployeeStatement.setInt(1, employeeId);

                // Execute the delete for the Employee
                int employeeDeleted = deleteEmployeeStatement.executeUpdate();

                if (employeeDeleted > 0 && userDeleted > 0) {
                    return true; // Successfully deleted both the employee and user
                } else {
                    return false; // Could not delete the employee or the user
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting employee.");
        }
    }

}


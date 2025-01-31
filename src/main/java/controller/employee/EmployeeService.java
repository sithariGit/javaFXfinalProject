package controller.employee;

import model.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {
    boolean addEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    List<Employee> getAllEmployees();
    boolean deleteEmployee(int employeeId) throws SQLException;
}

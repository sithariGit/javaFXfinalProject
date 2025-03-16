package controller.supplier;

import db.DBConnection;
import dto.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SupplierServiceImpl implements SupplierService {

    @Override
    public boolean addSupplier(Supplier supplier) {

        String SQL = "INSERT INTO supplier (name, email, company_name) VALUES (?,?,?)";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getEmail());
            preparedStatement.setString(3, supplier.getCompanyName());

            return preparedStatement.executeUpdate() > 0;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql="UPDATE supplier SET name=?, email=?, company_name=? WHERE supplier_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getEmail());
            preparedStatement.setString(3, supplier.getCompanyName());
            preparedStatement.setInt(4, supplier.getSupplierId());


            return preparedStatement.executeUpdate()>0;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Supplier searchSupplier(String id) {return null;
    }

    @Override
    public Supplier searchSupplierData(String id) {
        return null;
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();


        try {
            String SQL = "SELECT * FROM supplier";
            Connection connection = DBConnection.getInstance().getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("company_name")
                );
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return suppliers;
    }


    @Override
    public boolean deleteSupplier(String id) {
        return false;
    }

    public Supplier getSupplierById(Integer supplierId) {
        Supplier supplier = null;
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String SQL = "SELECT * FROM supplier WHERE supplier_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, supplierId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("company_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;
    }

}

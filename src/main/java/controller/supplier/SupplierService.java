package controller.supplier;

import dto.Supplier;

import java.sql.SQLException;
import java.util.List;

public interface SupplierService {
    boolean addSupplier(Supplier supplier) throws SQLException;
    boolean updateSupplier(Supplier supplier);
    Supplier searchSupplier(String id);

    Supplier searchSupplierData(String id);

    List<Supplier> getAllSuppliers();
    boolean deleteSupplier(String id);
}

package controller.product;

import dto.Product;

import java.util.List;

public interface ProductService {
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    Product searchProduct(Integer id);
    List<Product> getAllProducts();
    boolean deleteProduct(Integer id);
}

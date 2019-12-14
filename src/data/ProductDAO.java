package data;

import model.FoodMenu;
import model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public interface ProductDAO {
    public void createProduct(Product product) throws SQLException;

    public void updateProduct(Product product) throws SQLException;

    public void deleteProduct(Product product) throws SQLException;

    public Product getProductById(int id) throws SQLException;

    public Collection<Product> getAllProducts() throws SQLException;

    public Collection<Product> getProductByFoodMenu(FoodMenu menu) throws SQLException;
}

package data;

import model.FoodMenu;
import model.Product;

import java.sql.SQLException;
import java.util.Collection;

public interface ProductDAO {
    /**
     * Create product in db
     * @param product
     * @throws SQLException
     */
    void createProduct(Product product) throws SQLException;

    /**
     * Update product in db
     * @param product
     * @throws SQLException
     */
    void updateProduct(Product product) throws SQLException;

    /**
     * Delete product from db
     * @param product
     * @throws SQLException
     */
    void deleteProduct(Product product) throws SQLException;

    /**
     * Get product by id
     * @param id
     * @return Product
     * @throws SQLException
     */
    Product getProductById(int id) throws SQLException;

    /**
     * Get all products from db
     * @return Collection of product
     * @throws SQLException
     */
    Collection<Product> getAllProducts() throws SQLException;

    /**
     * Return all product in food menu
     * @param menu
     * @return Collection of product
     * @throws SQLException
     */
    Collection<Product> getProductByFoodMenu(FoodMenu menu) throws SQLException;
}

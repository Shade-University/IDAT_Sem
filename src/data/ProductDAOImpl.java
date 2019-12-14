package data;

import controller.enums.*;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAOImpl implements ProductDAO {
    Connection conn;


    public ProductDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void createProduct(Product product) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO PRODUKTY(ID_PRODUKTU, NAZEV, POPIS, SKLADEM, TYP, CENA) "
                        + "VALUES (?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setInt(1, product.getId());
        preparedStatement.setString(2, product.getName());
        preparedStatement.setString(3, product.getDescription());
        preparedStatement.setInt(4, product.getInStock());
        preparedStatement.setString(5, product.getType().toString());
        preparedStatement.setFloat(6, product.getPrice());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Product inserted");
    }

    @Override
    public Collection<Product> getAllProducts() throws SQLException {
        Collection<Product> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM PRODUKTY");

        while (rs.next()) {
            Product product = getProduct(rs);
            collection.add(product);
        }
        statement.close();
        conn.commit();
        return collection;
    }

    @Override
    public Collection<Product> getProductByFoodMenu(FoodMenu menu) throws SQLException {
        Collection<Product> collection = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM PRODUKTY p " +
                        "left join LISTEK_PRODUKT LP on p.ID_PRODUKTU = LP.ID_PRODUKT" +
                        " where LP.ID_LISTEK = ? AND p.SKLADEM > 0");
        preparedStatement.setInt(1, menu.getId());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Product product = getProduct(rs);
            collection.add(product);
        }
        preparedStatement.close();
        conn.commit();
        return collection;

    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE PRODUKTY SET NAZEV = ?, POPIS = ?, SKLADEM = ?, TYP = ?, CENA = ? where ID_PRODUKTU = ?"
        );
        preparedStatement.setString(2, product.getName());
        preparedStatement.setString(3, product.getDescription());
        preparedStatement.setInt(4, product.getInStock());
        preparedStatement.setString(5, product.getType().toString());
        preparedStatement.setFloat(6, product.getPrice());
        preparedStatement.setInt(1, product.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Product updated");
    }

    @Override
    public void deleteProduct(Product product) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM PRODUKTY WHERE ID_PRODUKTU = ?"
        );
        preparedStatement.setInt(1, product.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Product deleted");
    }

    @Override
    public Product getProductById(int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM PRODUKTY WHERE ID_PRODUKTU = ? ");
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            Product product = getProduct(rs);
            preparedStatement.close();
            conn.commit();
            return product;
        }
        preparedStatement.close();
        return null;
    }

    /**
     * Parser ResultSetu na Produkt
     *
     * @param rs
     * @return produkt
     * @throws SQLException
     */
    private Product getProduct(ResultSet rs) throws SQLException {
        Product product = new Product(
                rs.getInt("id_produktu"),
                rs.getString("nazev"),
                rs.getString("popis"),
                rs.getInt("skladem"),
                TRANSACTION_TYPE.get(rs.getString("typ")),
                rs.getFloat("cena")
        );
        return product;
    }

}

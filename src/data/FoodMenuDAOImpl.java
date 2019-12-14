package data;

import controller.enums.TRANSACTION_TYPE;
import model.FoodMenu;
import model.Group;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoodMenuDAOImpl implements FoodMenuDAO {

    private Connection conn;

    @Override
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createFoodMenu(FoodMenu menu) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO JIDELNI_LISTKY(ID_LISTKU, DATUM) "
                        + "VALUES (?, ?)"
        );
        preparedStatement.setInt(1, menu.getId());
        preparedStatement.setDate(2, menu.getDate());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Food menu inserted");
    }

    @Override
    public void updateFoodMenu(FoodMenu menu) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE JIDELNI_LISTKY SET DATUM = ? where ID_LISTKU = ?"
        );
        preparedStatement.setDate(2, menu.getDate());
        preparedStatement.setInt(1, menu.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Food menu updated");
    }

    @Override
    public void deleteFoodMenu(FoodMenu menu) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM JIDELNI_LISTKY WHERE ID_LISTKU = ?"
        );
        preparedStatement.setInt(1, menu.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Food menu deleted");
    }

    @Override
    public FoodMenu getFoodMenuByDate(Date date) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM JIDELNI_LISTKY WHERE DATUM = ? ");
        preparedStatement.setDate(1, date);
        ResultSet rs = preparedStatement.executeQuery();
        FoodMenu foodMenu = null;
        if (rs.next())
            foodMenu = getFoodMenu(rs);

        preparedStatement.close();
        conn.commit();
        return foodMenu;
    }

    @Override
    public Collection<FoodMenu> getAllFoodMenu() throws SQLException {
        Collection<FoodMenu> collection = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM JIDELNI_LISTKY");
        while (rs.next()) {
            FoodMenu foodMenu = getFoodMenu(rs);
            collection.add(foodMenu);
        }
        statement.close();
        conn.commit();
        return collection;
    }

    /**
     * Parser ResultSetu na Produkt
     *
     * @param rs
     * @return produkt
     * @throws SQLException
     */
    private FoodMenu getFoodMenu(ResultSet rs) throws SQLException {
        FoodMenu foodMenu = new FoodMenu(
                rs.getInt("id_listku"),
                rs.getDate("datum")
        );
        return foodMenu;
    }
}

package data;

import com.sun.deploy.security.ValidationState;
import com.sun.org.apache.xpath.internal.operations.Or;
import controller.enums.TRANSACTION_TYPE;
import model.Order;
import model.Product;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class OrderDAOImpl implements OrderDAO {
    UserDAO userDAO = new UserDAOImpl();
    ProductDAO productDAO = new ProductDAOImpl();
    private Connection conn;

    @Override
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createOrder(Order order) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO TRANSAKCE(ID_TRANSAKCE, ID_UZIVATELE, ID_PRODUKTU, TYP_TRANSAKCE, CASTKA, DATUM, POPIS) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setInt(1, order.getId());
        preparedStatement.setInt(2, order.getUser().getId());
        preparedStatement.setInt(3, order.getProduct().getId());
        preparedStatement.setString(4, order.getTypeOfTransaction().toString());
        preparedStatement.setFloat(5, order.getPrice());
        preparedStatement.setDate(6, order.getDate());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Order created!");
    }

    @Override
    public void updateOrder(Order order) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE TRANSAKCE SET ID_UZIVATELE = ?, ID_PRODUKTU = ?, TYP_TRANSAKCE = ?, CASTKA = ?, DATUM = ?, POPIS = ? where ID_TRANSAKCE = ?"
        );
        preparedStatement.setInt(2, order.getUser().getId());
        preparedStatement.setInt(3, order.getProduct().getId());
        preparedStatement.setString(4, order.getTypeOfTransaction().toString());
        preparedStatement.setFloat(5, order.getPrice());
        preparedStatement.setDate(6, order.getDate());
        preparedStatement.setInt(1, order.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Order updated");

    }

    @Override
    public void removeOrder(Order order) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM TRANSAKCE WHERE ID_TRANSAKCE = ?"
        );
        preparedStatement.setInt(1, order.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("Order deleted");
    }

    @Override
    public Collection<Order> getAllOrders() throws SQLException {
        Collection<Order> collection = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM TRANSAKCE");
        while (rs.next()) {
            Order order = getOrder(rs);
            collection.add(order);
        }
        statement.close();
        conn.commit();
        return collection;
    }

    @Override
    public Collection<Order> getOrdersByUser(User user) throws SQLException {
        Collection<Order> collection = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM TRANSAKCE WHERE ID_UZIVATELE = ?"
        );
        preparedStatement.setInt(1, user.getId());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Order order = getOrder(rs);
            collection.add(order);
        }
        preparedStatement.close();
        conn.commit();
        return collection;
    }

    @Override
    public Collection<Order> getOrdersNewerThan(Date date) throws SQLException {
        Collection<Order> collection = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM TRANSAKCE WHERE DATUM >= ?"
        );
        preparedStatement.setDate(1, date);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Order order = getOrder(rs);
            collection.add(order);
        }
        preparedStatement.close();
        conn.commit();
        return collection;
    }

    @Override
    public Order getTodayOrderByUser(User user, Date date) throws SQLException {
        Collection<Order> collection = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM TRANSAKCE WHERE DATUM = ? AND ID_UZIVATELE = ?"
        );
        preparedStatement.setDate(1, date);
        preparedStatement.setInt(2, user.getId());
        ResultSet rs = preparedStatement.executeQuery();
        Order order = null;
        if (rs.next())
            order = getOrder(rs);
        preparedStatement.close();
        conn.commit();
        return order;
    }

    /**
     * Parser ResultSetu na Transakce
     *
     * @param rs
     * @return Order
     * @throws SQLException
     */
    private Order getOrder(ResultSet rs) throws SQLException {
        Order order = new Order(
                rs.getInt("id_transakce"),
                userDAO.getUserById(rs.getInt("id_uzivatele")),
                productDAO.getProductById(rs.getInt("id_produktu")),
                TRANSACTION_TYPE.get(rs.getString("typ_transakce")),
                rs.getFloat("castka"),
                rs.getDate("datum"),
                rs.getString("popis")
        );
        return order;
    }
}

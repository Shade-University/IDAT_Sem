package data;

import controller.enums.TRANSACTION_TYPE;
import model.Order;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAOImpl implements OrderDAO {
    UserDAO userDAO = new UserDAOImpl();
    ProductDAO productDAO = new ProductDAOImpl();
    private Connection conn;

    public OrderDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void createOrder(Order order) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO TRANSAKCE(ID_TRANSAKCE, ID_UZIVATELE, ID_PRODUKTU, TYP_TRANSAKCE, CASTKA, DATUM, POPIS) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setInt(1, order.getId());
        preparedStatement.setInt(2, order.getUser().getId());
        if(order.getProduct()!=null){
            preparedStatement.setInt(3, order.getProduct().getId());
        } else {
            preparedStatement.setNull(3,0);
        }
        preparedStatement.setString(4, order.getTypeOfTransaction().toString());
        preparedStatement.setFloat(5, order.getPrice());
        preparedStatement.setDate(6, order.getDate());
        preparedStatement.setString(7, order.getDescription());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("CreateOrder");
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
        preparedStatement.setString(7, order.getDescription());
        preparedStatement.setInt(1, order.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        conn.commit();
        System.out.println("UpdateOrder");

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
        System.out.println("RemoveOrder");
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
        System.out.println("GetAllOrders");
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
        System.out.println("getOrdersByUser");
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
        System.out.println("getOrdersNewerThan");
        return collection;
    }

    @Override
    public Collection<Order> getTodayOrderByUser(User user, Date date) throws SQLException {
        Collection<Order> collection = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM TRANSAKCE WHERE to_char(DATUM, 'yyyy-mm-dd') = to_char(?, 'yyyy-mm-dd') AND ID_UZIVATELE = ?"
        );
        preparedStatement.setDate(1, date);
        preparedStatement.setInt(2, user.getId());

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Order order = getOrder(rs);
            collection.add(order);
        }
        preparedStatement.close();
        System.out.println("getTodayOrderByUser");
        return collection;
    }

    @Override
    public float getAccountBalance(User user) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT " +
                        "    SUM( CASTKA ) \"castka\"" +
                        "FROM " +
                        "    TRANSAKCE\n" +
                        "WHERE " +
                        "    ID_UZIVATELE = ?"
        );
        preparedStatement.setInt(1, user.getId());

        ResultSet rs = preparedStatement.executeQuery();
        Float balance = null;
        if (rs.next())
            balance = rs.getFloat("castka");
        preparedStatement.close();
        System.out.println("getAccountBalance");
        return balance;
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

package data;

import model.Order;
import model.User;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

public interface OrderDAO {

    /**
     * Create order in db
     * @param order
     * @throws SQLException
     */
    void createOrder(Order order) throws SQLException;

    /**
     * Update order in db
     * @param order
     * @throws SQLException
     */
    void updateOrder(Order order) throws SQLException;

    /**
     * Remove order from db
     * @param order
     * @throws SQLException
     */
    void removeOrder(Order order) throws SQLException;

    /**
     * Return collection of all orders in db
     * @return Collection of order
     * @throws SQLException
     */
    Collection<Order> getAllOrders() throws SQLException;

    /**
     * Return orders by user
     * @param user
     * @return Collection of order
     * @throws SQLException
     */
    Collection<Order> getOrdersByUser(User user) throws SQLException;

    /**
     * Return orderss newer than date
     * @param date
     * @return Collection of order
     * @throws SQLException
     */
    Collection<Order> getOrdersNewerThan(Date date) throws SQLException;

    /**
     * Get today orders by user
     * @param user
     * @param date
     * @return Collection of order
     * @throws SQLException
     */
    Collection<Order> getTodayOrderByUser(User user, Date date) throws SQLException;

    /**
     * Return account balance of user
     * @param user
     * @return Float
     * @throws SQLException
     */
    float getAccountBalance(User user) throws SQLException;

}

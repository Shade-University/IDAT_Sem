package data;

import model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

public interface OrderDAO {


    public void createOrder(Order order) throws SQLException;

    public void updateOrder(Order order) throws SQLException;

    public void removeOrder(Order order) throws SQLException;

    public Collection<Order> getAllOrders() throws SQLException;

    public Collection<Order> getOrdersByUser(User user) throws SQLException;

    public Collection<Order> getOrdersNewerThan(Date date) throws SQLException;

    public Collection<Order> getTodayOrderByUser(User user, Date date) throws SQLException;

    public float getAccountBalance(User user) throws SQLException;

}

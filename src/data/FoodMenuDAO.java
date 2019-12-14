package data;

import model.FoodMenu;
import model.Product;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

public interface FoodMenuDAO {

    public void setConn(Connection conn);

    public void createFoodMenu(FoodMenu menu) throws SQLException;
    public void updateFoodMenu(FoodMenu menu) throws SQLException;
    public void deleteFoodMenu(FoodMenu menu) throws SQLException;

    public FoodMenu getFoodMenuByDate(Date date) throws SQLException;
    public Collection<FoodMenu> getAllFoodMenu() throws SQLException;
}

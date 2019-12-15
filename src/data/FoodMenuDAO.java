package data;

import model.FoodMenu;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

public interface FoodMenuDAO {

    /**
     * Create food menu in db
     * @param menu
     * @throws SQLException
     */
    void createFoodMenu(FoodMenu menu) throws SQLException;

    /**
     * Update food menu in db
     * @param menu
     * @throws SQLException
     */
    void updateFoodMenu(FoodMenu menu) throws SQLException;

    /**
     * Delete food menu from db
     * @param menu
     * @throws SQLException
     */
    void deleteFoodMenu(FoodMenu menu) throws SQLException;

    /**
     * Return food menu by date from db
     * @param date
     * @return FoodMenu
     * @throws SQLException
     */
    FoodMenu getFoodMenuByDate(Date date) throws SQLException;

    /**
     * Return all food menus from db
     * @return Collection of food menu
     * @throws SQLException
     */
    Collection<FoodMenu> getAllFoodMenu() throws SQLException;
}

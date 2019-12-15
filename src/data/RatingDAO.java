package data;

import model.Group;
import model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author user
 */
public interface RatingDAO {

    /**
     * Get all ratings from db
     * @return Collection of rating
     * @throws SQLException
     */
    Collection<Rating> getAllRatings() throws SQLException;

    /**
     * Create rating in db
     * @param rating
     * @throws SQLException
     */
    void createRating(Rating rating) throws SQLException;

    /**
     * Update rating in db
     * @param rating
     * @throws SQLException
     */
    void updateRating(Rating rating) throws SQLException;

    /**
     * Return average rating for group
     * @param group
     * @return double
     * @throws SQLException
     */
    double getAverageRating(Group group) throws SQLException;

    /**
     * Helper method for parse rating from result set
     * @param rs
     * @return Rating
     * @throws SQLException
     */
    Rating getRating(ResultSet rs) throws SQLException;

    /**
     * Delete rating from db
     * @param rt
     * @throws SQLException
     */
    void deleteRating(Rating rt) throws SQLException;
}

package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import model.Rating;
import model.Group;

/**
 *
 * @author user
 */
public interface RatingDAO {

    public Collection<Rating> getAllRatings() throws SQLException;

    public void createRating(Rating hodnoceni) throws SQLException;

    public void updateRating(Rating hodnoceni) throws SQLException;

    public double getAverageRating(Group skupina) throws SQLException;

    public Rating getRating(ResultSet rs) throws SQLException;

    public void deleteRating(Rating rt) throws SQLException;
}

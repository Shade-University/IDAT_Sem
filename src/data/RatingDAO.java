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

    public Collection<Rating> getAllRatings();

    public void createRating(Rating hodnoceni);

    public double getAverageRating(Group skupina);

    public Rating getRating(ResultSet rs) throws SQLException;
}

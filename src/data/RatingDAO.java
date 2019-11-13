package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import model.Hodnoceni;
import model.Skupina;

/**
 *
 * @author user
 */
public interface RatingDAO {

    public Collection<Hodnoceni> getAllRatings();

    public void createRating(Hodnoceni hodnoceni);

    public double getAverageRating(Skupina skupina);

    public Hodnoceni getRating(ResultSet rs) throws SQLException;
}

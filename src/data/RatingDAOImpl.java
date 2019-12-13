package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Rating;
import model.Group;

/**
 *
 * @author Tomáš Vondra
 */
public class RatingDAOImpl implements RatingDAO {

    private Connection conn;
    private UserDAO userDAO;
    private GroupDAO groupDAO;

    public RatingDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
            userDAO = new UserDAOImpl();
            groupDAO = new GroupDAOImpl();
        } catch (SQLException ex) {
            Logger.getLogger(RatingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection<Rating> getAllRatings() {
        Collection<Rating> collection = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM GETHODNOCENI");
            while (rs.next()) {
                Rating hodnoceni = getRating(rs);
                collection.add(hodnoceni);
            }
            return collection;

        } catch (SQLException ex) {
            Logger.getLogger(RatingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void createRating(Rating hodnoceni) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO HODNOCENI(HODNOTA_HODNOCENI, POPIS, ID_UZIVATEL, ID_SKUPINA)"
                    + "VALUES(?,?,?,?)");
            preparedStatement.setInt(1, hodnoceni.getHodnota());
            preparedStatement.setString(2, hodnoceni.getPopis());
            preparedStatement.setInt(3, hodnoceni.getHodnoticiUzivatel().getId());
            preparedStatement.setInt(4, hodnoceni.getHodnoticiSkupina().getId());

            preparedStatement.executeUpdate();
            System.out.println("Rating created");
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(RatingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateRating(Rating hodnoceni) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call update_hodnoceni(?,?,?,?,?)"
        );
        callableStatement.setInt(1, hodnoceni.getId());
        callableStatement.setInt(2, hodnoceni.getHodnota());
        callableStatement.setString(3, hodnoceni.getPopis());
        callableStatement.setInt(4, hodnoceni.getHodnoticiUzivatel().getId());
        callableStatement.setInt(5, hodnoceni.getHodnoticiSkupina().getId());
        callableStatement.executeQuery();
        conn.commit();
        System.out.println("Rating has been updated.");
    }

    @Override
    public Rating getRating(ResultSet rs) throws SQLException {
        Rating hodnoceni = new Rating(
                rs.getInt("id_hodnoceni"),
                rs.getInt("hodnota_hodnoceni"),
                rs.getString("popis"),
                userDAO.getUser(rs),
                groupDAO.getGroup(rs)
        );
        return hodnoceni;
    }

    @Override
    public double getAverageRating(Group skupina) {
        try {
            Statement statement = conn.createStatement();

//            ResultSet rs = stmt.executeQuery(
//                    "SELECT (SUM(hodnota_hodnoceni) / COUNT(*)) AS AVERAGE FROM getRatings");
            ResultSet rs = statement.executeQuery("SELECT AVG(hodnota_hodnoceni) as \"AVERAGE\" FROM getRatings "
                    + "WHERE id_skupina = " + skupina.getId());
            double output = -1;
            if (rs.next()) {
                output =  rs.getDouble("AVERAGE");
            }
            return output;
        } catch (SQLException ex) {
            Logger.getLogger(RatingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public void deleteRating(Rating rt) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_hodnoceni(?)"
        );
        callableStatement.setInt(1, rt.getId());
        callableStatement.executeQuery();
        conn.commit();
        System.out.println("Rating has been deleted.");
    }
}

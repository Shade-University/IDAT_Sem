package data;

import model.Group;
import model.Rating;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public Collection<Rating> getAllRatings() throws SQLException {
        Collection<Rating> collection = new ArrayList<>();
        Statement statement = conn.createStatement();

        ResultSet rs = statement.executeQuery(
                "SELECT * FROM GETHODNOCENI");
        while (rs.next()) {
            Rating rating = getRating(rs);
            collection.add(rating);
        }
        statement.close();
        System.out.println("getAllRatings");
        return collection;
    }

    @Override
    public void createRating(Rating rating) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO HODNOCENI(HODNOTA_HODNOCENI, POPIS, ID_UZIVATEL, ID_SKUPINA)"
                        + "VALUES(?,?,?,?)");
        preparedStatement.setInt(1, rating.getHodnota());
        preparedStatement.setString(2, rating.getPopis());
        preparedStatement.setInt(3, rating.getHodnoticiUzivatel().getId());
        preparedStatement.setInt(4, rating.getHodnoticiSkupina().getId());

        preparedStatement.executeUpdate();
        conn.commit();
        System.out.println("CreateRating");
        preparedStatement.close();
    }

    @Override
    public void updateRating(Rating rating) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call update_hodnoceni(?,?,?,?,?)"
        );
        callableStatement.setInt(1, rating.getId());
        callableStatement.setInt(2, rating.getHodnota());
        callableStatement.setString(3, rating.getPopis());
        callableStatement.setInt(4, rating.getHodnoticiUzivatel().getId());
        callableStatement.setInt(5, rating.getHodnoticiSkupina().getId());

        callableStatement.executeQuery();
        callableStatement.close();
        conn.commit();
        System.out.println("UpdateRating");
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
    } //Parser method to get Rating

    @Override
    public Rating getRatingByUserAndGroup(User user, Group group) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "select * from GETHODNOCENI where ID_UZIVATEL = ? AND ID_SKUPINA = ?"
        );
        preparedStatement.setInt(1, user.getId());
        preparedStatement.setInt(2, group.getId());

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
           return getRating(rs);
        preparedStatement.close();
        System.out.println("getRatingByUserAndGroup");
        return null;
    }

    @Override
    public double getAverageRating(Group group) throws SQLException {
        Statement statement = conn.createStatement();

        ResultSet rs = statement.executeQuery("SELECT AVG(hodnota_hodnoceni) as \"AVERAGE\" FROM getRatings "
                + "WHERE id_skupina = " + group.getId());
        Double output = null;
        if (rs.next())
            output = rs.getDouble("AVERAGE");

        statement.close();
        System.out.println("GetAverageRating");
        return output;
    }

    @Override
    public void deleteRating(Rating rt) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_hodnoceni(?)"
        );
        callableStatement.setInt(1, rt.getId());

        callableStatement.executeQuery();
        callableStatement.close();
        conn.commit();
        System.out.println("DeleteRating");
    }
}

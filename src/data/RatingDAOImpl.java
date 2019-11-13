package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Hodnoceni;
import model.Skupina;

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
    public Collection<Hodnoceni> getAllRatings() {
        Collection<Hodnoceni> collection = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM getRatings");
            while (rs.next()) {
                Hodnoceni hodnoceni = getRating(rs);
                collection.add(hodnoceni);
            }
            return collection;

        } catch (SQLException ex) {
            Logger.getLogger(RatingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void createRating(Hodnoceni hodnoceni) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO HODNOCENI(HODNOTA_HODNOCENI, POPIS, ID_UZIVATEL, ID_SKUPINA)"
                    + "VALUES(?,?,?,?)");
            stmt.setInt(1, hodnoceni.getHodnota());
            stmt.setString(2, hodnoceni.getPopis());
            stmt.setInt(3, hodnoceni.getHodnoticiUzivatel().getId());
            stmt.setInt(4, hodnoceni.getHodnoticiSkupina().getId());

            stmt.executeUpdate();
            System.out.println("Rating created");
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(RatingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Hodnoceni getRating(ResultSet rs) throws SQLException {
        Hodnoceni hodnoceni = new Hodnoceni(
                rs.getInt("id_hodnoceni"),
                rs.getInt("hodnota_hodnoceni"),
                rs.getString("popis"),
                userDAO.getUser(rs),
                groupDAO.getGroup(rs)
        );
        return hodnoceni;
    }

    @Override
    public double getAverageRating(Skupina skupina) {
        try {
            Statement stmt = conn.createStatement();

//            ResultSet rs = stmt.executeQuery(
//                    "SELECT (SUM(hodnota_hodnoceni) / COUNT(*)) AS AVERAGE FROM getRatings");
            ResultSet rs = stmt.executeQuery("SELECT AVG(hodnota_hodnoceni) as \"AVERAGE\" FROM getRatings "
                    + "WHERE id_skupina = " + skupina.getId());
            if (rs.next()) {
                return rs.getDouble("AVERAGE");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

}

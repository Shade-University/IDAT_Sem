package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Obor;
import model.Predmet;

/**
 *
 * @author Tomáš Vondra
 */
public class SubjectDAOImpl implements SubjectDAO {

    private Connection conn;

    public SubjectDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection<Predmet> getAllSubjects() {
        Collection<Predmet> collection = new ArrayList<>();
        try {

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM PREDMETY");

            while (rs.next()) {
                Predmet predmet = getPredmet(rs);
                collection.add(predmet);
            }

            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Collection<Predmet> getSubjectsForField(Obor obor) {
        Collection<Predmet> collection = new ArrayList<>();
        try {
            PreparedStatement pstm = conn.prepareStatement(
                    "SELECT * FROM OBOR_PREDMET op\n"
                    + "JOIN PREDMETY p ON p.id_predmet = op.predmet_id_predmet\n"
                    + "WHERE op.studijni_obor_id_obor = ?"
            );
            pstm.setInt(1, obor.getId());

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Predmet p = getPredmet(rs);
                collection.add(p);
            }
            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Predmet getPredmet(ResultSet rs) throws SQLException {
        Predmet predmet = new Predmet(
                rs.getInt("id_predmet"),
                rs.getString("nazev"),
                rs.getString("popis")
        );

        return predmet;
    }

    @Override
    public void insertSubjectsToField(List<Predmet> predmety, Obor obor) {
        try {

            for (int i = 0; i < predmety.size(); i++) {
                PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO OBOR_PREDMET(studijni_obor_id_obor, predmet_id_predmet)"
                        + "VALUES (?, ?)"
                );
                pstm.setInt(1, obor.getId());
                pstm.setInt(2, predmety.get(i).getId());
                
                
                pstm.executeUpdate();
                conn.commit();
                System.out.println("Subject added to field");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void removeSubjectsFromField(List<Predmet> predmety, Obor obor) {
        try {

            for (int i = 0; i < predmety.size(); i++) {
                PreparedStatement pstm = conn.prepareStatement(
                        "DELETE FROM OBOR_PREDMET "
                                + "WHERE predmet_id_predmet = ? AND "
                                + "studijni_obor_id_obor = ?"
                );
                pstm.setInt(1, predmety.get(i).getId());
                pstm.setInt(2, obor.getId());
                pstm.setInt(1, predmety.get(i).getId());
                
                
                pstm.executeUpdate();
                conn.commit();
                System.out.println("Subject deleted from field");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

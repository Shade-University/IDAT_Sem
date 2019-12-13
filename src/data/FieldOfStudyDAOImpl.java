package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Field;
import model.Subject;
import model.User;

/**
 * @author Tomáš Vondra
 */
public class FieldOfStudyDAOImpl implements FieldOfStudyDAO {

    private Connection conn;

    public FieldOfStudyDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FieldOfStudyDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection<Field> getAllFields() throws SQLException{

        Collection<Field> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM STUDIJNI_OBORY");

        while (rs.next()) {
            Field obor = new Field(
                    rs.getInt("id_obor"),
                    rs.getString("nazev"),
                    rs.getString("popis")
            );
            collection.add(obor);
        }
        conn.commit();
        statement.close();
        return collection;
    }

    @Override
    public Collection<Field> getFieldsBySubjects(Subject subject) throws SQLException {
        Collection<Field> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM getOborySPredmetem g WHERE g.id_predmet = " + subject.getId());

        while (rs.next()) {
            Field obor = new Field(
                    rs.getInt("id_obor"),
                    rs.getString("nazev"),
                    rs.getString("POPIS")
            );
            collection.add(obor);
        }
        conn.commit();
        statement.close();
        return collection;
    }

    @Override
    public void deleteField(Field obor) {
        try {
            CallableStatement callableStatement = conn.prepareCall(
                    "call delete_field(?)"
            );
            callableStatement.setInt(1, obor.getId());

            callableStatement.executeQuery();
            conn.commit();
            callableStatement.close();
            System.out.println("Field deleted");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertField(Field obor) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call insert_studijni_obor(?,?)"
        );
        callableStatement.setString(1, obor.getNazev());
        callableStatement.setString(2, obor.getPopis());
        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("Field added.");
    }

    @Override
    public void updateField(Field obor) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE STUDIJNI_OBORY SET "
                        + "nazev = ?, "
                        + "popis = ?"
                        + " where id_obor = ?"
        );
        preparedStatement.setString(1, obor.getNazev());
        preparedStatement.setString(2, obor.getPopis());
        preparedStatement.setInt(3, obor.getId());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("Field updated");
    }


}

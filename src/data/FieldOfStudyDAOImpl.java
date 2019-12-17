package data;

import model.Field;
import model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        statement.close();
        System.out.println("GetAllFields");
        return collection;
    }

    @Override
    public Collection<Field> getFieldsBySubject(Subject subject) throws SQLException {
        Collection<Field> collection = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM getOborySPredmetem g WHERE g.id_predmet = ?"
        );
        preparedStatement.setInt(1, subject.getId());

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Field obor = new Field(
                    rs.getInt("id_obor"),
                    rs.getString("nazev"),
                    rs.getString("POPIS")
            );
            collection.add(obor);
        }

        preparedStatement.close();
        System.out.println("GetFieldsBySubjects");
        return collection;
    }

    @Override
    public void deleteField(Field field) {
        try {
            CallableStatement callableStatement = conn.prepareCall(
                    "call delete_field(?)"
            );
            callableStatement.setInt(1, field.getId());

            callableStatement.executeQuery();
            conn.commit();
            callableStatement.close();
            System.out.println("DeleteField");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertField(Field field) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call insert_studijni_obor(?,?)"
        );
        callableStatement.setString(1, field.getName());
        callableStatement.setString(2, field.getDescription());

        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("InsertField");
    }

    @Override
    public void updateField(Field field) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE STUDIJNI_OBORY SET "
                        + "nazev = ?, "
                        + "popis = ?"
                        + " where id_obor = ?"
        );
        preparedStatement.setString(1, field.getName());
        preparedStatement.setString(2, field.getDescription());
        preparedStatement.setInt(3, field.getId());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("UpdateField");
    }
}

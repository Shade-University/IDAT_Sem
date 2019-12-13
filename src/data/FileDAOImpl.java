package data;

import model.File;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileDAOImpl implements FileDAO {

    private Connection conn;

    public FileDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void insertFile(File file) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO SOUBORY(nazev_souboru, typ_souboru, pripona, data, upraveno, nahrano) "
                        + "VALUES (?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1,file.getName());
        preparedStatement.setString(2, file.getType());
        preparedStatement.setString(3, file.getExtension());
        preparedStatement.setBytes(4, file.getData());
        preparedStatement.setDate(5, file.getDate_updated());
        preparedStatement.setDate(6, file.getDate_created());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("File inserted");
    }

    @Override
    public Collection<File> getAllFiles() throws SQLException {
        Collection<File> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM soubory");

        while (rs.next()) {
            File file = getFile(rs);
            collection.add(file);
        }
        statement.close();
        System.out.println("file data loaded");
        return collection;
    }

    @Override
    public File getFileById(int id) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM soubory WHERE ID_SOUBORU="+id);

        while (rs.next()) {
            return getFile(rs);
        }
        return null;
    }

    @Override
    public File getFile(ResultSet rs) throws SQLException {
        return new File(
                rs.getInt("id_souboru"),
                rs.getString("nazev_souboru"),
                rs.getString("pripona"),
                rs.getString("typ_souboru"),
                rs.getBytes("data"),
                rs.getDate("nahrano"),
                rs.getDate("upraveno")
        );
    }

    @Override
    public void updateFile(File editedFile) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "update soubory" +
                        " SET nazev_souboru = ?," +
                        "typ_souboru = ?," +
                        "pripona = ?," +
                        "data = ? " +
                        "where id_souboru = ?"
        );
        preparedStatement.setString(1, editedFile.getName());
        preparedStatement.setString(2, editedFile.getType());
        preparedStatement.setString(3, editedFile.getExtension());
        preparedStatement.setBytes(4, editedFile.getData());
        preparedStatement.setInt(5, editedFile.getId());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("File edited updated");
    }

    @Override
    public void deleteFile(File editedFile) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM SOUBORY WHERE id_souboru=?"
        );
        preparedStatement.setInt(1, editedFile.getId());

        preparedStatement.execute();
        conn.commit();
        preparedStatement.close();
        System.out.println("File deleted");
    }
}

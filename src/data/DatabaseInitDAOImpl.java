package data;

import model.FoodMenu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseInitDAOImpl implements DatabaseInitDAO {

    private Connection conn;

    public DatabaseInitDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FieldOfStudyDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createDatabaseStructure() throws SQLException, IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        System.out.println(path);
        String createStoredProcedureSqlString = new String(Files.readAllBytes(Paths.get(path +"/scripty/CreateScript.sql")));
             Statement statement = conn.createStatement();
            statement.execute(createStoredProcedureSqlString);
            statement.close();
            System.out.println("CreateDatabaseSchema");
    }

    @Override
    public void insertDemoData() {

    }

    @Override
    public void deleteDatabase() {

    }
}

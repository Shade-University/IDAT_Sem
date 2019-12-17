package data;

import java.io.IOException;
import java.sql.SQLException;

public interface DatabaseInitDAO {

    void createDatabaseStructure() throws SQLException, IOException;

    void insertDemoData();

    void deleteDatabase();

}

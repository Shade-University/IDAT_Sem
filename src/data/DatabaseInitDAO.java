package data;

import java.io.IOException;
import java.sql.SQLException;

public interface DatabaseInitDAO {

    /**
     * Create structure in db
     * Start CreateScript.sql
     * @throws SQLException
     * @throws IOException
     */
    void createDatabaseStructure() throws SQLException, IOException;

    /**
     * Insert sample date to db
     * Start InsertScript.sql
     */
    void insertDemoData();

    /**
     * Delete structure in db
     * Start DeleteScript.sql
     */
    void deleteDatabase();

}

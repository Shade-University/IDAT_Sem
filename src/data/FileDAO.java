package data;

import model.File;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface FileDAO {

    /**
     * Insert file to db
     * @return Id of inserted file, INT
     * @param file
     * @throws SQLException
     */
    int insertFile(File file) throws SQLException;

    /**
     * Get all files from db
     * @return Collection of files
     * @throws SQLException
     */
    Collection<File> getAllFiles() throws SQLException;

    /**
     * Return file from db by id
     * @param id
     * @return File
     * @throws SQLException
     */
    File getFileById(int id) throws SQLException;

    /**
     * Helper method for parsing file from result from db
     * @param rs
     * @return File from db
     * @throws SQLException
     */
    File getFile(ResultSet rs) throws SQLException;

    /**
     * Update file in db
     * @param editedFile
     * @throws SQLException
     */
    void updateFile(File editedFile) throws SQLException;

    /**
     * Delete file from db
     * @param editedFile
     * @throws SQLException
     */
    void deleteFile(File editedFile) throws SQLException;
}

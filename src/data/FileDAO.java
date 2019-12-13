package data;

import model.File;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface FileDAO {

    void insertFile(File file) throws SQLException;

    Collection<File> getAllFiles() throws SQLException;

    File getFileById(int id) throws SQLException;

    public File getFile(ResultSet rs) throws SQLException;

    void updateFile(File editedFile) throws SQLException;

    void deleteFile(File editedFile) throws SQLException;
}

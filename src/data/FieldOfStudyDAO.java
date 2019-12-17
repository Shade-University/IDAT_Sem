package data;

import model.Field;
import model.Subject;

import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author user
 */
public interface FieldOfStudyDAO {

    /**
     * Get all fields
     * @return Collection of all fields in db
     * @throws SQLException
     */
    Collection<Field> getAllFields() throws SQLException;

    /**
     * Get fields by subject
     * @param subject
     * @return Collection of fields from subject
     * @throws SQLException
     */
    Collection<Field> getFieldsBySubject(Subject subject) throws SQLException;

    /**
     * Insert field to db
     * @param field
     */
    void deleteField(Field field) throws SQLException;

    /**
     * Delete field from db
     * @param field
     * @throws SQLException
     */
    void insertField(Field field) throws SQLException;

    /**
     * Update field in db
     * @param field
     * @throws SQLException
     */
    void updateField(Field field)  throws SQLException;
}

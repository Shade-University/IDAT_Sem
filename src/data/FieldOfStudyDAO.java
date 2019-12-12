package data;

import java.sql.SQLException;
import java.util.Collection;
import model.Field;
import model.Subject;

/**
 *
 * @author user
 */
public interface FieldOfStudyDAO {
    
    public Collection<Field> getAllFields() throws SQLException;

    public Collection<Field> getFieldsBySubjects(Subject subject) throws SQLException;

    public void deleteField(Field obor);
    
    public void insertField(Field obor) throws SQLException;
    
    public void updateField(Field obor)  throws SQLException;
}

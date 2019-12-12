package data;

import java.sql.SQLException;
import java.util.Collection;
import model.Field;

/**
 *
 * @author user
 */
public interface FieldOfStudyDAO {
    
    public Collection<Field> getAllFields();
    
    public void deleteField(Field obor);
    
    public void insertField(Field obor) throws SQLException;
    
    public void updateField(Field obor)  throws SQLException;
    
}

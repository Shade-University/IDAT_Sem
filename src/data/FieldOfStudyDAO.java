package data;

import java.util.Collection;
import model.Field;

/**
 *
 * @author user
 */
public interface FieldOfStudyDAO {
    
    public Collection<Field> getAllFields();
    
    public void deleteField(Field obor);
    
    public void insertField(Field obor);
    
    public void updateField(Field obor);
    
}

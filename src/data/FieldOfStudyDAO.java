package data;

import java.util.Collection;
import model.Obor;

/**
 *
 * @author user
 */
public interface FieldOfStudyDAO {
    
    public Collection<Obor> getAllFields();
    
    public void deleteField(Obor obor);
    
    public void insertField(Obor obor);
    
    public void updateField(Obor obor);
    
}

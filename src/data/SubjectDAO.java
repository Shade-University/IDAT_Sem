package data;

import java.util.Collection;
import java.util.List;
import model.Field;
import model.Subject;

/**
 *
 * @author user
 */
public interface SubjectDAO {
    
    public Collection<Subject> getAllSubjects();
    
    public Collection<Subject> getSubjectsForField(Field obor);
    
    public void insertSubjectsToField(List<Subject> predmety, Field obor);
    
    public void removeSubjectsFromField(List<Subject> predmety, Field obor);
}

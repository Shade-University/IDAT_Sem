package data;

import java.util.Collection;
import java.util.List;
import model.Obor;
import model.Predmet;

/**
 *
 * @author user
 */
public interface SubjectDAO {
    
    public Collection<Predmet> getAllSubjects();
    
    public Collection<Predmet> getSubjectsForField(Obor obor);
    
    public void insertSubjectsToField(List<Predmet> predmety, Obor obor);
    
    public void removeSubjectsFromField(List<Predmet> predmety, Obor obor);
}

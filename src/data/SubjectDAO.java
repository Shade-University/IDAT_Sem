package data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import model.*;

/**
 * @author user
 */
public interface SubjectDAO {

    public Collection<Subject> getAllSubjects() throws SQLException;

    public Collection<Subject> getSubjectsForField(Field obor) throws SQLException;

    public void insertSubject(Subject subject) throws SQLException;

    public void insertSubjectsToField(List<Subject> predmety, Field obor);

    public void insertSubjectsToField(Subject subject, Field fieldOfStudy) throws SQLException;

    public void insertTeacherToSubject(Subject subject, User teacher) throws SQLException;

    public void insertSubjectToGroup(Subject subject, Group group) throws SQLException;

    public void updateSubject(Subject subject) throws SQLException;

    public void removeSubjectsFromField(List<Subject> predmety, Field obor);

    public void removeSubjectsFromField(Subject subject, Field fieldOfStudy) throws SQLException;

    public void removeTeacherFromSubject(Subject subject, User teacher) throws SQLException;

    public void removeSubjectFromGroup(Subject subject, Group group) throws SQLException;

    public void removeSubject(Subject subject) throws SQLException;
}

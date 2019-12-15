package data;

import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * @author user
 */
public interface SubjectDAO {

    /**
     * Get all subjects from db
     * @return Collection of subject
     * @throws SQLException
     */
    Collection<Subject> getAllSubjects() throws SQLException;

    /**
     * Return sall subjects for field
     * @param field
     * @return Collection of subject
     * @throws SQLException
     */
    Collection<Subject> getSubjectsForField(Field field) throws SQLException;

    /**
     * Return all subjects for teacher
     * @param user
     * @return Collection of subject
     * @throws SQLException
     */
    Collection<Subject> getAllSubjectsByTeacher(User user) throws SQLException;

    /**
     * Insert subject in db
     * @param subject
     * @throws SQLException
     */
    void insertSubject(Subject subject) throws SQLException;

    /**
     * Insert subjects to field
     * @param subjects
     * @param field
     */
    void insertSubjectToField(List<Subject> subjects, Field field);

    /**
     * Insert subject to field
     * @param subject
     * @param fieldOfStudy
     * @throws SQLException
     */
    void insertSubjectToField(Subject subject, Field fieldOfStudy) throws SQLException;

    /**
     * Insert teacher to subject
     * @param subject
     * @param teacher
     * @throws SQLException
     */
    void insertTeacherToSubject(Subject subject, User teacher) throws SQLException;

    /**
     * Insert subject to group
     * @param subject
     * @param group
     * @throws SQLException
     */
    void insertSubjectToGroup(Subject subject, Group group) throws SQLException;

    /**
     * Update subject in db
     * @param subject
     * @throws SQLException
     */
    void updateSubject(Subject subject) throws SQLException;

    /**
     * Remove subjects from field
     * @param subjects
     * @param field
     */
    void removeSubjectsFromField(List<Subject> subjects, Field field);

    /**
     * Remove subject from field
     * @param subject
     * @param fieldOfStudy
     * @throws SQLException
     */
    void removeSubjectsFromField(Subject subject, Field fieldOfStudy) throws SQLException;

    /**
     * Remove teacher from subject
     * @param subject
     * @param teacher
     * @throws SQLException
     */
    void removeTeacherFromSubject(Subject subject, User teacher) throws SQLException;

    /**
     * Remove subject from group
     * @param subject
     * @param group
     * @throws SQLException
     */
    void removeSubjectFromGroup(Subject subject, Group group) throws SQLException;

    /**
     * Remove subject from db
     * @param subject
     * @throws SQLException
     */
    void removeSubject(Subject subject) throws SQLException;

    /**
     * Helper method to parse subject from result set
     * @param rs
     * @return
     * @throws SQLException
     */
    Subject getSubject(ResultSet rs) throws SQLException;

    /**
     * Insert subjects to teacher
     * @param subjects
     * @param teacher
     * @throws SQLException
     */
    void insertSubjectsToTeacher(List<Subject> subjects, Teacher teacher) throws SQLException;
}

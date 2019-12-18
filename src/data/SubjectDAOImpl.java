package data;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomáš Vondra
 */
public class SubjectDAOImpl implements SubjectDAO {

    private Connection conn;

    public SubjectDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection<Subject> getAllSubjects() throws SQLException {
        Collection<Subject> collection = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM PREDMETY");

        while (rs.next()) {
            Subject subject = getSubject(rs);
            collection.add(subject);
        }
        statement.close();
        System.out.println("GetAllSubjects");
        return collection;
    }

    @Override
    public Collection<Subject> getAllSubjectsByTeacher(User user) throws SQLException {
        Collection<Subject> collection = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement(
                "select * from PREDMETY p join UCITELE_PREDMETY up on p.ID_PREDMET = up.PREDMET_ID_PREDMET where up.UCITELE_ID_UCITEL =  ?");
        preparedStatement.setInt(1, user.getId());

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Subject subject = getSubject(rs);
            collection.add(subject);
        }
        preparedStatement.close();
        System.out.println("GetAllSubjectsByTeacher");
        return collection;
    }

    @Override
    public Collection<Subject> getSubjectsForField(Field field) throws SQLException {
        Collection<Subject> collection = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM OBOR_PREDMET op\n"
                        + "JOIN PREDMETY p ON p.id_predmet = op.predmet_id_predmet\n"
                        + "WHERE op.studijni_obor_id_obor = ?"
        );
        preparedStatement.setInt(1, field.getId());

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Subject subject = getSubject(rs);
            collection.add(subject);
        }
        preparedStatement.close();
        System.out.println("GetSujectsForField");
        return collection;
    }

    public Subject getSubject(ResultSet rs) throws SQLException {
        Subject subject = new Subject(
                rs.getInt("id_predmet"),
                rs.getString("nazev"),
                rs.getString("popis")
        );

        return subject;
    }

    @Override
    public void insertSubjectsToTeacher(List<Subject> subjects, Teacher teacher) throws SQLException {
        try {

            for (Subject subject : subjects) {
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "INSERT INTO UCITELE_PREDMETY(ucitele_id_ucitel, predmet_id_predmet)"
                                + "VALUES (?, ?)"
                );
                preparedStatement.setInt(1, teacher.getId());
                preparedStatement.setInt(2, subject.getId());

                preparedStatement.executeUpdate();
                preparedStatement.close();
                conn.commit();
                System.out.println("insertSubjectsToTeacher");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertSubject(Subject subject) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call insert_predmet(?,?)"
        );
        callableStatement.setString(1, subject.getName());
        callableStatement.setString(2, subject.getDescription());
        callableStatement.executeQuery();

        callableStatement.close();
        conn.commit();
        System.out.println("InsertSubject");
    }

    @Override
    public void insertSubjectToField(List<Subject> subjects, Field field) {
        try {

            for (Subject subject : subjects) {
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "INSERT INTO OBOR_PREDMET(studijni_obor_id_obor, predmet_id_predmet)"
                                + "VALUES (?, ?)"
                );
                preparedStatement.setInt(1, field.getId());
                preparedStatement.setInt(2, subject.getId());

                preparedStatement.executeUpdate();
                preparedStatement.close();
                conn.commit();
                System.out.println("InsertSubjectsToField");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertSubjectToField(Subject subject, Field fieldOfStudy) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call insert_obor_predmet(?,?)"
        );
        callableStatement.setInt(1, fieldOfStudy.getId());
        callableStatement.setInt(2, subject.getId());

        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("InsertSubjectsToField");
    }

    @Override
    public void insertTeacherToSubject(Subject subject, User teacher) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call insert_predmet_ucitel(?,?)"
        );
        callableStatement.setInt(1, teacher.getId());
        callableStatement.setInt(2, subject.getId());

        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("InsertTeacherToSubject");
    }

    @Override
    public void insertSubjectToGroup(Subject subject, Group group) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call   insert_skupiny_predmety(?,?)"
        );
        callableStatement.setInt(1, group.getId());
        callableStatement.setInt(2, subject.getId());

        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("insertSubjectToGroup");
    }

    @Override
    public void updateSubject(Subject subject) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call update_predmet(?,?,?)"
        );
        callableStatement.setInt(1, subject.getId());
        callableStatement.setString(2, subject.getName());
        callableStatement.setString(3, subject.getDescription());

        callableStatement.executeUpdate();
        conn.commit();
        callableStatement.close();
        System.out.println("UpdateSubject");
    }

    @Override
    public void removeSubjectsFromField(List<Subject> subjects, Field field) {
        try {

            for (Subject subject : subjects) {
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "DELETE FROM OBOR_PREDMET "
                                + "WHERE predmet_id_predmet = ? AND "
                                + "studijni_obor_id_obor = ?"
                );
                preparedStatement.setInt(1, subject.getId());
                preparedStatement.setInt(2, field.getId());
                preparedStatement.setInt(1, subject.getId());

                preparedStatement.executeQuery();
                conn.commit();
                preparedStatement.close();
                System.out.println("RemoveSujectsFromField");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeSubjectsFromField(Subject subject, Field fieldOfStudy) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_obor_predmet(?,?)"
        );
        callableStatement.setInt(1, subject.getId());
        callableStatement.setInt(2, fieldOfStudy.getId());

        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("RemoveSubjectsFromField");
    }

    @Override
    public void removeTeacherFromSubject(Subject subject, User teacher) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_ucitele_predmety(?,?)"
        );
        callableStatement.setInt(1, subject.getId());
        callableStatement.setInt(2, teacher.getId());

        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("RemoveTeacherFromSubject");
    }

    @Override
    public void removeSubjectFromGroup(Subject subject, Group group) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_skupiny_predmety(?,?)"
        );
        callableStatement.setInt(1, subject.getId());
        callableStatement.setInt(2, group.getId());

        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("RemoveSubjectFromGroup");
    }

    @Override
    public void removeSubject(Subject subject) throws SQLException{
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_predmet(?)"
        );
        callableStatement.setInt(1, subject.getId());

        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("RemoveSubject");
    }

    @Override
    public List<Subject> getSubjectsForGroup(int id_group) throws SQLException {
        List<Subject> subjects = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM getPredmetyVeSkupine WHERE id_skupina = ?"
        );
        preparedStatement.setInt(1, id_group);

        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
            Subject subject = getSubject(rs);
            subjects.add(subject);
        }
        preparedStatement.close();
        System.out.println("GetSubjectsForGroup");
        return subjects;
    }

    @Override
    public void updateTeacherSubjects(Teacher user) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM UCITELE_PREDMETY" +
                        " WHERE UCITELE_ID_UCITEL = ?"
        );
        preparedStatement.setInt(1, user.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();

        for (Subject subject : user.getSubjects()) {
            preparedStatement = conn.prepareStatement(
                    "INSERT INTO UCITELE_PREDMETY (UCITELE_ID_UCITEL, PREDMET_ID_PREDMET) " +
                            "VALUES (?,?)"
            );
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, subject.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        System.out.println("UpdateTeacherSubjects");
    }

    @Override
    public void updateGroupSubjects(Group group) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM SKUPINY_PREDMETY" +
                        " WHERE SKUPINY_ID_SKUPINA = ?"
        );
        preparedStatement.setInt(1, group.getId());

        preparedStatement.executeUpdate();
        preparedStatement.close();

        for (Subject subject : group.getSubject()) {
            preparedStatement = conn.prepareStatement(
                    "INSERT INTO SKUPINY_PREDMETY (SKUPINY_ID_SKUPINA, PREDMETY_ID_PREDMET) " +
                            "VALUES (?,?)"
            );
            preparedStatement.setInt(1, group.getId());
            preparedStatement.setInt(2, subject.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        System.out.println("UpdateGroupSubjects");
    }

}

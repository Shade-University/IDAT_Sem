package data;

import controller.enums.USER_TYPE;
import model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tomáš Vondra
 */
public class UserDAOImpl implements UserDAO {

    private Connection conn;
    private final SubjectDAO subjectDAO = new SubjectDAOImpl();

    public UserDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection<User> getAllUsers() throws SQLException {
        Collection<User> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM getUzivatele");

        while (rs.next()) {
            User user = getUser(rs);
            collection.add(user);
        }
        statement.close();
        System.out.println("GetAllUsers");
        return collection;
    }

    @Override
    public Collection<User> getTeachers() throws SQLException {
        Collection<User> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM GETUCITELE");

        while (rs.next()) {
            User user = getUser(rs);
            collection.add(user);
        }
        statement.close();
        System.out.println("GetTeachers");

        return collection;
    }

    @Override
    public User getUserByLogin(String email, String password) throws SQLException {
        //Hash password
        CallableStatement callableStatement = conn.prepareCall(
                "{ ? = call fnc_zahashuj_uzivatele(?,?) }"
        );
        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.setString(2, email);
        callableStatement.setString(3, password);

        callableStatement.executeQuery();
        String hashedPassword = callableStatement.getString(1);
        //Hash password from function from db

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM getUzivatele WHERE email=? AND heslo=?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, hashedPassword);
        //Compare hashed password with db password

        ResultSet rs = preparedStatement.executeQuery();
        User user = null;
        if (rs.next())
            user = getUser(rs);

        callableStatement.close();
        preparedStatement.close();
        System.out.println("GetUserByLogin");
        return user;
    }

    @Override
    public User getUserById(int userID) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM getUzivatele WHERE ID_UZIVATEL = ?");
        preparedStatement.setInt(1, userID);

        ResultSet rs = preparedStatement.executeQuery();
        User user = null;
        if (rs.next())
            user = getUser(rs);
        preparedStatement.close();
        System.out.println("GetUserById");
        return user;
    }

    @Override
    public Collection<User> getAllUsersFromGroup(Group group) throws SQLException {
        Collection<User> collection = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM getUzivateleVeSkupine g WHERE g.id_skupina = ?"
        );
        preparedStatement.setInt(1, group.getId());

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            User user = getUser(rs);
            collection.add(user);
        }
        preparedStatement.close();
        System.out.println("GetAllUsersFromGroup");
        return collection;
    }

    @Override
    public Collection<User> getTeachersBySubject(Subject subject) throws SQLException {
        Collection<User> collection = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM getUciteleSPredmetem g WHERE g.id_predmet = ?"
        );
        preparedStatement.setInt(1, subject.getId());

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            User user = getUser(rs);
            collection.add(user);
        }
        preparedStatement.close();
        System.out.println("GetTeachersBySubject");
        return collection;
    }

    @Override
    public User getUser(ResultSet rs) throws SQLException {
        //TODO Admin vlastní tabulka a třída
        User user = null;
        USER_TYPE type = USER_TYPE.get(rs.getString("uzivatel_typ"));

        switch (type) {
            case ADMIN:
                user = new User(
                        rs.getInt("id_uzivatel"),
                        rs.getString("jmeno"),
                        rs.getString("prijmeni"),
                        rs.getString("email"),
                        rs.getDate("datum_vytvoreni"),
                        type,
                        readImage(rs.getBlob("avatar"))
                );
                break;
            case STUDENT:
                user = new Student(
                        new Field(
                                rs.getInt("id_obor"),
                                rs.getString("nazev_obor"),
                                rs.getString("popis_obor")
                        ),
                        rs.getString("rok_studia"),
                        rs.getInt("id_uzivatel"),
                        rs.getString("jmeno"),
                        rs.getString("prijmeni"),
                        rs.getString("email"),
                        rs.getDate("datum_vytvoreni"),
                        readImage(rs.getBlob("avatar"))
                );
                break;
            case TEACHER:
                user = new Teacher(getSubjects(rs.getInt("id_uzivatel")),
                        rs.getString("katedra"),
                        rs.getInt("id_uzivatel"),
                        rs.getString("jmeno"),
                        rs.getString("prijmeni"),
                        rs.getString("email"),
                        rs.getDate("datum_vytvoreni"),
                        readImage(rs.getBlob("avatar"))
                );
                break;
            default:
                break;
        }
        return user;
    } //Parser method for users

    private List<Subject> getSubjects(int id_user) {
        List<Subject> collection = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM getVyucovanePredmety " +
                            "where id_uzivatel = ?"
            );
            preparedStatement.setInt(1, id_user);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Subject subject = subjectDAO.getSubject(rs);
                collection.add(subject);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("getSubjects");
        return collection;
    } //Parser method for subject

    @Override
    public BufferedImage readImage(Blob img) throws SQLException {
        if (img == null)
            return null;

        int blobLength = (int) img.length();
        byte[] blobAsBytes = img.getBytes(1, blobLength);

        try {
            return ImageIO.read(new ByteArrayInputStream(blobAsBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    } //Helper method for reading blob as image

    @Override
    public void updateUser(User user) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE Uzivatele SET "
                        + "jmeno = ?, "
                        + "prijmeni = ?, "
                        + "email = ? "
                        + "WHERE id_uzivatel = ?"
        );
        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setInt(4, user.getId());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("UpdateUser");
    }

    @Override
    public void insertUser(User user) throws SQLException {

        if (user instanceof Teacher) {
            insertUcitel((Teacher) user);
        } else if (user instanceof Student) {
            insertStudent((Student) user);
        } else {
            insertAdmin(user);
        }

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT MAX(id_uzivatel) \"id\" FROM UZIVATELE");
        //Get last inserted user id and set it
        if (rs.next()) {
            user.setId(rs.getInt("id"));
        }
        statement.close();
        System.out.println("InsertUser");
    }

    private void insertUcitel(Teacher teacher) throws SQLException {
        CallableStatement preparedStatement = conn.prepareCall(
                "CALL insert_ucitel"
                        + "(?, ?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1, teacher.getFirstName());
        preparedStatement.setString(2, teacher.getLastName());
        preparedStatement.setString(3, teacher.getEmail());
        preparedStatement.setString(4, teacher.getPassword());
        preparedStatement.setDate(5, teacher.getDateCreated());
        preparedStatement.setString(6, teacher.getInstitute());

        preparedStatement.execute();
        conn.commit();
        preparedStatement.close();

        subjectDAO.insertSubjectsToTeacher(teacher.getSubjects(), teacher);
        System.out.println("Teacher inserted");
    }

    private void insertStudent(Student student) throws SQLException {
        CallableStatement preparedStatement = conn.prepareCall(
                "CALL insert_student"
                        + "(?, ?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1, student.getFirstName());
        preparedStatement.setString(2, student.getLastName());
        preparedStatement.setString(3, student.getEmail());
        preparedStatement.setString(4, student.getPassword());
        preparedStatement.setDate(5, student.getDateCreated());
        preparedStatement.setString(6, student.getStudyYear());
        preparedStatement.setInt(7, student.getField().getId());

        preparedStatement.executeQuery();
        conn.commit();
        preparedStatement.close();
        System.out.println("Student inserted");
    }

    @Override
    public void deleteUser(User user) throws SQLException {

        if (user instanceof Teacher) {
            deleteUcitel((Teacher) user);
        } else if (user instanceof Student) {
            deleteStudent((Student) user);
        } else {
            CallableStatement preparedStatement = conn.prepareCall(
                    "CALL delete_admin(?)"
            );
            preparedStatement.setInt(1, user.getId());

            preparedStatement.executeQuery();
            conn.commit();
            preparedStatement.close();
            System.out.println("Admin deleted");
        }

    }

    @Override
    public void updateAvatar(File image, User user) throws SQLException {
        InputStream in = null;
        try {
            in = new FileInputStream(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE Uzivatele SET "
                        + "avatar = ? "
                        + "WHERE id_uzivatel = ?"
        );
        preparedStatement.setBlob(1, in);
        preparedStatement.setInt(2, user.getId());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("Avatar updated");
    } //Helper method for update user avatar

    private void deleteStudent(Student student) throws SQLException {
        CallableStatement preparedStatement = conn.prepareCall(
                "CALL delete_student(?)"
        );
        preparedStatement.setInt(1, student.getId());

        preparedStatement.executeQuery();
        conn.commit();
        preparedStatement.close();
        System.out.println("Student deleted");
    }

    private void deleteUcitel(Teacher teacher) throws SQLException {
        CallableStatement preparedStatement = conn.prepareCall(
                "CALL delete_ucitel(?)"
        );
        preparedStatement.setInt(1, teacher.getId());

        preparedStatement.executeQuery();
        conn.commit();
        preparedStatement.close();
        System.out.println("Teacher deleted");
    }

    private void insertAdmin(User admin) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO UZIVATELE(jmeno, prijmeni, email, heslo, datum_vytvoreni, uzivatel_typ) "
                        + "VALUES (?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1, admin.getFirstName());
        preparedStatement.setString(2, admin.getLastName());
        preparedStatement.setString(3, admin.getEmail());
        preparedStatement.setString(4, admin.getPassword());
        preparedStatement.setDate(5, admin.getDateCreated());
        preparedStatement.setString(6, admin.getUserType().getType());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("Admin inserted");

    }
}

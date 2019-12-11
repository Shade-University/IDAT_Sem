package data;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import model.*;
import model.Group;

import javax.imageio.ImageIO;

/**
 * @author Tomáš Vondra
 */
public class UserDAOImpl implements UserDAO {

    private Connection conn;

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
        callableStatement.execute();
        String hashedPassword = callableStatement.getString(1);

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM getUzivatele WHERE email=? AND heslo=?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, hashedPassword);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next())
            return getUser(rs);

        return null;
    }

    @Override
    public Collection<User> getAllUsersFromGroup(Group group) throws SQLException {

        Collection<User> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM getUzivateleVeSkupine g WHERE g.id_skupina = " + group.getId());

        while (rs.next()) {
            User user = getUser(rs);
            collection.add(user);
        }

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
                user = new Teacher(new ArrayList<>(),
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
    } //Metoda rozparsuje a vytvoří uživatele

    private BufferedImage readImage(Blob img) throws SQLException {
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
    }

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
        System.out.println("User updated");
    }

    @Override
    public void insertUser(User user) throws SQLException {

        if (user instanceof Teacher) {
            //insertUcitel((Teacher) user);
        } else if (user instanceof Student) {
            insertStudent((Student) user);
        } else {
            insertAdmin(user);
        }

        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT MAX(id_uzivatel) \"id\" FROM UZIVATELE");
        if (rs.next()) {
            user.setId(rs.getInt("id"));
        }
    }

   /* private void insertUcitel(Teacher TEACHER) throws SQLException {
        CallableStatement preparedStatement = conn.prepareCall(
                "CALL insert_ucitel"
                        + "(?, ?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1, TEACHER.getFirstName());
        preparedStatement.setString(2, TEACHER.getLastName());
        preparedStatement.setString(3, TEACHER.getEmail());
        preparedStatement.setString(4, TEACHER.getPassword());
        preparedStatement.setDate(5, TEACHER.getDateCreated());
        preparedStatement.setString(6, TEACHER.getInstitute());
        preparedStatement.setInt(7, TEACHER.getVyucujici_Predmet().getId());

        preparedStatement.execute();
        conn.commit();
        System.out.println("Teacher inserted");
    } */

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

        preparedStatement.execute();
        conn.commit();
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

            preparedStatement.execute();
            conn.commit();
            System.out.println("Admin deleted");
        }

    } //TODO Občas používám preparedStatement, občas stm

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
        System.out.println("Avatar updated");
    }

    private void deleteStudent(Student student) throws SQLException {
        CallableStatement preparedStatement = conn.prepareCall(
                "CALL delete_student(?)"
        );
        preparedStatement.setInt(1, student.getId());

        preparedStatement.execute();
        conn.commit();
        System.out.println("Student deleted");
    }

    private void deleteUcitel(Teacher teacher) throws SQLException {
        CallableStatement preparedStatement = conn.prepareCall(
                "CALL delete_ucitel(?)"
        );
        preparedStatement.setInt(1, teacher.getId());

        preparedStatement.execute();
        conn.commit();
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
        System.out.println("Admin inserted");

    }

}

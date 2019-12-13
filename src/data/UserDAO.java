package data;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javafx.scene.image.Image;
import model.Group;
import model.Subject;
import model.User;

/**
 * @author user
 */
public interface UserDAO {

    public Collection<User> getAllUsers() throws SQLException;

    public Collection<User> getTeachers() throws SQLException;

    public Collection<User> getAllUsersFromGroup(Group skupina) throws SQLException;

    public Collection<User> getTeachersBySubject(Subject subject) throws SQLException;

    public User getUserById(int userID) throws SQLException;

    public User getUserByLogin(String email, String heslo) throws SQLException;

    public User getUser(ResultSet rs) throws SQLException;

    public void updateUser(User uzivatel) throws SQLException;

    public void insertUser(User uzivatel) throws SQLException;

    public void deleteUser(User uzivatel) throws SQLException;

    public void updateAvatar(File image, User user) throws SQLException;
}

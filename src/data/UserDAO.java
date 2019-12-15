package data;

import model.Group;
import model.Subject;
import model.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author user
 */
public interface UserDAO {

    /**
     * Get all users from db
     * @return Collection of user
     * @throws SQLException
     */
    Collection<User> getAllUsers() throws SQLException;

    /**
     * Get all teachers from db
     * @return Collection of user
     * @throws SQLException
     */
    Collection<User> getTeachers() throws SQLException;

    /**
     * Get all users in group
     * @param group
     * @return Collection of user
     * @throws SQLException
     */
    Collection<User> getAllUsersFromGroup(Group group) throws SQLException;

    /**
     * Get teachers by subject
     * @param subject
     * @return Collection of user
     * @throws SQLException
     */
    Collection<User> getTeachersBySubject(Subject subject) throws SQLException;

    /**
     * Get user by id
     * @param userID
     * @return User
     * @throws SQLException
     */
    User getUserById(int userID) throws SQLException;

    /**
     * Get user by login
     * @param email
     * @param password
     * @return User
     * @throws SQLException
     */
    User getUserByLogin(String email, String password) throws SQLException;

    /**
     * Helper method to parse user from result set
     * @param rs
     * @return User
     * @throws SQLException
     */
    User getUser(ResultSet rs) throws SQLException;

    /**
     * Update user in db
     * @param user
     * @throws SQLException
     */
    void updateUser(User user) throws SQLException;

    /**
     * Insert user to db
     * @param user
     * @throws SQLException
     */
    void insertUser(User user) throws SQLException;

    /**
     * Delete user from db
     * @param user
     * @throws SQLException
     */
    void deleteUser(User user) throws SQLException;

    /**
     * Update user avatar
     * @param image
     * @param user
     * @throws SQLException
     */
    void updateAvatar(File image, User user) throws SQLException;

    /**
     * Read image from blob
     * @param img
     * @return BufferedImage
     * @throws SQLException
     */
    BufferedImage readImage(Blob img) throws SQLException;
}

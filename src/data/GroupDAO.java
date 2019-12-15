package data;

import model.Group;
import model.Rating;
import model.Subject;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface GroupDAO {

    /**
     * Return all groups from db
     * @return Collection of group
     * @throws SQLException
     */
    Collection<Group> getAllGroups() throws SQLException;

    /**
     * Return all groups with user quantity from db
     * @return Collection of group
     * @throws SQLException
     */
    Collection<Group> getAllGroupWithUserQuantity() throws SQLException;

    /**
     * Return all groups from user
     * @param user
     * @return Collection of group
     * @throws SQLException
     */
    Collection<Group> getUserGroups(User user) throws SQLException;

    /**
     * Return all groups from subject
     * @param subject
     * @return Collection of group
     * @throws SQLException
     */
    Collection<Group> getSubjectGroups(Subject subject) throws SQLException;

    /**
     * Return group from rating
     * @param rt
     * @return Group
     * @throws SQLException
     */
    Group getRatedGroup(Rating rt) throws SQLException;

    /**
     * Return group by id
     * @param id
     * @return Group
     * @throws SQLException
     */
    Group getGroupById(int id) throws SQLException;

    /**
     * Helper method for parsing from result set
     * @param rs
     * @return Group
     * @throws SQLException
     */
    Group getGroup(ResultSet rs) throws SQLException;

    /**
     * Helper method for parsing from result set
     * @param rs
     * @return
     * @throws SQLException
     */
    Group getGroupWithQuantity(ResultSet rs) throws SQLException;

    /**
     * Update group in db
     * @param group
     * @throws SQLException
     */
    void updateGroup(Group group) throws SQLException;

    /**
     * Insert group to db
     * @param group
     * @throws SQLException
     */
    void insertGroup(Group group) throws SQLException;

    /**
     * Remove group from db
     * @param group
     * @throws SQLException
     */
    void removeGroup(Group group) throws SQLException;

    /**
     * Insert user to group in db
     * @param u
     * @param s
     * @throws SQLException
     */
    void insertUserToGroup(User u, Group s) throws SQLException;

    /**
     * Remove user from group in db
     * @param u
     * @param s
     * @throws SQLException
     */
    void removeUserFromGroup(User u, Group s) throws SQLException;
}

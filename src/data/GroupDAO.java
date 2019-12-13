package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import model.Group;
import model.Rating;
import model.Subject;
import model.User;

public interface GroupDAO {

    public Collection<Group> getAllGroups() throws SQLException;

    public Collection<Group> getAllGroupWithUserQuantity() throws SQLException;

    public Collection<Group> getUserGroups(User user) throws SQLException;

    public Collection<Group> getSubjectGroups(Subject subject) throws SQLException;

    public Group getRatedGroup(Rating rt) throws SQLException;

    public Group getGroupById(int id) throws SQLException;

    public Group getGroup(ResultSet rs) throws SQLException;

    public Group getGroupWithQuantity(ResultSet rs) throws SQLException;

    public void updateGroup(Group group) throws SQLException;

    public void insertUserToGroup(User u, Group s) throws SQLException;

    public void removeUserFromGroup(User u, Group s) throws SQLException;

    public void insertGroup(Group group) throws SQLException;

    public void removeGroup(Group group) throws SQLException;
}

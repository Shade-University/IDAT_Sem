package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import model.Group;
import model.User;

/**
 *
 * @author user
 */
public interface GroupDAO {
    
    public Collection<Group> getAllGroups() throws SQLException;
    
    public Collection<Group> getUserGroups(User user) throws SQLException;
    
    public Group getGroup(ResultSet rs) throws SQLException;
    
    public void updateGroup(Group group) throws SQLException;
    
    public void insertUserToGroup(User u, Group s) throws SQLException;
    
    public void insertGroup(Group group) throws SQLException;
    
    public void removeGroup(Group group) throws SQLException;
}

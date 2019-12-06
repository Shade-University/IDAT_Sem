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
public interface UserDAO {
    
    public Collection<User> getAllUsers() throws SQLException;
    
    public Collection<User> getAllUsersFromGroup(Group skupina) throws SQLException;
    
    public User getUserByLogin(String email, String heslo) throws SQLException;
    
    public User getUser(ResultSet rs) throws SQLException;
    
    public void updateUser(User uzivatel)  throws SQLException;
    
    public void insertUser(User uzivatel) throws SQLException;
    
    public void deleteUser(User uzivatel) throws SQLException;
    
}

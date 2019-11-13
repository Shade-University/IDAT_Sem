package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import model.Skupina;
import model.Uzivatel;

/**
 *
 * @author user
 */
public interface GroupDAO {
    
    public Collection<Skupina> getAllGroups();
    
    public Collection<Skupina> getUserGroups(Uzivatel user);
    
    public Skupina getGroup(ResultSet rs) throws SQLException;
    
    public void updateGroup(Skupina group);
    
    public void insertUserToGroup(Uzivatel u, Skupina s);
    
    public void insertGroup(Skupina group);
    
    public void removeGroup(Skupina group);
}

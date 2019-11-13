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
public interface UserDAO {
    
    public Collection<Uzivatel> getAllUsers();
    
    public Collection<Uzivatel> getAllUsersFromGroup(Skupina skupina);
    
    public Uzivatel getUserByLogin(String email, String heslo);
    
    public Uzivatel getUser(ResultSet rs) throws SQLException;
    
    public void updateUser(Uzivatel uzivatel);
    
    public void insertUser(Uzivatel uzivatel);
    
    public void deleteUser(Uzivatel uzivatel);
    
}

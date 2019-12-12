package data;

import java.sql.SQLException;
import java.util.Collection;
import model.Group;
import model.User;
import model.Message;

/**
 *
 * @author user
 */
public interface MessageDAO {
    
    public void createMessage(Message message) throws SQLException;

    public Collection<Message> getAllMessages() throws SQLException;

    public Collection<Message> getMessagesForChatBetween(User uzivatel1, User uzivatel2) throws SQLException;
    
    public Collection<Message> getMessagesForGroupChat(Group skupina) throws SQLException;
}

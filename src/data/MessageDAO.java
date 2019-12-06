package data;

import java.util.Collection;
import model.Group;
import model.User;
import model.Message;

/**
 *
 * @author user
 */
public interface MessageDAO {
    
    public void createMessage(Message message);
    
    public Collection<Message> getMessagesForChatBetween(User uzivatel1, User uzivatel2);
    
    public Collection<Message> getMessagesForGroupChat(Group skupina);
}

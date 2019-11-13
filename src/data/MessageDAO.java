package data;

import java.util.Collection;
import model.Skupina;
import model.Uzivatel;
import model.Zprava;

/**
 *
 * @author user
 */
public interface MessageDAO {
    
    public void createMessage(Zprava message);
    
    public Collection<Zprava> getMessagesForChatBetween(Uzivatel uzivatel1, Uzivatel uzivatel2);
    
    public Collection<Zprava> getMessagesForGroupChat(Skupina skupina);
}

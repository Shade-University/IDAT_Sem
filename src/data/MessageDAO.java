package data;

import model.Group;
import model.Message;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface MessageDAO {

    /**
     * Create message in db
     * @param message
     * @throws SQLException
     */
    void createMessage(Message message) throws SQLException;

    /**
     * Insert message to db
     * @param message
     * @throws SQLException
     */
    void insertMessage(Message message) throws SQLException;

    /**
     * Update message in db
     * @param message
     * @throws SQLException
     */
    void updateMessage(Message message) throws SQLException;

    /**
     * Delete message from db
     * @param message
     * @throws SQLException
     */
    void deleteMessage(Message message) throws SQLException;

    /**
     * Get collection of all messages from db
     * @return Collection of messages
     * @throws SQLException
     */
    Collection<Message> getAllMessages() throws SQLException;

    /**
     * Get message by id
     * @param id
     * @return Message
     * @throws SQLException
     */
    Message getMessageById(int id) throws SQLException;

    /**
     * Helper method to parse message from result set
     * @param rs
     * @return Message
     * @throws SQLException
     */
    Message getMessage(ResultSet rs) throws SQLException;

    /**
     * Get messages between two users
     * @param user1
     * @param user2
     * @return Collection of messages
     * @throws SQLException
     */
    Collection<Message> getMessagesForChatBetween(User user1, User user2) throws SQLException;

    /**
     * Get messages in group
     * @param skupina
     * @return Collection of messages
     * @throws SQLException
     */
    Collection<Message> getMessagesForGroupChat(Group skupina) throws SQLException;
}

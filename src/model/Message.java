
package model;

import data.OracleConnection;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Message model
 */
public class Message {

    private int id;
    private String name;
    private String content;
    private Date date_created;
    private User sender;

    private User user_receiver;
    private Group group_receiver;

    private int parent;
    private int level;
    private File attached_file;

    public Message(String name, String content, User sender, User user_receiver, Group group_receiver) {
        this(-1, name, content, sender, user_receiver, group_receiver,
                OracleConnection.parseDate(LocalDateTime.now().toString(), "yyyy-MM-dd'T'HH:mm:ss"));
    } //Konstruktor pro vytváření

    public Message(String name, String content, User sender, User user_receiver, Group group_receiver, Date date_created) {
        this(-1, name, content, sender, user_receiver, group_receiver,
                date_created);
    } //Konstruktor pro vytváření s datumem

    public Message(int id, String name, String content, User sender, User user_receiver, Group group_receiver, Date date_created) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.date_created = date_created;
        this.sender = sender;
        this.user_receiver = user_receiver;
        this.group_receiver = group_receiver;
    } //Konstrutor pro načítání

    public Message(int id, String name, String content, User sender, User user_receiver, Group group_receiver, Date date_created, int parent, File attached_file) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.date_created = date_created;
        this.sender = sender;
        this.user_receiver = user_receiver;
        this.group_receiver = group_receiver;
        this.parent = parent;
        this.attached_file = attached_file;
    } //Konstruktor pro načítání s rodičem

    public Message(int id, String name, String content, User sender, User user_receiver, Group group_receiver, Date date_created, int parent, File attached_file, int level) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.date_created = date_created;
        this.sender = sender;
        this.user_receiver = user_receiver;
        this.group_receiver = group_receiver;
        this.parent = parent;
        this.attached_file = attached_file;
        this.level = level;
    } //Konstruktor pro načítání se zanořením

    public Message() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getDate_created() {
        return date_created;
    }

    public User getSender() {
        return sender;
    }

    public User getUser_receiver() {
        return user_receiver;
    }

    public Group getGroup_receiver() {
        return group_receiver;
    }

    public int getParent() {
        return parent;
    }

    public File getAttached_file() {
        return attached_file;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(User us) {
        this.sender = us;
    }

    public void setDate_created(Date date) {
        this.date_created = date;
    }

    public void setUser_receiver(User us) {
        this.user_receiver = us;
    }

    public void setGroup_receiver(Group gp) {
        this.group_receiver = gp;
    }

    public void setParent(Message msg) {
        if (msg == null) {
            this.parent = 0;
        } else {
            this.parent = msg.getId();
        }
    }

    public void setAttached_file(File fl) {
        this.attached_file = fl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        if (message.user_receiver != null) {
            return id == message.id &&
                    parent == message.parent &&
                    name.equals(message.name) &&
                    content.equals(message.content) &&
                    date_created.equals(message.date_created) &&
                    sender.equals(message.sender) &&
                    user_receiver.equals(message.user_receiver);
        } else {
            return id == message.id &&
                    parent == message.parent &&
                    name.equals(message.name) &&
                    content.equals(message.content) &&
                    date_created.equals(message.date_created) &&
                    sender.equals(message.sender) &&
                    group_receiver.equals(message.group_receiver);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, content, date_created, sender, user_receiver, group_receiver, parent, attached_file);
    }

    @Override
    public String toString() {
        return getSender() + " => " + getContent();
    }


}

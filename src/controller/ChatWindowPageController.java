package controller;

import data.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Group;
import model.Message;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ChatWindowPageController implements Initializable {

    public TextArea txtAreaMessages;
    public ListView<User> listViewUsers;
    public TextField txtFieldNewMessage;

    private MessageDAO messageDAO = new MessageDAOImpl();
    private UserDAO userDao = new UserDAOImpl();
    private Group chattedGroup;

    public void setChatUsers(List<User> users) {
        this.listViewUsers.setItems(FXCollections.observableArrayList(users));
        refreshMessages();
    }

    public void setChatGroup(Group group) {
        chattedGroup = group;
        try {
            Collection<User> users = userDao.getAllUsersFromGroup(group);
            setChatUsers(new ArrayList<>(users));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshMessages(){
        txtAreaMessages.clear();
        Collection<Message> messages = new ArrayList<>();

        if(chattedGroup != null) {
            try {
                messages = messageDAO.getMessagesForGroupChat(chattedGroup);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                messages = messageDAO.getMessagesForChatBetween(MainDashboardPageController.getLoggedUser(), listViewUsers.getItems().get(0));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        for (Message m : messages) {
            txtAreaMessages.appendText(createMessageFormat(m));
        }
    }
    private String createMessageFormat(Message m) {
        return m.getDatum_vytvoreni().toString() + ":\t" + m.toString() + "\n";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnSendClicked(MouseEvent mouseEvent) {
        Message message = new Message(
                "Uživatelská zpráva",
                txtFieldNewMessage.getText(),
                MainDashboardPageController.getLoggedUser(),
                listViewUsers.getItems().get(0),
                null
        );

        try {
            messageDAO.createMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        refreshMessages();
    }
}

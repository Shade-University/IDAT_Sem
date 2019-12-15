package controller;

import data.MessageDAO;
import data.MessageDAOImpl;
import data.UserDAO;
import data.UserDAOImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Group;
import model.Message;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

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
        new Thread(() -> {
            try {
                Collection<User> users = userDao.getAllUsersFromGroup(group);
                Platform.runLater(() -> setChatUsers(new ArrayList<>(users)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void refreshMessages() {
        txtAreaMessages.clear();


        new Thread(() -> {
            Collection<Message> messages = new ArrayList<>();
            if (chattedGroup != null) {
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
            Collection<Message> finalMessages = messages;
            Platform.runLater(() -> {
                for (Message m : finalMessages) {
                    txtAreaMessages.appendText(createMessageFormat(m));
                }
            });
        }).start();
    }

    private String createMessageFormat(Message m) {
        return m.getDatum_vytvoreni().toString() + ":\t" + m.toString() + "\n";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnSendClicked(MouseEvent mouseEvent) {
        Message message = null;
        if (chattedGroup == null) {
            message = new Message(
                    "Uživatelská zpráva",
                    txtFieldNewMessage.getText(),
                    MainDashboardPageController.getLoggedUser(),
                    listViewUsers.getItems().get(0),
                    null
            );
        } else {
            message = new Message(
                    "Skupinová zpráva",
                    txtFieldNewMessage.getText(),
                    MainDashboardPageController.getLoggedUser(),
                    null,
                    chattedGroup
            );
        }

        try {
            messageDAO.createMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        refreshMessages();
    }
}

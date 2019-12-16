package controller;

import data.MessageDAO;
import data.MessageDAOImpl;
import data.UserDAO;
import data.UserDAOImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Group;
import model.Message;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class ChatWindowPageGroupController implements Initializable {

    @FXML
    public ListView<Message> lVMessages;
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
        lVMessages.setItems(null);


        new Thread(() -> {
            ObservableList<Message> list = null;
            if (chattedGroup != null) {
                try {
                    list = FXCollections.observableArrayList(messageDAO.getMessagesForGroupChatWithLevel(chattedGroup));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
              System.out.println("CHYBA");
            }
            ObservableList<Message> finalList = list;
            Platform.runLater(() -> {
                if (finalList != null) {
                    lVMessages.setItems(finalList);
                    lVMessages.setMouseTransparent(true);
                    lVMessages.setFocusTraversable(false);
                    lVMessages.setCellFactory(new Callback<ListView<Message>,
                                                      ListCell<Message>>() {
                                                  @Override
                                                  public ListCell<Message> call(ListView<Message> list) {
                                                      return new MessageListCellController();
                                                  }
                                              }
                    );
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

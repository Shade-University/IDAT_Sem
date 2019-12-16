package controller;

import controller.enums.RATING_GRADE;
import data.*;
import gui.AlertDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Group;
import model.Message;
import model.Rating;
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
    public VBox parentRating;
    public VBox boxRating;
    public ComboBox<RATING_GRADE> cBRatingOfGroup;
    public CheckBox checkBox;

    private MessageDAO messageDAO = new MessageDAOImpl();
    private RatingDAO ratingDAO = new RatingDAOImpl();
    private UserDAO userDao = new UserDAOImpl();
    private Group chatedGroup;
    private Message selectedMessage;
    private Rating groupRating;

    public void setChatUsers(List<User> users) {
        this.listViewUsers.setItems(FXCollections.observableArrayList(users));
        refreshMessages();
    }

    public void setChatGroup(Group group) {
        chatedGroup = group;
        new Thread(() -> {
            try {
                Collection<User> users = userDao.getAllUsersFromGroup(group);
                Platform.runLater(() -> setChatUsers(new ArrayList<>(users)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lVMessages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                checkBox.setDisable(false);
                checkBox.setSelected(true);
                selectedMessage = newValue;
            }
        });

        cBRatingOfGroup.setItems(FXCollections.observableArrayList(RATING_GRADE.values()));

        cBRatingOfGroup.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    if (groupRating != null) {
                        groupRating.setHodnota(RATING_GRADE.getPoints(cBRatingOfGroup.getValue()));
                        groupRating.setPopis(cBRatingOfGroup.getValue().toString());
                        ratingDAO.updateRating(groupRating);
                    } else {
                        groupRating = new Rating(-1,
                                RATING_GRADE.getPoints(cBRatingOfGroup.getValue()),
                                cBRatingOfGroup.getValue().toString(),
                                MainDashboardPageController.getLoggedUser(),
                                chatedGroup);
                        ratingDAO.createRating(groupRating);
                    }
                } catch (SQLException e)
                {
                    AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void refreshMessages() {
        if(chatedGroup==null){
            parentRating.getChildren().remove(boxRating);
        }
        checkBox.setDisable(true);
        checkBox.setSelected(false);
        new Thread(() -> {
            ObservableList<Message> list = null;
            if (chatedGroup != null) {
                try {
                    list = FXCollections.observableArrayList(messageDAO.getMessagesForGroupChatWithLevel(chatedGroup));
                     groupRating = ratingDAO.getRatingByUserAndGroup(MainDashboardPageController.getLoggedUser(),chatedGroup);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("CHYBA");
            }
            ObservableList<Message> finalList = list;
            Platform.runLater(() -> {
                if(groupRating!=null){
                    if(RATING_GRADE.convertToRATING_GRADE(groupRating)!=null){}
                    cBRatingOfGroup.setValue(RATING_GRADE.convertToRATING_GRADE(groupRating));
                } else {
                    cBRatingOfGroup.setValue(null);
                }
                if (finalList != null) {
                    lVMessages.setItems(null);
                    lVMessages.setItems(finalList);
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

    public void btnSendClicked(MouseEvent mouseEvent) {
        Message message = new Message(
                "Skupinová zpráva",
                txtFieldNewMessage.getText(),
                MainDashboardPageController.getLoggedUser(),
                null,
                chatedGroup
        );
        if (checkBox.isSelected() && !checkBox.isDisabled())
            message.setRodic(selectedMessage);
        try {
            messageDAO.createMessage(message);
            txtFieldNewMessage.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        refreshMessages();
    }
}

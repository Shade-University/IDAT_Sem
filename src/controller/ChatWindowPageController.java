package controller;

import controller.enums.RATING_GRADE;
import data.*;
import gui.AlertDialog;
import gui.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.Group;
import model.Message;
import model.Rating;
import model.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ChatWindowPageController implements Initializable {

    public ListView<Message> lVMessages;
    public ListView<User> listViewUsers;
    public TextField txtFieldNewMessage;
    public VBox parentRating;
    public VBox boxRating;
    public ComboBox<RATING_GRADE> cBRatingOfGroup;
    public CheckBox checkBox;
    public Button btnFileAdd;
    public Label labelAverageRatingOfGroup;

    private final MessageDAO messageDAO = new MessageDAOImpl();
    private final RatingDAO ratingDAO = new RatingDAOImpl();
    private final UserDAO userDao = new UserDAOImpl();
    private final FileDAO fileDao = new FileDAOImpl();

    private Group chatedGroup;
    private Message selectedMessage;
    private Rating groupRating;

    private ObservableList<Message> messages;
    private model.File attachedFile;

    //Set users to chatlist
    public void setChatUsers(List<User> users) {
        if (chatedGroup == null) {
            parentRating.getChildren().remove(boxRating);
        }

        this.listViewUsers.setItems(FXCollections.observableArrayList(users));
        refreshMessages();
    }

    //set users in group to chatlist and select chat with group
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
        }); //On select message, send message will be answering to this message (this message will be parent)

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
                    reloadRatingOfGroup();
                } catch (SQLException e) {
                    AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
                }
            }
        }); //On change rating, change rating in db
    }

    private void refreshMessages() {
        checkBox.setDisable(true);
        checkBox.setSelected(false);

        new Thread(() -> {
            if (chatedGroup != null) {
                try {
                    messages = FXCollections.observableArrayList(messageDAO.getMessagesForGroupChatWithLevel(chatedGroup));
                    groupRating = ratingDAO.getRatingByUserAndGroup(MainDashboardPageController.getLoggedUser(), chatedGroup);
                    reloadRatingOfGroup();
                    Platform.runLater(() -> {
                        if (groupRating != null) {
                            cBRatingOfGroup.setValue(RATING_GRADE.convertToRATING_GRADE(groupRating));
                        } else {
                            cBRatingOfGroup.setValue(null);
                        }
                        lVMessages.setItems(messages);
                        lVMessages.setCellFactory(list1 -> new MessageListCellController());
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Load messages for group
            } else {
                try {
                    messages = FXCollections.observableArrayList(
                            messageDAO.getMessagesForChatBetweenWithLevel(MainDashboardPageController.getLoggedUser(), listViewUsers.getItems().get(0)));
                    Platform.runLater(() -> {
                        lVMessages.setItems(messages);
                        lVMessages.setCellFactory(list1 -> new MessageListCellController());
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Load messages for user
            }
        }).start();
    }

    /**
     * Reload rating of Group.
     */
    public void reloadRatingOfGroup(){
        new Thread(() -> {
            double ratingOfGroup = 0;
            try {
                ratingOfGroup = ratingDAO.getAverageRating(chatedGroup);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            double finalRatingOfGroup = ratingOfGroup;
            Platform.runLater(() -> {
                labelAverageRatingOfGroup.setText("Průměrné hodnocení je: " + String.valueOf(finalRatingOfGroup));
            });
        }).start();
    }

    public void btnSendClicked(MouseEvent mouseEvent) {
        Message message = null;

        if (chatedGroup == null) {
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
                    chatedGroup
            );
        }
        message.setLevel(1);
        //Create message for group or user

        if(attachedFile != null) {
            try {
                int id = fileDao.insertFile(attachedFile);
                attachedFile.setId(id);
                message.setAttached_file(attachedFile);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } //Attach file if is attached

        if (checkBox.isSelected() && !checkBox.isDisabled()) {
            message.setParent(selectedMessage);
            message.setLevel(selectedMessage.getLevel() + 1);
            messages.add(messages.indexOf(selectedMessage) + 1, message);
        } else {
            messages.add(message);
        } //If answering to message, set level

        try {
            messageDAO.createMessage(message);
            txtFieldNewMessage.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lVMessages.refresh(); //Create message in db and refresh messages
    }

    public void btnFileAddClicked(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();

        File selectedFile = chooser.showOpenDialog(Main.primaryStage);

        if (selectedFile != null) {
            try {
                attachedFile = convertToModelFile(selectedFile);
                btnFileAdd.setText(attachedFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } //Select file

    private model.File convertToModelFile(File selectedFile) throws IOException {
        String fullName = selectedFile.getName();
        String extension = fullName.substring(fullName.lastIndexOf('.'), fullName.length());
        String name = fullName.substring(0, fullName.lastIndexOf('.'));
        byte[] content = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));

        return new model.File(
                name,
                extension,
                "Soubor ve zprávě",
                content,
                java.sql.Date.valueOf(LocalDate.now()),
                java.sql.Date.valueOf(LocalDate.now())
        );
    } //Convert file to sql file
}

package controller;

import controller.enums.RECIPIENT_TYPE;
import data.*;
import gui.AlertDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.File;
import model.Group;
import model.Message;
import model.User;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.ResourceBundle;

public class EditMessagePageController implements Initializable {

    private MessageDAO messageDAO = new MessageDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private FileDAO fileDAO = new FileDAOImpl();
    private GroupDAO groupDAO = new GroupDAOImpl();

    private ObservableList<Object> users;
    private AdministrationPageController parent;
    private MainDashboardPageController mdpc;
    private ToolboxForTeachersPageController tftp;
    private Tab tab;
    private Message editedMessage;
    /**
     * Called from
     * true -> Administration
     * false -> ToolboxForTeachers
     */
    private boolean calledFrom = true;

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Obnovení dat ve formuláři a předkovi
     *
     * @throws SQLException
     */
    private void exitPane() throws SQLException {
        editedMessage = null;
        parent.refreshMessage();
        parent.stackPaneEditMessage.getChildren().clear();
    }

    public TextField txtFieldMessageName;
    public ComboBox<Object> cBSender;
    public TextArea textAreaMessageBody;
    public Button btnSave;
    public Button btnDelete;
    public ComboBox<File> cBFile;
    public DatePicker dateMessagePicker;
    public ComboBox<Object> cBRecipientUniversal;
    public ComboBox<RECIPIENT_TYPE> cBRecipientType;
    public ComboBox<Message> cBMessageParent;


    public void initDataFromToolBox(Message msg, Tab tab, MainDashboardPageController mdpc, ToolboxForTeachersPageController tftp) {
        this.editedMessage = msg;
        this.tab = tab;
        this.mdpc = mdpc;
        this.tftp = tftp;
        calledFrom = false;
        initPane();
    }

    public void initDataFromAdministration(Message msg, AdministrationPageController apc) {
        this.editedMessage = msg;
        this.parent = apc;
        calledFrom = true;
        Thread t = new Thread(this::initPane);
        t.start(); //Init edited data
    }

    private void initPane() {
        if (editedMessage != null) {
            new Thread(() -> {
                try {
                    users = FXCollections.observableArrayList(userDAO.getAllUsers());
                    Collection<Message> messages = messageDAO.getAllMessages();
                    Collection<File> files = fileDAO.getAllFiles();
                    Platform.runLater(() -> {
                        cBMessageParent.setItems(FXCollections.observableArrayList(messages));
                        cBFile.setItems(FXCollections.observableArrayList(files));
                        cBSender.setItems(users);

                        cBSender.setValue(editedMessage.getSender());
                        cBFile.setValue(editedMessage.getAttached_file());
                        try {
                            cBMessageParent.setValue(messageDAO.getMessageById(editedMessage.getParent()));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start(); //Load messages data
            cBRecipientType.setItems(FXCollections.observableArrayList(RECIPIENT_TYPE.values()));
            if (editedMessage != null) {
                txtFieldMessageName.setText(editedMessage.getName());
                textAreaMessageBody.setText(editedMessage.getContent());
                dateMessagePicker.setValue(convertToLocalDate(editedMessage.getDate_created()));

                if (editedMessage.getUser_receiver() == null) {
                    cBRecipientType.setValue(RECIPIENT_TYPE.SKUPINA);
                    cBRecipientTypeChanged(null);
                    cBRecipientUniversal.setValue(editedMessage.getGroup_receiver());
                } else {
                    cBRecipientType.setValue(RECIPIENT_TYPE.UZIVATEL);
                    cBRecipientTypeChanged(null);
                    cBRecipientUniversal.setValue(editedMessage.getUser_receiver());
                }
            } else {
                cBRecipientType.setValue(RECIPIENT_TYPE.UZIVATEL);
                cBRecipientTypeChanged(null);
                btnDelete.setVisible(false);
                btnSave.setText("Vytvořit");
            } //Set up default values
        }
    }

    @FXML
    void btnSaveClicked(ActionEvent event) {
        try {
            Message msg = new Message();
            msg.setName(txtFieldMessageName.getText());
            msg.setContent(textAreaMessageBody.getText());
            msg.setSender((User) cBSender.getValue());
            msg.setDate_created(Date.valueOf(dateMessagePicker.getValue()));
            if (cBFile.getValue() != null) {
                msg.setAttached_file(cBFile.getValue());
            } else {
                msg.setAttached_file(null);
            }
            if (cBMessageParent.getValue() != null) {
                msg.setParent(cBMessageParent.getValue());
            } else {
                msg.setParent(null);
            }
            if (cBRecipientType.getValue() == RECIPIENT_TYPE.SKUPINA) {
                msg.setUser_receiver(null);
                msg.setGroup_receiver((Group) cBRecipientUniversal.getValue());
            } else {
                msg.setGroup_receiver(null);
                msg.setUser_receiver((User) cBRecipientUniversal.getValue());
            }
            if (cBRecipientUniversal.getValue() != null) {
                if (editedMessage != null) {
                    msg.setId(editedMessage.getId());
                    messageDAO.updateMessage(msg);

                } else {
                    messageDAO.insertMessage(msg);
                }
                if (calledFrom) {
                    exitPane();
                } else {
                    exitTab();
                }
            } else {
                AlertDialog.show("Zpráva nemá žádného příjemce", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            AlertDialog.show("Zadané údaje nejsou správné!", Alert.AlertType.ERROR);
        } //Create Message
    }

    private void exitTab() {
        mdpc.removeTab(tab);
        tftp.reloadMessagesByGroup(tftp.lVGroups.getSelectionModel().getSelectedItem());
    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {
        try {
            messageDAO.deleteMessage(editedMessage);
            if (calledFrom) {
                exitPane();
            } else {
                exitTab();
            }
        } catch (SQLException e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void cBRecipientTypeChanged(ActionEvent event) {
        cBRecipientUniversal.getItems().clear();

        if (cBRecipientType.getValue() == RECIPIENT_TYPE.SKUPINA) {
            new Thread(() -> {
                try {
                    Collection<Group> groups = groupDAO.getAllGroups();
                    Platform.runLater(() -> cBRecipientUniversal.setItems(FXCollections.observableArrayList(groups)));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            cBRecipientUniversal.setItems(users);
        } //On change reload data
    }

    @FXML
    void btnMessageParentNullClicked(ActionEvent event) {
        cBMessageParent.setValue(null);
    }

    @FXML
    void btnMessageFileNullClicked(ActionEvent event) {
        cBFile.setValue(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

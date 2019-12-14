package controller;

import controller.enums.RECIPIENT_TYPE;
import data.*;
import gui.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.File;
import model.Group;
import model.Message;
import model.User;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
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
    ObservableList<Object> users;
    private static AdministrationPageController parent;
    private static Message editedMessage;

    /**
     * Nastavení vstupních parametrů
     *
     * @param message - editovaná skupina
     * @param aP      - Předek ve kterém je zobrazený formulář
     */
    public static void setParams(Message message, AdministrationPageController aP) {
        editedMessage = message;
        parent = aP;
    }

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

    @FXML
    private TextField txtFieldMessageName;

    @FXML
    private ComboBox<Object> cBSender;

    @FXML
    private TextArea textAreaMessageBody;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox<File> cBFile;

    @FXML
    private DatePicker dateMessagePicker;

    @FXML
    private ComboBox<Object> cBRecipientUniversal;

    @FXML
    private ComboBox<RECIPIENT_TYPE> cBRecipientType;

    @FXML
    private ComboBox<Message> cBMessageParent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            users = FXCollections.observableArrayList(userDAO.getAllUsers());
            cBMessageParent.setItems(FXCollections.observableArrayList(messageDAO.getAllMessages()));
            cBFile.setItems(FXCollections.observableArrayList(fileDAO.getAllFiles()));
            cBSender.setItems(users);
            cBRecipientType.setItems(FXCollections.observableArrayList(RECIPIENT_TYPE.values()));
            if (editedMessage != null) {
                txtFieldMessageName.setText(editedMessage.getNazev());
                textAreaMessageBody.setText(editedMessage.getObsah());
                dateMessagePicker.setValue(convertToLocalDate(editedMessage.getDatum_vytvoreni()));
                cBSender.setValue(editedMessage.getOdesilatel());
                if (editedMessage.getPrijemce_uzivatel() == null) {
                    cBRecipientType.setValue(RECIPIENT_TYPE.SKUPINA);
                    cBRecipientTypeChanged(null);
                    cBRecipientUniversal.setValue(editedMessage.getPrijemce_skupina());
                } else {
                    cBRecipientType.setValue(RECIPIENT_TYPE.UZIVATEL);
                    cBRecipientTypeChanged(null);
                    cBRecipientUniversal.setValue(editedMessage.getPrijemce_uzivatel());
                }
                cBFile.setValue(editedMessage.getSoubor());
                cBMessageParent.setValue(messageDAO.getMessageById(editedMessage.getRodic()));
            } else {
                cBRecipientType.setValue(RECIPIENT_TYPE.UZIVATEL);
                cBRecipientTypeChanged(null);
                btnDelete.setVisible(false);
                btnSave.setText("Vytvořit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSaveClicked(ActionEvent event) {
        try {
            Message msg = new Message();
            msg.setNazev(txtFieldMessageName.getText());
            msg.setObsah(textAreaMessageBody.getText());
            msg.setOdesilatel((User) cBSender.getValue());
            msg.setDatum_vytvoreni(Date.valueOf(dateMessagePicker.getValue()));
            if (cBFile.getValue() != null) {
                msg.setSoubor(cBFile.getValue());
            } else {
                msg.setSoubor(null);
            }
            if (cBMessageParent.getValue() != null) {
                msg.setRodic(cBMessageParent.getValue());
            } else {
                msg.setRodic(null);
            }
            if (cBRecipientType.getValue() == RECIPIENT_TYPE.SKUPINA) {
                msg.setPrijemce_uzivatel(null);
                msg.setPrijemce_skupina((Group) cBRecipientUniversal.getValue());
            } else {
                msg.setPrijemce_skupina(null);
                msg.setPrijemce_uzivatel((User) cBRecipientUniversal.getValue());
            }
            if (cBRecipientUniversal.getValue() != null) {
                if(editedMessage!=null){
                    msg.setId(editedMessage.getId());
                     messageDAO.updateMessage(msg);

                } else {
                        messageDAO.insertMessage(msg);
                }
                exitPane();
            } else {
                AlertDialog.show("Zpráva nemá žádného příjemce", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            AlertDialog.show("Zadané údaje nejsou správné!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {
        try {
            messageDAO.deleteMessage(editedMessage);
            exitPane();
        } catch (SQLException e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void cBRecipientTypeChanged(ActionEvent event) throws SQLException {
        cBRecipientUniversal.setItems(null);
        if (cBRecipientType.getValue() == RECIPIENT_TYPE.SKUPINA) {
            cBRecipientUniversal.setItems(FXCollections.observableArrayList(groupDAO.getAllGroups()));
        } else {
            cBRecipientUniversal.setItems(users);
        }
    }

    @FXML
    void btnMessageParentNullClicked(ActionEvent event) {
        cBMessageParent.setValue(null);
    }

    @FXML
    void btnMessageFileNullClicked(ActionEvent event) {
        cBFile.setValue(null);
    }

}

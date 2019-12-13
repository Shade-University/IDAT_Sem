package controller;

import data.MessageDAO;
import data.MessageDAOImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Message;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditMessagePageController implements Initializable {

    private MessageDAO messageDAO = new MessageDAOImpl();
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

    @FXML
    private TextField txtFieldMessageName;

    @FXML
    private ComboBox<?> cBSender;

    @FXML
    private TextArea textAreaMessageBody;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<?> cBFile;

    @FXML
    private DatePicker dateMessagePicker;

    @FXML
    private ComboBox<?> cBRecipientUniversal;

    @FXML
    private Label lblError1112;

    @FXML
    private ComboBox<?> cBRecipientType;

    @FXML
    private ComboBox<Message> cBMessageParent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cBMessageParent.setItems(FXCollections.observableArrayList(messageDAO.getAllMessages()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSaveClicked(ActionEvent event) {

    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {

    }

    @FXML
    void cBRecipientTypeChanged(ActionEvent event) {

    }

    @FXML
    void btnMessageParentNullClicked(ActionEvent event) {

    }

    @FXML
    void btnMessageFileNullClicked(ActionEvent event) {

    }

}

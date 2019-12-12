package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Message;
import model.Rating;

import java.net.URL;
import java.util.ResourceBundle;

public class EditMessagePageController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private TextField txtFieldMessageName;

    @FXML
    private ComboBox<?> comboBoxRecipientUniversal;

    @FXML
    private ComboBox<?> comboBoxFile;

    @FXML
    private TextArea textAreaMessageBody;

    @FXML
    private ComboBox<?> comboBoxRecipientType;

    @FXML
    private ComboBox<?> comboBoxSender;

    @FXML
    private DatePicker dateMessagePicker;

    @FXML
    private Label lblError1112;

    @FXML
    private ComboBox<?> comboBoxMessageParent;

    @FXML
    void btnMessageSaveClicked(ActionEvent event) {

    }

    @FXML
    void e40b0b(ActionEvent event) {

    }

    @FXML
    void btnMessageDeleteClicked(ActionEvent event) {

    }

    @FXML
    void btnMessageParentNullClicked(ActionEvent event) {

    }

    @FXML
    void btnMessageFileNullClicked(ActionEvent event) {

    }

}

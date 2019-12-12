package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Group;
import model.Subject;

import java.net.URL;
import java.util.ResourceBundle;

public class EditSubjectPageController implements Initializable {


    private static AdministrationPageController parent;

    private static Subject editedSubject;

    /**
     * Nastavení vstupních parametrů
     *
     * @param subject - editovaná skupina
     * @param aP    - Předek ve kterém je zobrazený formulář
     */
    public static void setParams(Subject subject, AdministrationPageController aP) {
        editedSubject = subject;
        parent = aP;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private ComboBox<?> comboBoxAddSubjectToUniversal;

    @FXML
    private ComboBox<?> comboBoxSubjectsUniversal;

    @FXML
    private Button btnAddSubjectToUniversal;

    @FXML
    private TextField txtFieldSubjectName;

    @FXML
    private Label lblError111;

    @FXML
    private TextArea textAreaSubjectDescription;

    @FXML
    private ListView<?> listViewSubjectUniversal;

    @FXML
    void btnSubjectSaveClicked(ActionEvent event) {

    }

    @FXML
    void e40b0b(ActionEvent event) {

    }

    @FXML
    void btnSubjectDeleteClicked(ActionEvent event) {

    }

    @FXML
    void btnbtnAddSubjectToUniversalClicked(ActionEvent event) {

    }

}

package controller;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Field;
import model.Group;
import model.Subject;
import model.User;
import gui.*;

import javax.lang.model.type.ErrorType;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditSubjectPageController implements Initializable {

    private final SubjectDAO subjectDAO = new SubjectDAOImpl();
    private final GroupDAO groupDAO = new GroupDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private final FieldOfStudyDAO fieldDAO = new FieldOfStudyDAOImpl();
    private ObservableList<Group> groupsInSubject;
    private ObservableList<User> usersInSubject;
    private ObservableList<Field> fieldInSubject;
    private static AdministrationPageController parent;
    private static Subject editedSubject;

    private enum tableType {
        FIELD("Obory"),
        TEACHER("Učitelé"),
        GROUP("Skupiny");

        private String type;

        tableType(String typeForHuman) {
            this.type = typeForHuman;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }

    }

    @FXML
    private ComboBox<Object> comboBoxAddSubjectToUniversal;

    @FXML
    private ComboBox<tableType> comboBoxSubjectsUniversal;

    @FXML
    private Button btnAddSubjectToUniversal;

    @FXML
    private TextField txtFieldSubjectName;

    @FXML
    private TextArea textAreaSubjectDescription;

    @FXML
    private ListView<Object> listViewSubjectUniversal;


    /**
     * Nastavení vstupních parametrů
     *
     * @param subject - editovaná skupina
     * @param aP      - Předek ve kterém je zobrazený formulář
     */
    public static void setParams(Subject subject, AdministrationPageController aP) {
        editedSubject = subject;
        parent = aP;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (editedSubject != null) {
            txtFieldSubjectName.setText(editedSubject.getName());
            textAreaSubjectDescription.setText(editedSubject.getDescription());
            comboBoxSubjectsUniversal.setItems(FXCollections.observableArrayList(tableType.values()));
            comboBoxSubjectsUniversal.getSelectionModel().select(0);
            try {
                loadDataToUniversal();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            transformToCreatePage();
        }
    }

    /**
     * Slouží pro plnění společných komponent.
     *
     * @throws SQLException
     */
    private void loadDataToUniversal() throws SQLException {
        switch (comboBoxSubjectsUniversal.getValue()) {
            case TEACHER:
                usersInSubject = FXCollections.observableArrayList(userDAO.getTeachersBySubject(editedSubject));
                listViewSubjectUniversal.setItems(FXCollections.observableArrayList(usersInSubject));
                comboBoxAddSubjectToUniversal.setItems(FXCollections.observableArrayList(userDAO.getTeachers()));
                break;
            case FIELD:
                fieldInSubject = FXCollections.observableArrayList(fieldDAO.getFieldsBySubjects(editedSubject));
                listViewSubjectUniversal.setItems(FXCollections.observableArrayList(fieldInSubject));
                comboBoxAddSubjectToUniversal.setItems(FXCollections.observableArrayList(fieldDAO.getAllFields()));
                break;
            case GROUP:
                groupsInSubject = FXCollections.observableArrayList(groupDAO.getSubjectGroups(editedSubject));
                listViewSubjectUniversal.setItems(FXCollections.observableArrayList(groupsInSubject));
                comboBoxAddSubjectToUniversal.setItems(FXCollections.observableArrayList(groupDAO.getAllGroups()));
                break;
        }
        cbUniversalChanged(null);
        comboBoxAddSubjectToUniversal.getSelectionModel().select(0);
    }

    private void transformToCreatePage() {
    }

    private void reloadData() throws SQLException {
        editedSubject = null;
        parent.refreshSubject();
        parent.stackPaneEditSubject.getChildren().clear();
    }

    private boolean isMemberOf() {
        switch (comboBoxSubjectsUniversal.getValue()) {
            case TEACHER:
                return usersInSubject.contains(
                        comboBoxAddSubjectToUniversal.getValue());
            case FIELD:
                return fieldInSubject.contains(comboBoxAddSubjectToUniversal.getValue());
            case GROUP:
                return groupsInSubject.contains(comboBoxAddSubjectToUniversal.getValue());
            default:
                throw new NullPointerException();
        }
    }

    @FXML
    void cbSubjectTypeChanged(ActionEvent event) throws SQLException {
        loadDataToUniversal();
    }

    @FXML
    void btnSubjectSaveClicked(ActionEvent event) {
        try {
            editedSubject.setName(txtFieldSubjectName.getText());
            editedSubject.setDescription(textAreaSubjectDescription.getText());
            subjectDAO.updateSubject(editedSubject);
            AlertDialog.show("Úprava předmětu byla úspěšná!", Alert.AlertType.INFORMATION);
            reloadData();
        } catch (SQLException e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    void btnSubjectDeleteClicked(ActionEvent event) {
        try {
            subjectDAO.removeSubject(editedSubject);
            AlertDialog.show("Předmět byl úspěšně smazán!", Alert.AlertType.INFORMATION);
            reloadData();
        } catch (SQLException e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }


    //TODO dodělat
    @FXML
    void btnAddSubjectToUniversalClicked(ActionEvent event) throws SQLException {
        if(isMemberOf()){

        }else{
            switch (comboBoxSubjectsUniversal.getValue()) {
                case TEACHER:
                case FIELD:
                case GROUP:

            }
        }
    }

    /**
     * Sleduje, zda-li je objekt členem předmětu, a následně dle toho mění popis tlačítka:
     * True -> Odebrat
     * False -> Přidat
     */
    @FXML
    void cbUniversalChanged(ActionEvent event) {
        if (isMemberOf()) {
            btnAddSubjectToUniversal.setText("Odebrat");
        } else {
            btnAddSubjectToUniversal.setText("Přidat");
        }
    }

}

package controller;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import gui.*;

import javax.jws.soap.SOAPBinding;
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

    @FXML
    private Label labelAddTo;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;



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

    /**
     * Inicializace stránky a naplnění daty.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (editedSubject != null) {
            transformToCreatePage(false);
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
            transformToCreatePage(true);
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
        comboBoxAddSubjectToUniversal.getSelectionModel().select(0);
        cbUniversalChanged(null);

    }


    /**
     * Skrývání a zobrazování prvků formuláře dle Create/UD
     * @param bool
     */
    private void transformToCreatePage(boolean bool) {
        labelAddTo.setVisible(!bool);
        comboBoxSubjectsUniversal.setVisible(!bool);
        btnInsert.setVisible(bool);
        listViewSubjectUniversal.setVisible(!bool);
        comboBoxAddSubjectToUniversal.setVisible(!bool);
        btnAddSubjectToUniversal.setVisible(!bool);
        btnDelete.setVisible(!bool);
        btnSave.setVisible(!bool);
    }

    /**
     * Odebrání součásného formůláře z rodiče a reload.
     * @throws SQLException
     */
    private void reloadData() throws SQLException {
        editedSubject = null;
        parent.refreshSubject();
        parent.stackPaneEditSubject.getChildren().clear();
    }

    /**
     * Zjištění, jestli je daný objekt členem kolekce
     * @return True/false
     */
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
                return true;
        }
    }

    /**
     * Změna typu
     * @param event
     * @throws SQLException
     */
    @FXML
    void cbSubjectTypeChanged(ActionEvent event) throws SQLException {
        loadDataToUniversal();
    }


    /**
     * Stisknutí tlačítka uložit a následný update předmětu.
     * @param event
     */
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

    /**
     * Smazání předmětu
     * @param event
     */
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

    /**
     * Přidání/odebrání objektu do určité kolekce
     * Teacher/FieldOfStudy/Group
     * @param event
     * @throws SQLException
     */
    @FXML
    void btnAddSubjectToUniversalClicked(ActionEvent event) throws SQLException {
        try {
            if (isMemberOf()) {
                switch (comboBoxSubjectsUniversal.getValue()) {
                    case TEACHER:
                        subjectDAO.removeTeacherFromSubject(editedSubject, (User) comboBoxAddSubjectToUniversal.getValue());
                        break;
                    case FIELD:
                        subjectDAO.removeSubjectsFromField(editedSubject, (Field) comboBoxAddSubjectToUniversal.getValue());
                        break;
                    case GROUP:
                        subjectDAO.removeSubjectFromGroup(editedSubject, (Group) comboBoxAddSubjectToUniversal.getValue());
                        break;
                    default:
                        AlertDialog.show("Odebrání se nezdařilo!", Alert.AlertType.ERROR);
                }
            } else {
                switch (comboBoxSubjectsUniversal.getValue()) {
                    case TEACHER:
                        subjectDAO.insertTeacherToSubject(editedSubject, (User) comboBoxAddSubjectToUniversal.getValue());
                        break;
                    case FIELD:
                        subjectDAO.insertSubjectsToField(editedSubject, (Field) comboBoxAddSubjectToUniversal.getValue());
                        break;
                    case GROUP:
                        subjectDAO.insertSubjectToGroup(editedSubject, (Group) comboBoxAddSubjectToUniversal.getValue());
                        break;
                    default:
                        AlertDialog.show("Přidání se nezdařilo!", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        loadDataToUniversal();
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

    /**
     * Vložení nového předmětu
     * @param event
     * @throws SQLException
     */
    @FXML
    private void btnInsertClicked(ActionEvent event) throws SQLException {
        try {
            Subject sb = new Subject(0, txtFieldSubjectName.getText(), textAreaSubjectDescription.getText());
            subjectDAO.insertSubject(sb);
            AlertDialog.show("Skupina byla úspěšně vytvořena.", Alert.AlertType.INFORMATION);
            reloadData();
        } catch (SQLException e){
            AlertDialog.show("e", Alert.AlertType.ERROR);
        }

    }

}

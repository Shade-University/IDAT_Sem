package controller;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.*;

import javax.jws.soap.SOAPBinding;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class EditGroupPageController implements Initializable {

    @FXML
    private TextArea textAreaGroupDescription;
    @FXML
    private TextField txtFieldGroupName;
    @FXML
    private Button btnAddUserToGroup;
    @FXML
    private ListView<User> listViewUsersInGroup;
    @FXML
    private ComboBox<User> comboBoxAddUserToGroup;

    private final GroupDAO groupDAO = new GroupDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private ObservableList<User> usersInGroup;

    private static Group editedGroup;

    public static void setEditedGroup(Group group) {
        editedGroup = group;
    }

    public static Group getEditedGroup() {
        return editedGroup;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
    }


    /**
     * Inicializace formuláře
     */
    private void initData() {
        if (editedGroup != null) {
            txtFieldGroupName.setText(editedGroup.getName());
            textAreaGroupDescription.setText(editedGroup.getDescription());
            try {
                usersInGroup = FXCollections.observableArrayList(userDAO.getAllUsersFromGroup(editedGroup));
                comboBoxAddUserToGroup.setItems(FXCollections.observableArrayList(userDAO.getAllUsers()));
                listViewUsersInGroup.setItems(usersInGroup);
            } catch (SQLException e) {
                System.out.println(e.getStackTrace());
            }
        }
    }

    /**
     * Logika sloužící pro ověření, zda-li je uživatel členem skupiny.
     *
     * @return True -> je členem.
     */
    private boolean isMemberOf() {
        return usersInGroup.contains(comboBoxAddUserToGroup.getValue());
    }

    @FXML
    void btnGroupSaveClicked(ActionEvent event) {

    }

    @FXML
    void e40b0b(ActionEvent event) {

    }

    //TODO Dodělat REMOVE a UPDATE
    @FXML
    void btnGroupDeleteClicked(ActionEvent event) throws SQLException {
        groupDAO.removeGroup(editedGroup);
        editedGroup = null;
        initData();
    }


    /**
     * Sleduje, zda-li je uživatel členem skupiny, a následně dle toho mění popis tlačítka:
     * True -> Odebrat uživatele
     * False -> Přidat uživatele
     */
    @FXML
    void selectionCBChangedAddUserToGroup(ActionEvent event) {
        if (isMemberOf()) {
            btnAddUserToGroup.setText("Odebrat uživatele");
        } else {
            btnAddUserToGroup.setText("Přidat uživatele");
        }
    }

    /**
     * Logika ošetřující stisknutí tlačítka "přidat/odebrat uživatele", která zkoumá, jestli je uživatel členem skupiny.
     */
    @FXML
    void btnGroupUserAddClicked(ActionEvent event) throws SQLException {
        if (isMemberOf()) {
            groupDAO.removeUserFromGroup(comboBoxAddUserToGroup.getValue(), editedGroup);
        } else {
            groupDAO.insertUserToGroup(comboBoxAddUserToGroup.getValue(), editedGroup);
        }
        comboBoxAddUserToGroup.getSelectionModel().clearSelection();
        initData();
    }

}

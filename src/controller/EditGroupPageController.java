package controller;

import data.GroupDAO;
import data.GroupDAOImpl;
import data.UserDAO;
import data.UserDAOImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Group;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;


public class EditGroupPageController implements Initializable {

    public TextArea textAreaGroupDescription;
    public TextField txtFieldGroupName;
    public Button btnAddUserToGroup;
    public ListView<User> listViewUsersInGroup;
    public ComboBox<User> comboBoxAddUserToGroup;
    public Button btnSave;
    public Button btnDelete;
    public Label labelUs;
    public Label labelCs;
    public GridPane grid;
    public Button btnInsert;


    private final GroupDAO groupDAO = new GroupDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    private ObservableList<User> usersInGroup;

    private static AdministrationPageController parent;
    private static Group editedGroup;


    /**
     * Nastavení vstupních parametrů
     *
     * @param group - editovaná skupina
     * @param aP    - Předek ve kterém je zobrazený formulář
     */
    public static void setParams(Group group, AdministrationPageController aP) {
        editedGroup = group;
        parent = aP;
    }

    /**
     * Slouží pro vrácení editované skupiny.
     *
     * @return vrátí načtenou skupinu
     */
    public static Group getEditedGroup() {
        return editedGroup;
    }

    /**
     * Inicializace formuláře
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
    }


    /**
     * Naplnění formuláře daty
     */
    private void initData() {
        if (editedGroup != null) {
            btnInsert.setVisible(false);
            txtFieldGroupName.setText(editedGroup.getName());
            textAreaGroupDescription.setText(editedGroup.getDescription());
            new Thread(() -> {
                try {
                    usersInGroup = FXCollections.observableArrayList(userDAO.getAllUsersFromGroup(editedGroup));
                    Collection<User> users = userDAO.getAllUsers();
                    Platform.runLater(() -> {
                        comboBoxAddUserToGroup.setItems(FXCollections.observableArrayList(users));
                        comboBoxAddUserToGroup.getSelectionModel().select(1);
                        listViewUsersInGroup.setItems(usersInGroup);
                        selectionCBChangedAddUserToGroup(null);
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start(); //Load group data
        } else {
            transformToCreatePage();
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

    private void transformToCreatePage() {
        txtFieldGroupName.setText("");
        textAreaGroupDescription.setText("");
        listViewUsersInGroup.setVisible(false);
        comboBoxAddUserToGroup.setVisible(false);
        btnAddUserToGroup.setVisible(false);
        labelCs.setVisible(false);
        labelUs.setVisible(false);
        btnDelete.setVisible(false);
        btnSave.setVisible(false);
    } //Set data for create

    /**
     * Vytvoří Alert dialog
     *
     * @param message - Zobrazovaná zpráva
     * @param type    - Typ dialogu information/ERROR
     */
    private void displayAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        if (type == Alert.AlertType.ERROR) {
            alert.setTitle("Chyba při provádění požadované akce");
            alert.setHeaderText("Chyba při provádění požadované akce");
        } else {
            alert.setTitle("Operace byla provedena");
            alert.setHeaderText(null);
        }
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Obnovení dat ve formuláři a předkovi
     *
     * @throws SQLException
     */
    private void reloadData() throws SQLException {
        editedGroup = null;
        parent.refreshGroups();
        parent.stackPaneEditGroup.getChildren().clear();
    }


    /**
     * Provede UPDATE skupiny
     */
    @FXML
    void btnGroupSaveClicked(ActionEvent event) {
        try {
            editedGroup.setName(txtFieldGroupName.getText());
            editedGroup.setDescription(textAreaGroupDescription.getText());
            groupDAO.updateGroup(editedGroup);
            displayAlert("Úprava skupiny byla úspěšná!", Alert.AlertType.INFORMATION);
            reloadData();
        } catch (SQLException e) {
            displayAlert(e.toString(), Alert.AlertType.ERROR);
        }
    }


    /**
     * Vloží novou skupinu
     */
    @FXML
    void btnGroupInsertClicked(ActionEvent event) {
        try {
            Group ng = new Group(txtFieldGroupName.getText(), textAreaGroupDescription.getText(), null);
            groupDAO.insertGroup(ng);
            displayAlert("Skupina byla úspěšně vytvořena.", Alert.AlertType.INFORMATION);
            reloadData();
        } catch (SQLException e) {
            displayAlert(e.toString(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Smaže skupinu z databáze včetně dat ze spojových tabulek.
     */
    @FXML
    void btnGroupDeleteClicked(ActionEvent event) throws SQLException {
        try {
            groupDAO.removeGroup(editedGroup);
            displayAlert("Skupina byla úspěšně smazána!", Alert.AlertType.INFORMATION);
            reloadData();
        } catch (SQLException e) {
            displayAlert(e.toString(), Alert.AlertType.ERROR);
        }
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

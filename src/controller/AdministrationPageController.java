package controller;

import data.FieldOfStudyDAO;
import data.FieldOfStudyDAOImpl;
import data.GroupDAO;
import data.GroupDAOImpl;
import data.SubjectDAO;
import data.SubjectDAOImpl;
import data.UserDAO;
import data.UserDAOImpl;
import gui.Main;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import model.Field;
import model.Group;
import model.User;
/**
 * FXML Controller class
 *
 * @author user
 */
public class AdministrationPageController implements Initializable {

    @FXML
    private Button btnHotovo;
    @FXML
    private ListView<User> listViewUsers;
    @FXML
    private ListView<Group> listViewGroups;

    private final UserDAO userDAO = new UserDAOImpl();
    private final GroupDAO groupDAO = new GroupDAOImpl();
    private final FieldOfStudyDAO fieldDAO = new FieldOfStudyDAOImpl();
    private final SubjectDAO subjectDAO = new SubjectDAOImpl();
    @FXML
    private ListView<Field> listViewFields;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //listViewUsers.setItems(FXCollections.observableArrayList(userDAO.getAllUsers()));
        //listViewGroups.setItems(FXCollections.observableArrayList(groupDAO.getAllGroups()));
        //listViewFields.setItems(FXCollections.observableArrayList(fieldDAO.getAllFields()));
    }
}
    /*

    @FXML
    private void btnDoneClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(Main.class.getResource("/gui/MainDashboardPage.fxml"));
            loader.load();
            MainDashboardPageController controller = loader.getController();
            controller.initialize(null, null); //Refresh stránky
            Main.switchScene("/gui/MainDashboardPage.fxml");

        } catch (IOException ex) {

            Logger.getLogger(AdministrationPageController.class.getFirstName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onUpravitUzivateleMenuItemClicked(ActionEvent event) {
        User user = listViewUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            EditUserDialog edit = new EditUserDialog(user);
            edit.showAndWait().ifPresent((u) -> {
                if (user.equals(MainDashboardPageController.getLoggedUser())) {
                    MainDashboardPageController.setLoggedUser(u);
                }
                try {
                    userDAO.updateUser(u);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                updateUserListView();
            });
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Uživatel nebyl vybrán").showAndWait();
        }
    }

    @FXML
    private void onSkupinyUpravitMenuItemClicked(ActionEvent event) {
        Group group = listViewGroups.getSelectionModel().getSelectedItem();
        if (group != null) {
            EditGroupDialog edit = new EditGroupDialog(group);
            edit.showAndWait().ifPresent((u) -> {
                groupDAO.updateGroup(u);
                updateGroupListView();
            });
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Group nebyla vybrána").showAndWait();
        }
    }

    @FXML
    private void onAddToGroupClicked(ActionEvent event) {
        User uzivatel = listViewUsers.getSelectionModel().getSelectedItem();
        if (uzivatel != null && !listViewGroups.getItems().isEmpty()) {
            ChoiceDialog<Group> chDialog = new ChoiceDialog<>(listViewGroups.getItems().get(0), listViewGroups.getItems());
            chDialog.setTitle("Vlož do skupiny");
            chDialog.setHeaderText("");
            chDialog.setContentText("Vyberte skupinu");

            chDialog.showAndWait().ifPresent((s) -> {
                groupDAO.insertUserToGroup(uzivatel, s);
            });

        } else {
            new Alert(Alert.AlertType.INFORMATION, "Uživatel nebyl vybrán nebo nejsou skupiny").showAndWait();
        }
    }

    @FXML
    private void onCreateUserClicked(ActionEvent event) {
        InsertUserDialog insertDialog = new InsertUserDialog();
        insertDialog.showAndWait().ifPresent((u) -> {
            userDAO.insertUser(u);
            updateUserListView();
        });
    }

    @FXML
    private void onRemoveUserClicked(ActionEvent event) {
        if (!listViewUsers.getSelectionModel().isEmpty()) {
            if (listViewUsers.getSelectionModel().getSelectedItem().equals(MainDashboardPageController.getLoggedUser())) {
                new Alert(Alert.AlertType.INFORMATION, "Nelze odstranit sebe").showAndWait();
                return;
            }
            userDAO.deleteUser(listViewUsers.getSelectionModel().getSelectedItem());
            updateUserListView();

        } else {
            new Alert(Alert.AlertType.INFORMATION, "Uživatel nebyl vybrán").showAndWait();
        }
    }

    @FXML
    private void onGroupInsertClicked(ActionEvent event) {
        InsertGroupDialog groupDialog = new InsertGroupDialog();
        groupDialog.showAndWait().ifPresent((g) -> {
            groupDAO.insertGroup(g);
            updateGroupListView();
        });
    }

    @FXML
    private void onGroupDeleteClicked(ActionEvent event) {
        if (!listViewGroups.getSelectionModel().isEmpty()) {
            groupDAO.removeGroup(listViewGroups.getSelectionModel().getSelectedItem());
            updateGroupListView();

        } else {
            new Alert(Alert.AlertType.INFORMATION, "Group nebyla vybrána").showAndWait();
        }
    }

    private void updateUserListView() {
        listViewUsers.getItems().clear();
        listViewUsers.setItems(FXCollections.observableArrayList(userDAO.getAllUsers()));
    }

    private void updateGroupListView() {
        listViewGroups.getItems().clear();
        listViewGroups.setItems(FXCollections.observableArrayList(groupDAO.getAllGroups()));
    }

    private void updateFieldListView() {
        listViewFields.getItems().clear();
        listViewFields.setItems(FXCollections.observableArrayList(fieldDAO.getAllFields()));
    }

    @FXML
    private void onFieldInsertClicked(ActionEvent event) {
        InsertFieldDialog fieldDialog = new InsertFieldDialog();
        fieldDialog.showAndWait().ifPresent((f) -> {
            fieldDAO.insertField(f);
            updateFieldListView();
        });
    }

    @FXML
    private void onFieldRemoveClicked(ActionEvent event) {
        if (!listViewFields.getSelectionModel().isEmpty()) {
            fieldDAO.deleteField(listViewFields.getSelectionModel().getSelectedItem());
            updateFieldListView();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Field nebyl vybrán").showAndWait();
        }
    }

    @FXML
    private void onFieldEditClicked(ActionEvent event) {
        if (!listViewFields.getSelectionModel().isEmpty()) {
            new EditFieldDialog(listViewFields.getSelectionModel().getSelectedItem()).showAndWait().ifPresent((f) -> {
                fieldDAO.updateField(f);
                updateFieldListView();
            });

        } else {
            new Alert(Alert.AlertType.INFORMATION, "Field nebyl vybrán").showAndWait();
        }
    }

    @FXML
    private void onFieldShowSubjectsClicked(ActionEvent event) {
    }

    @FXML
    private void onFieldSubjectAddClicked(ActionEvent event) {
        if (!listViewFields.getSelectionModel().isEmpty()) {
            new AddRemoveSubjectsDialog(listViewFields.getSelectionModel().getSelectedItem()).showAndWait();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Field nebyl vybrán").showAndWait();
        }
    }

} */

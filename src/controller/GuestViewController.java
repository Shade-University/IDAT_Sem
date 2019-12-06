package controller;

import data.GroupDAO;
import data.GroupDAOImpl;
import data.UserDAO;
import data.UserDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Group;
import model.USER_TYPE;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GuestViewController implements Initializable {

    private UserDAO userDAO = new UserDAOImpl();
    private GroupDAO groupDAO = new GroupDAOImpl();

    private ObservableList<User> userData;
    private ObservableList<Group> groupData;


    public TableView<User> tableViewUsers;
    public TableView<Group> tableViewGroups;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prepareTableViewUsers();
        prepareTableViewGroups();

        try {
            userData = FXCollections.observableArrayList(userDAO.getAllUsers());
            groupData = FXCollections.observableArrayList(groupDAO.getAllGroups());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableViewUsers.setItems(userData);
        tableViewGroups.setItems(groupData);
    }

    private void prepareTableViewUsers() {
        TableColumn<User, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> firstNameColumn = new TableColumn<>("Jméno");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Přijmení");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableViewUsers.getColumns().add(idColumn);
        tableViewUsers.getColumns().add(firstNameColumn);
        tableViewUsers.getColumns().add(lastNameColumn);
        tableViewUsers.getColumns().add(emailColumn);
        tableViewUsers.getColumns().add(roleColumn);
    }

    private void prepareTableViewGroups() {
        TableColumn<Group, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Group, String> nameColumn = new TableColumn<>("Název");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Group, String> descriptionColumn = new TableColumn<>("Popis");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Group, String> countPeopleColumn = new TableColumn<>("Počet lidí ve skupině");
        countPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("xx"));

        tableViewGroups.getColumns().add(idColumn);
        tableViewGroups.getColumns().add(nameColumn);
        tableViewGroups.getColumns().add(descriptionColumn);
        tableViewGroups.getColumns().add(countPeopleColumn);
    }

}

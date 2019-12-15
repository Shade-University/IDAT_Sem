package controller;

import data.GroupDAO;
import data.GroupDAOImpl;
import data.UserDAO;
import data.UserDAOImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Group;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GuestViewController implements Initializable {

    public TabPane tabPane;
    public TextField txtFieldSearch;

    private UserDAO userDAO = new UserDAOImpl();
    private GroupDAO groupDAO = new GroupDAOImpl();

    private ObservableList<User> userData;
    private ObservableList<Group> groupData;


    public TableView<User> tableViewUsers;
    public TableView<Group> tableViewGroups;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTxtField();
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov) -> Platform.runLater(() -> txtFieldSearch.requestFocus())
        );
        prepareTableViewUsers();
        prepareTableViewGroups();

        // runnable for that thread
        new Thread(() -> {
            try {
                userData = FXCollections.observableArrayList(userDAO.getAllUsers());
                groupData = FXCollections.observableArrayList(groupDAO.getAllGroupWithUserQuantity());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                tableViewUsers.setItems(userData);
                tableViewGroups.setItems(groupData);
            });
        }).start();
    }

    private void initializeTxtField() {
        txtFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tabPane.getSelectionModel().getSelectedIndex() == 0)
                searchUsers(newValue);
            else
                searchGroups(newValue);
        });
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

        TableColumn<Group, Integer> countPeopleColumn = new TableColumn<>("Počet lidí ve skupině");
        countPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableViewGroups.getColumns().add(idColumn);
        tableViewGroups.getColumns().add(nameColumn);
        tableViewGroups.getColumns().add(descriptionColumn);
        tableViewGroups.getColumns().add(countPeopleColumn);
    }

    private void searchGroups(String value) {
        List<Group> foundGroups = groupData.stream()
                .filter(item -> item.getName().contains(value))
                .collect(Collectors.toList());
        if (foundGroups.isEmpty())
            tableViewGroups.setItems(groupData);
        else
            tableViewGroups.setItems(FXCollections.observableArrayList(foundGroups));
    }

    private void searchUsers(String value) {
        List<User> foundUsers = userData.stream()
                .filter(item -> item.getFirstName().contains(value))
                .collect(Collectors.toList());
        if (foundUsers.isEmpty())
            tableViewUsers.setItems(userData);
        else
            tableViewUsers.setItems(FXCollections.observableArrayList(foundUsers));
    }
}

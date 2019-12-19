package controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import data.*;
import gui.AlertDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

public class ToolboxForTeachersPageController implements Initializable {

    private SubjectDAO subjectDAO = new SubjectDAOImpl();
    private GroupDAO groupDAO = new GroupDAOImpl();
    private MessageDAO messageDAO = new MessageDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private Tab EditMessageTab = new Tab();
    private MainDashboardPageController mdpc;
    private User loggedUser;
    private Group selectedGroup;

    public ListView<Group> lVSGroups;
    public ListView<Subject> lVSSubjects;
    public ListView<Message> lVGroupMessages;
    public ListView<User> lVMembersOfGroup;
    public ListView<Group> lVGroups;
    public ListView<Subject> lVMySubjects;
    public HBox hBoxDescription;
    public VBox parentBox;
    public Label lblMessages;
    public Label lblGroups;
    public ComboBox<User> cbUsersToGroup;
    public Button btnAddUser;
    public Label labelMembers;
    public Label lblSubjectsInGroup;

    void initData(User loggedUser, MainDashboardPageController mdpc) {
        this.loggedUser = loggedUser;
        this.mdpc = mdpc;
        new Thread(() -> {
            try {
                Collection<Subject> subjects = subjectDAO.getAllSubjectsByTeacher(loggedUser);
                Collection<User> users = userDAO.getAllUsers();
                Collection<Group> groups = groupDAO.getAllGroups();
                Platform.runLater(() -> {
                    lVMySubjects.setItems(FXCollections.observableArrayList(subjects));
                    cbUsersToGroup.setItems(FXCollections.observableArrayList(users));
                    cbUsersToGroup.getSelectionModel().selectFirst();
                    lVSGroups.setItems(FXCollections.observableArrayList(groups));
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    } //Load teacher data

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Předměty
        try {
            lVMySubjects.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                loading(1, true);
                new Thread(() -> {
                    try {
                        Collection<Group> groups = groupDAO.getSubjectGroups(newValue);
                        Platform.runLater(() -> lVGroups.setItems(FXCollections.observableArrayList(groups)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        loading(1, false);
                    });
                }).start();
            });
            lVSGroups.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                loading(3, true);
                new Thread(() -> {
                    try {
                        Collection<Subject> subjects = subjectDAO.getSubjectsForGroup(newValue.getId());
                        Platform.runLater(() -> lVSSubjects.setItems(FXCollections.observableArrayList(subjects)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        loading(3, false);
                    });
                }).start();
            });
        } catch (Exception e) {
            e.printStackTrace();
        } //Load teacher subjects
        //Zprávy
        lVGroups.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            reloadMessagesByGroup(newValue);
        }); //On change group, get users in group etc
    }


    public void reloadMessagesByGroup(Group gp) {
        loading(2, true);
        new Thread(() -> {
            try {
                if (gp != null) {
                    selectedGroup = gp;
                    Collection<User> membersOfGroup = userDAO.getAllUsersFromGroup(gp);
                    Collection<Message> messages = messageDAO.getMessagesForGroupChat(gp);
                    Platform.runLater(() ->{
                        lVGroupMessages.setItems(FXCollections.observableArrayList(messages));
                        lVMembersOfGroup.setItems(FXCollections.observableArrayList(membersOfGroup));
                        try{
                            buttonLabel();
                        }catch (Exception e){

                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        lVGroupMessages.setItems(null);
                        lVMembersOfGroup.setItems(null);
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                loading(2, false);
            });
        }).start();
    } //Get all users and messages in group

    @FXML
    void btnChangeClicked(ActionEvent event) {
        if (lVGroupMessages.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/EditMessagePage.fxml"));
                Parent editMessagePane = loader.load();

                EditMessagePageController toolbox = loader.getController();
                EditMessageTab.setText("Editace zprávy");
                EditMessageTab.setClosable(true);
                toolbox.initDataFromToolBox(lVGroupMessages.getSelectionModel().getSelectedItem(), EditMessageTab, mdpc, this);
                EditMessageTab.setContent(editMessagePane);
                mdpc.selectTab(EditMessageTab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialog.show("Není zvolená žádná zpráva k editaci", Alert.AlertType.ERROR);
        }
    } //On edit message, show edit message window

    /**
     * Vytvoření upozornění na načítání.
     *
     * @param hodnota -> 1)Skupina, 2)Message
     * @param state   -> True - Načítání
     */
    private void loading(int hodnota, boolean state) {
        switch (hodnota) {
            case 1:
                if (!state) {
                    lblGroups.setText("Skupiny předmětu:");
                } else {
                    lblGroups.setText("Skupiny předmětu: (Načítání...)");
                }
                break;
            case 2:
                if (!state) {
                    lblMessages.setText("Komentáře ve skupině:");
                    labelMembers.setText("Členové:");
                } else {
                    lblMessages.setText("Komentáře ve skupině: (Načítání...)");
                    labelMembers.setText("Členové: (Načítání...)");
                }
            case 3:
                if (!state) {
                    lblSubjectsInGroup.setText("Předměty ve skupině:");
                } else {
                    lblSubjectsInGroup.setText("Předměty ve skupině: (Načítání...)");
                }


        }
    }

    public void btnAddUserToGroup(ActionEvent event) {
        try {
            try {
                if(lVMembersOfGroup.getItems().contains(cbUsersToGroup.getValue())){
                    groupDAO.removeUserFromGroup(cbUsersToGroup.getValue(), selectedGroup);
                } else {
                    groupDAO.insertUserToGroup(cbUsersToGroup.getValue(), selectedGroup);
                }
                reloadMessagesByGroup(selectedGroup);
            } catch (SQLException e){
                AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //Add user to group

    @FXML
    void cbUsersToGroupChanged(ActionEvent event) throws SQLException {
       buttonLabel();
    }

    private void buttonLabel(){
        if(lVMembersOfGroup.getItems().contains(cbUsersToGroup.getValue())){
            btnAddUser.setText("Odebrat uživatele");
        } else {
            btnAddUser.setText("Přidat uživatele");
        }
    }
}

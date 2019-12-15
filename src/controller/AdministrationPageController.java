package controller;

import data.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import model.*;

import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 */
public class AdministrationPageController implements Initializable {

    public StackPane stackPaneEditUser;
    public StackPane stackPaneEditGroup;
    public StackPane stackPaneEditFieldsOfStudy;
    public StackPane stackPaneEditSubject;
    public StackPane stackPaneEditRating;
    public StackPane stackPaneEditMessage;
    public StackPane stackPaneEditFile;
    public StackPane stackPaneISKAM;

    public ListView<File> listViewFile;

    @FXML
    private ListView<User> listViewUsers;
    @FXML
    private ListView<Group> listViewGroups;
    @FXML
    private ListView<Field> listViewFieldsOfStudy;
    @FXML
    private ListView<Subject> listViewSubjects;
    @FXML
    private ListView<Rating> listViewRatings;
    @FXML
    private ListView<Message> listViewMessage;
    @FXML
    private Tab tabISKAM;

    private final UserDAO userDAO = new UserDAOImpl();
    private final GroupDAO groupDAO = new GroupDAOImpl();
    private final FieldOfStudyDAO fieldDAO = new FieldOfStudyDAOImpl();
    private final SubjectDAO subjectDAO = new SubjectDAOImpl();
    private final FileDAO fileDAO = new FileDAOImpl();
    private final RatingDAO ratingDAO = new RatingDAOImpl();
    private final MessageDAO messageDAO = new MessageDAOImpl();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshAll();
        /*============USERS============*/
        listViewUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                EditUserController.setEditedUser(newValue);
                AnchorPane parent = FXMLLoader.load(getClass().getResource("/gui/EditUserPage.fxml"));

                stackPaneEditUser.getChildren().clear();
                stackPaneEditUser.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /*============GROUPS============*/
        listViewGroups.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadGroupPane(newValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /*============FieldsOfStudy============*/
        listViewFieldsOfStudy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadFieldOfStudyPane(newValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /*============Subjects============*/
        listViewSubjects.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadSubject(newValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /*============Rating============*/
        listViewRatings.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadRating(newValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /*============Message============*/
        listViewMessage.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadMessage(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /*============ISKAM============*/
        try {
            loadISKAM();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*============Files============*/
        listViewFile.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                EditFilePageController.setEditedFile(newValue);
                AnchorPane parent = FXMLLoader.load(getClass().getResource("/gui/EditFilePage.fxml"));

                stackPaneEditFile.getChildren().clear();
                stackPaneEditFile.getChildren().add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void refreshAll() {
        refreshUsers();
        refreshFiles();
        refreshGroups();
        refreshFieldOfStudy();
        refreshSubject();
        refreshRating();
        refreshMessage();
    }

    private void refreshUsers() {
        listViewUsers.getItems().clear();
        new Thread(() -> {
            try {
                Collection<User> users = userDAO.getAllUsers();
                Platform.runLater(() -> listViewUsers.setItems(FXCollections.observableArrayList(users)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void refreshFiles() {
        listViewFile.getItems().clear();
        Thread t = new Thread(() -> {
            try {
                Collection<File> files = fileDAO.getAllFiles();
                Platform.runLater(() -> listViewFile.setItems(FXCollections.observableArrayList(files)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void refreshGroups() {
        listViewGroups.getItems().clear();
        new Thread(() -> {
            try {
                Collection<Group> groups = groupDAO.getAllGroups();
                Platform.runLater(() -> listViewGroups.setItems(FXCollections.observableArrayList(groups)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void refreshFieldOfStudy() {
        listViewFieldsOfStudy.getItems().clear();
        new Thread(() -> {
            try {
                Collection<Field> fields = fieldDAO.getAllFields();
                Platform.runLater(() -> listViewFieldsOfStudy.setItems(FXCollections.observableArrayList(fields)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void refreshSubject() {
        listViewSubjects.getItems().clear();
        new Thread(() -> {
            try {
                Collection<Subject> subjects = subjectDAO.getAllSubjects();
                Platform.runLater(() -> listViewSubjects.setItems(FXCollections.observableArrayList(subjects)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void refreshRating() {
        listViewRatings.getItems().clear();
        new Thread(() -> {
            try {
                Collection<Rating> ratings = ratingDAO.getAllRatings();
                Platform.runLater(() -> listViewRatings.setItems(FXCollections.observableArrayList(ratings)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void refreshMessage() {
        listViewMessage.getItems().clear();
        new Thread(() -> {
            try {
                Collection<Message> messages = messageDAO.getAllMessages();
                Platform.runLater(() -> listViewMessage.setItems(FXCollections.observableArrayList(messages)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void onClickAddUser(MouseEvent mouseEvent) {
        AnchorPane parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/gui/RegistrationPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stackPaneEditUser.getChildren().clear();
        stackPaneEditUser.getChildren().add(parent);
    }

    public void onClickAddGroup(MouseEvent mouseEvent) {
        try {
            loadGroupPane(null);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void onClickAddFieldOfStudy(MouseEvent mouseEvent) {
        try {
            loadFieldOfStudyPane(null);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void onClickAddSubject(MouseEvent mouseEvent) {
        try {
            loadSubject(null);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void onClickAddRating(MouseEvent mouseEvent) {
        try {
            loadRating(null);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void onClickAddMessage(MouseEvent mouseEvent) {
        try {
            loadMessage(null);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /*=====PaneLoader=====*/
    //Group
    private void loadGroupPane(Group gp) throws IOException {
        EditGroupPageController.setParams(gp, this);
        AnchorPane groupPane = FXMLLoader.load(getClass().getResource("/gui/EditGroupPage.fxml"));
        stackPaneEditGroup.getChildren().clear();
        stackPaneEditGroup.getChildren().add(groupPane);
    }

    //FieldOfStudy
    private void loadFieldOfStudyPane(Field fd) throws IOException {
        EditFieldOfStudyPageController.setParams(fd, this);
        AnchorPane fieldPane = FXMLLoader.load(getClass().getResource("/gui/EditFieldOfStudyPage.fxml"));
        stackPaneEditFieldsOfStudy.getChildren().clear();
        stackPaneEditFieldsOfStudy.getChildren().add(fieldPane);
    }

    //Subject
    private void loadSubject(Subject sb) throws IOException {
        EditSubjectPageController.setParams(sb, this);
        AnchorPane subjectPane = FXMLLoader.load(getClass().getResource("/gui/EditSubjectPage.fxml"));
        stackPaneEditSubject.getChildren().clear();
        stackPaneEditSubject.getChildren().add(subjectPane);
    }

    //Rating
    private void loadRating(Rating rt) throws IOException {
        EditRatingPageController.setParams(rt, this);
        AnchorPane ratingPane = FXMLLoader.load(getClass().getResource("/gui/EditRatingPage.fxml"));
        stackPaneEditRating.getChildren().clear();
        stackPaneEditRating.getChildren().add(ratingPane);
    }

    //Message
    private void loadMessage(Message msg) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/EditMessagePage.fxml"));
            Parent editMessagePane = loader.load();
            EditMessagePageController toolbox = loader.getController();
            toolbox.initDataFromAdministration(msg, this);
            stackPaneEditMessage.getChildren().clear();
            stackPaneEditMessage.getChildren().add(editMessagePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ISKAM - admin page
    private void loadISKAM() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ISKAMAdminPage.fxml"));
            Parent iskam = loader.load();
            ISKAMAdminPageController iskamAPC = loader.getController();
            iskamAPC.initData();
            stackPaneISKAM.getChildren().clear();
            stackPaneISKAM.getChildren().add(iskam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickAddFile(MouseEvent mouseEvent) {
        AnchorPane parent = null;
        EditFilePageController.setEditedFile(null);
        try {
            parent = FXMLLoader.load(getClass().getResource("/gui/EditFilePage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPaneEditFile.getChildren().clear();
        stackPaneEditFile.getChildren().add(parent);
    }
}

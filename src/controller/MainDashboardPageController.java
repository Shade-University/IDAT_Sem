package controller;

import data.GroupDAOImpl;
import data.MessageDAO;
import data.MessageDAOImpl;
import data.UserDAOImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

import gui.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.*;
import data.UserDAO;
import data.GroupDAO;
import data.RatingDAO;
import data.RatingDAOImpl;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MainDashboardPageController implements Initializable {

    private final UserDAO userDAO = new UserDAOImpl();
    private final GroupDAO groupDAO = new GroupDAOImpl();
    private final MessageDAO messageDAO = new MessageDAOImpl();
    private final RatingDAO ratingDAO = new RatingDAOImpl();

    private static User loggedUser;
    public ListView<User> listViewUsers;
    public StackPane mainStackPane;
    public Label lblInfo;
    public Label lblRole;
    public Label lblNickName;
    public ImageView imgView;
    public TabPane tabPane;
    public ListView<Group> listViewGroups;
    public ListView listViewIskam;

    private FileChooser fileChooser = new FileChooser();
    private ObservableList<User> userData;
    private ObservableList<Group> groupData;
    private Tab editProfileTab = new Tab();
    private Tab administrationTab = new Tab();
    private Tab importTab = new Tab();
    private Tab toolboxForTeachers = new Tab();
    private Tab iskamTAB = new Tab();

    @FXML private Tab tabChat;
    @FXML private HBox hBoxToolboxForTeachers;
    @FXML private HBox hBoxAdministration;
    @FXML private HBox hBoxImport;
    @FXML private VBox vBoxMenu;
    @FXML private HBox hBoxChangeUser;
    @FXML private ComboBox<User> cbChangeUser;

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTabs();
        initFileChooser(); //Init components
        loadUserData(); //Load logged user data
        loadLabels(); //Set user data

        listViewUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            openChatWith(newValue);
        });
        listViewGroups.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            openChatWith(newValue);
        }); //On click on user/group, open chat

        cbChangeUser.getSelectionModel().select(loggedUser);
        cbChangeUser.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            MainDashboardPageController.setLoggedUser(newValue);
            try {
                Main.switchScene(getClass().getResource("/gui/MainDashboardPage.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        })); //On administrator evaluate account, login as that user
    }

    public void onClickEditProfile(MouseEvent mouseEvent) {
        try {
            editProfileTab.setContent(FXMLLoader.load(getClass().getResource("/gui/EditProfilePage.fxml")));
            selectTab(editProfileTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Edit user profile

    public void onClickLogOut(MouseEvent mouseEvent) {
        try {
            Main.switchScene(getClass().getResource("/gui/MainPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Log out

    public void onImageClicked(MouseEvent mouseEvent) {
        File file = fileChooser.showOpenDialog(Main.primaryStage);
        if(file!=null) {
            try {
                userDAO.updateAvatar(file, loggedUser);
                //Store password in plain text
                String passwordTemp = loggedUser.getPassword();
                loggedUser = userDAO.getUserByLogin(loggedUser.getEmail(), passwordTemp);
                loggedUser.setPassword(passwordTemp);
                this.initialize(null, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } //Change avatar

    public void onAdministrationClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AdministrationPage.fxml"));
            Parent admin = loader.load();
            AdministrationPageController adminPC = loader.getController();
            administrationTab.setContent(admin);
            selectTab(administrationTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Open administration

    public void onToolboxClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ToolboxForTeachersPage.fxml"));
            Parent toolBoxPane = loader.load();

            ToolboxForTeachersPageController toolbox = loader.getController();
            toolbox.initData(getLoggedUser(), this);

            toolboxForTeachers.setContent(toolBoxPane);
            selectTab(toolboxForTeachers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Open teacher tools

    public void onISKAMClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ISKAMUserPage.fxml"));
            Parent ISKAMpage = loader.load();

            ISKAMUserPageController iskamController = loader.getController();
            iskamController.initData(getLoggedUser());

            iskamTAB.setContent(ISKAMpage);
            selectTab(iskamTAB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Open iskam

    public void selectTab(Tab tab) {
        if (!tabPane.getTabs().contains(tab))
            tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    } //Helper method to open/select tab

    public void removeTab(Tab tab) {
        try {
            tabPane.getTabs().remove(tab);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    } //Helper method to close tab

    private void initTabs() {
        editProfileTab.setText("Profil");
        editProfileTab.setClosable(true);

        administrationTab.setText("Administrace");
        administrationTab.setClosable(true);

        importTab.setText("Import");
        importTab.setClosable(true);

        toolboxForTeachers.setText("Nástroje pro učitele");
        toolboxForTeachers.setClosable(true);

        iskamTAB.setText("ISKAM");
        iskamTAB.setClosable(true);

        EditProfileController.setEditedUser(loggedUser);
            switch (loggedUser.getUserType()){
                case TEACHER:
                    vBoxMenu.getChildren().remove(hBoxAdministration);
                    vBoxMenu.getChildren().remove(hBoxImport);
                    vBoxMenu.getChildren().remove(hBoxChangeUser);
                    break;
                case ADMIN:
                    vBoxMenu.getChildren().remove(hBoxToolboxForTeachers);
                    break;
                case STUDENT:
                    vBoxMenu.getChildren().remove(hBoxChangeUser);
                    vBoxMenu.getChildren().remove(hBoxAdministration);
                    vBoxMenu.getChildren().remove(hBoxImport);
                    vBoxMenu.getChildren().remove(hBoxToolboxForTeachers);
                    break;
            }
    } //Init default tabs

    private void loadLabels() {
        lblNickName.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName());
        lblRole.setText(loggedUser.getUserType().getType());

        if(loggedUser.getUserAvatar() != null)
            imgView.setImage(SwingFXUtils.toFXImage(loggedUser.getUserAvatar(), null));
        else
            imgView.setImage(new Image("/gui/images/account.png"));

        if(loggedUser instanceof Student)
            lblInfo.setText(((Student)loggedUser).getField().getName());
        else if(loggedUser instanceof Teacher)
            lblInfo.setText(((Teacher)loggedUser).getInstitute());
        else
            lblInfo.setVisible(false);
    } //Init user info

    private void loadUserData() {
        new Thread(() -> {
            try {
                userData = FXCollections.observableArrayList(userDAO.getAllUsers());
                groupData = FXCollections.observableArrayList(groupDAO.getUserGroups(loggedUser));
                Platform.runLater(() -> listViewUsers.setItems(userData));
                Platform.runLater(() -> cbChangeUser.setItems(userData));
                Platform.runLater(() -> listViewGroups.setItems(groupData));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    } //Load user data

    private void initFileChooser() {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
                , new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    } //Init file chooser

    private void openChatWith(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ChatWindowPage.fxml"));
        Tab chatTab = new Tab("Chat: " + user.getFirstName());
        try {
            chatTab.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChatWindowPageController chatWindowPageController = loader.getController();
        chatWindowPageController.setChatUsers(Arrays.asList(user));

        selectTab(chatTab);
    } //Open chat with user

    private void openChatWith(Group group) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ChatWindowPage.fxml"));
        Tab chatTab = new Tab("Chat: " + group.getName());
        try {
            chatTab.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChatWindowPageController chatWindowPageController = loader.getController();
        chatWindowPageController.setChatGroup(group);

        selectTab(chatTab);
    } //Open chat with group


    public void onImportClicked(MouseEvent mouseEvent) {
        try {
            importTab.setContent(FXMLLoader.load(getClass().getResource("/gui/ImportPage.fxml")));
            selectTab(importTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Open REST import window
}

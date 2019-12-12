package controller;

import data.GroupDAOImpl;
import data.MessageDAO;
import data.MessageDAOImpl;
import data.UserDAOImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import gui.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.Student;
import model.Teacher;
import model.USER_TYPE;
import model.User;
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

    private FileChooser fileChooser = new FileChooser();
    private ObservableList<User> userData;
    private Tab editProfileTab = new Tab();
    private Tab administrationTab = new Tab();


    public static void setLoggedUser(User user) {
        loggedUser = user;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTabs();
        initFileChooser();
        loadUserData();
        loadLabels();

        listViewUsers.setItems(userData);
        listViewUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           openChatWith(FXCollections.observableArrayList(newValue));
        });


    }




    public void onClickEditProfile(MouseEvent mouseEvent) throws IOException {
        selectTab(editProfileTab);
    }

    public void onClickLogOut(MouseEvent mouseEvent) {
        try {
            Main.switchScene(getClass().getResource("/gui/MainPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onImageClicked(MouseEvent mouseEvent) {
        File file = fileChooser.showOpenDialog(Main.primaryStage);
        try {
            userDAO.updateAvatar(file, loggedUser);
            //Store password in plain text
            String passwordTemp = loggedUser.getPassword();
            loggedUser = userDAO.getUserByLogin(loggedUser.getEmail(), passwordTemp);
            loggedUser.setPassword(passwordTemp);
            initialize(null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onAdministrationClicked(MouseEvent mouseEvent) {
        selectTab(administrationTab);
    }

    private void selectTab(Tab tab) {
        if (!tabPane.getTabs().contains(tab))
            tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    private void initTabs() {
        editProfileTab.setText("Profil");
        editProfileTab.setClosable(true);

        administrationTab.setText("Administrace");
        administrationTab.setClosable(true);

        try {
            EditProfileController.setEditedUser(loggedUser);
            editProfileTab.setContent(FXMLLoader.load(getClass().getResource("/gui/EditProfilePage.fxml")));
            administrationTab.setContent(FXMLLoader.load(getClass().getResource("/gui/AdministrationPage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadLabels() {
        lblNickName.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName());
        lblRole.setText(loggedUser.getUserType().getType());

        if(loggedUser.getUserAvatar() != null)
            imgView.setImage(SwingFXUtils.toFXImage(loggedUser.getUserAvatar(), null));
        else
            imgView.setImage(new Image("/gui/images/account.png"));

        if(loggedUser instanceof Student)
            lblInfo.setText(((Student)loggedUser).getField().getNazev());
        else if(loggedUser instanceof Teacher)
            lblInfo.setText(((Teacher)loggedUser).getInstitute());
        else
            lblInfo.setVisible(false);
    }
    private void loadUserData() {
        try {
            userData = FXCollections.observableArrayList(userDAO.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void initFileChooser() {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
                , new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    private void openChatWith(ObservableList<User> list) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ChatWindowPage.fxml"));
        Tab chatTab = new Tab("Chat");
        try {
            chatTab.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChatWindowPageController chatWindowPageController = loader.getController();
        chatWindowPageController.setChatUsers(list);

        selectTab(chatTab);
    }
}

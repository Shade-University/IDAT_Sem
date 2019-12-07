package controller;

import data.GroupDAOImpl;
import data.MessageDAO;
import data.MessageDAOImpl;
import data.UserDAOImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import gui.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
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
    public ListView listViewUzivatele;
    public StackPane mainStackPane;
    public Label lblInfo;
    public Label lblRole;
    public Label lblNickName;
    public ImageView imgView;
    public TabPane tabPane;

    FileChooser fileChooser = new FileChooser();

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }
    public static User getLoggedUser(){
        return loggedUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
                ,new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        lblNickName.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName());
        lblRole.setText(loggedUser.getUserType().getType());
        imgView.setImage(SwingFXUtils.toFXImage(loggedUser.getUserAvatar(), null));
        //TODO info
    }


    public void onClickEditProfile(MouseEvent mouseEvent) {
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
            String passwordTemp = loggedUser.getPassword();
            loggedUser = userDAO.getUserByLogin(loggedUser.getEmail(), passwordTemp);
            loggedUser.setPassword(passwordTemp);
            initialize(null,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

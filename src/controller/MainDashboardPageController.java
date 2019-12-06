package controller;

import data.GroupDAOImpl;
import data.MessageDAO;
import data.MessageDAOImpl;
import data.UserDAOImpl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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

    private static User prihlasenyUzivatel;
    public ListView listViewUzivatele;
    public StackPane mainStackPane;
    public Label lblInfo;
    public Label lblRole;
    public Label lblJmeno;
    public ImageView imgView;
    public TabPane tabPane;

    public static void setUzivatel(User uzivatel) {
        prihlasenyUzivatel = uzivatel;
    }
    public static User getUzivatel(){
        return prihlasenyUzivatel;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void onClickUpravitProfil(MouseEvent mouseEvent) {
    }

    public void onRegistrationClicked(MouseEvent mouseEvent) {
    }
}

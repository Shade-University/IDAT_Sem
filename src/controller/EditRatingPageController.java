package controller;

import data.*;
import gui.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import model.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

public class EditRatingPageController implements Initializable {


    private final GroupDAO groupDAO = new GroupDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private final RatingDAO ratingDAO = new RatingDAOImpl();
    private ObservableList<Rating> ratings;
    private static AdministrationPageController parent;
    private static Rating editedRating;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<RATING_GRADE> cBRating;

    @FXML
    private ComboBox<Group> cBRatedGroup;

    @FXML
    private ComboBox<User> cBRatingUser;

    /**
     * Nastavení vstupních parametrů
     *
     * @param rating - editovaná skupina
     * @param aP     - Předek ve kterém je zobrazený formulář
     */
    public static void setParams(Rating rating, AdministrationPageController aP) {
        editedRating = rating;
        parent = aP;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cBRatedGroup.setItems(FXCollections.observableArrayList(groupDAO.getAllGroups()));
            cBRating.setItems(FXCollections.observableArrayList(RATING_GRADE.values()));
            cBRatingUser.setItems(FXCollections.observableArrayList(userDAO.getAllUsers()));
            if (editedRating != null) {
                cBRatedGroup.setValue(editedRating.getHodnoticiSkupina());
                cBRating.setValue(RATING_GRADE.convertToRATING_GRADE(editedRating));
                cBRatingUser.setValue(editedRating.getHodnoticiUzivatel());
            } else {
                cBRatedGroup.getSelectionModel().select(0);
                cBRating.getSelectionModel().select(0);
                cBRatingUser.getSelectionModel().select(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obnovení dat ve formuláři a předkovi
     *
     * @throws SQLException
     */
    private void exitPane() throws SQLException {
        editedRating = null;
        parent.refreshRating();
        parent.stackPaneEditRating.getChildren().clear();
    }

    @FXML
    void btnSave(ActionEvent event) {
        try {
            Rating result = new Rating(RATING_GRADE.getPoints(cBRating.getValue()),cBRating.getValue().toString(),cBRatingUser.getValue(), cBRatedGroup.getValue());
            if(editedRating!=null){
                result.setId(editedRating.getId());
                ratingDAO.updateRating(result);
                AlertDialog.show("Hodnocení bylo úspěšně upraveno.", Alert.AlertType.INFORMATION);
            } else {
                ratingDAO.createRating(result);
                AlertDialog.show("Hodnocení bylo úspěšně vytvořeno.", Alert.AlertType.INFORMATION);
            }
            exitPane();
        } catch (SQLException e){
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnDelete(ActionEvent event) {
        try {
            ratingDAO.deleteRating(editedRating);
            AlertDialog.show("Hodnocení bylo vymazáno.", Alert.AlertType.INFORMATION);
            exitPane();
        } catch (SQLException e){
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }
}

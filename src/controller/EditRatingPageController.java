package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Rating;
import model.Subject;

import java.net.URL;
import java.util.ResourceBundle;

public class EditRatingPageController implements Initializable {

    private static AdministrationPageController parent;
    private static Rating editedRating;
    /**
     * Nastavení vstupních parametrů
     *
     * @param rating - editovaná skupina
     * @param aP      - Předek ve kterém je zobrazený formulář
     */
    public static void setParams(Rating rating, AdministrationPageController aP) {
        editedRating = rating;
        parent = aP;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private ComboBox<?> comboBoxRating;

    @FXML
    private ComboBox<?> comboBoxAddRatingToUniversal;

    @FXML
    private ListView<?> listViewRatingUniversal;

    @FXML
    private Button btnAddRatingToUniversal;

    @FXML
    private ComboBox<?> comboBoxRatingUniversal;

    @FXML
    private Label lblError1111;

    @FXML
    void btnRatingSaveClicked(ActionEvent event) {

    }

    @FXML
    void e40b0b(ActionEvent event) {

    }

    @FXML
    void btnSubjectDeleteClicked(ActionEvent event) {

    }

    @FXML
    void btnAddRatingToUniversalClicked(ActionEvent event) {

    }

}

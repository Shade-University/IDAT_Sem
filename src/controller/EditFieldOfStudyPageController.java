package controller;

import data.FieldOfStudyDAO;
import data.FieldOfStudyDAOImpl;
import data.GroupDAO;
import data.GroupDAOImpl;
import gui.AlertDialog;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Field;
import model.Group;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditFieldOfStudyPageController implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private TextArea textAreaFieldOfStudyDescription;
    @FXML
    private TextField txtFieldFieldOfStudyName;

    private static Field editedFieldOfStudy;
    private static AdministrationPageController parent;
    private final FieldOfStudyDAO fieldDAO = new FieldOfStudyDAOImpl();

    /**
     * Nastavení vstupních parametrů
     *
     * @param field - editovaný obor
     * @param aP    - Předek ve kterém je zobrazený formulář
     */
    public static void setParams(Field field, AdministrationPageController aP) {
        editedFieldOfStudy = field;
        parent = aP;
    }

    /**
     * Obnovení dat ve formuláři a předkovi
     *
     * @throws SQLException
     */
    private void reloadData() throws SQLException {
        editedFieldOfStudy = null;
        parent.refreshFieldOfStudy();
        parent.stackPaneEditFieldsOfStudy.getChildren().clear();
    }

    /**
     * Inicializace formuláře
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (editedFieldOfStudy != null) {
            txtFieldFieldOfStudyName.setText(editedFieldOfStudy.getNazev());
            textAreaFieldOfStudyDescription.setText(editedFieldOfStudy.getPopis());
        } else {
            btnSave.setText("Přidat obor");
            btnDelete.setVisible(false);
        }
    }

    @FXML
    void btnFieldOfStudySaveClicked(ActionEvent event) {
        try {
            if (editedFieldOfStudy == null) {
                Field nf = new Field(txtFieldFieldOfStudyName.getText(), textAreaFieldOfStudyDescription.getText());
                fieldDAO.insertField(nf);
                AlertDialog.show("Nový obor byl vytvořen.", Alert.AlertType.INFORMATION);
            } else {
                editedFieldOfStudy.setNazev(txtFieldFieldOfStudyName.getText());
                editedFieldOfStudy.setPopis(textAreaFieldOfStudyDescription.getText());
                fieldDAO.updateField(editedFieldOfStudy);
                AlertDialog.show("Úprava oboru byla úspěšná!", Alert.AlertType.INFORMATION);
            }
            reloadData();
        } catch (SQLException e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnFieldOfStudyDeleteClicked(ActionEvent event) {
        try {
            fieldDAO.deleteField(editedFieldOfStudy);
            AlertDialog.show("Obor byl úspěšně smazán!", Alert.AlertType.INFORMATION);
            reloadData();
        } catch (SQLException e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }

}

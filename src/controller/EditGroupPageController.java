package controller;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class EditGroupPageController implements Initializable {

    @FXML
    private Label lblError1;

    @FXML
    private TextArea textAreaGroupDescription;

    @FXML
    private TextField txtFieldGroupName;

    @FXML
    private Button btnAddUserToGroup;

    @FXML
    private ListView<?> listViewUsersInGroup;

    @FXML
    private ComboBox<?> comboBoxAddUserToGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void btnGroupSaveClicked(ActionEvent event) {

    }

    @FXML
    void e40b0b(ActionEvent event) {

    }

    @FXML
    void btnGroupDeleteClicked(ActionEvent event) {

    }

    @FXML
    void btnGroupUserAddClicked(ActionEvent event) {

    }

}

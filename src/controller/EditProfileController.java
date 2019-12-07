package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {
    public TextField txtFieldFirstName;
    public TextField txtFieldLastName;
    public TextField txtFieldEmail;
    public PasswordField txtFieldPassword;
    public PasswordField txtFieldPasswordConfirm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnUpdateClicked(ActionEvent actionEvent) {
    }

}

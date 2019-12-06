package controller;

import data.UserDAO;
import data.UserDAOImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.USER_TYPE;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationPageController implements Initializable {
    public PasswordField txtFieldPasswordConfirm;
    public PasswordField txtFieldPassword;
    public TextField txtFieldEmail;
    public TextField txtFieldLastName;
    public TextField txtFieldFirstName;
    public ComboBox<USER_TYPE> comboBox;
    public Label lblError;

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBox.setItems(FXCollections.observableArrayList(USER_TYPE.values()));
        comboBox.getSelectionModel().select(1);

    }

    public void btnRegisterClicked(ActionEvent actionEvent) {
        if(!txtFieldPassword.getText().equals(txtFieldPasswordConfirm.getText()))
            return;

        User newUser = new User(
                txtFieldFirstName.getText(),
                txtFieldLastName.getText(),
                txtFieldEmail.getText(),
                comboBox.getValue(),
                txtFieldPassword.getText()
        );

        try {
            userDAO.insertUser(newUser);
            lblError.setText("Uživatel úspěšně registrován");
        } catch (SQLException e) {
            lblError.setText(e.getMessage());
        }

    }
}

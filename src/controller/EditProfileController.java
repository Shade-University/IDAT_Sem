package controller;

import controller.enums.INSTITUTE;
import controller.enums.USER_TYPE;
import controller.enums.YEAR_STUDY;
import data.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {
    public TextField txtFieldFirstName;
    public TextField txtFieldLastName;
    public TextField txtFieldEmail;
    public PasswordField txtFieldPassword;
    public PasswordField txtFieldPasswordConfirm;
    public GridPane gridPane;
    public Label lblError;

    private final FieldOfStudyDAO fieldOfStudyDAO = new FieldOfStudyDAOImpl();
    private final SubjectDAO subjectDAO = new SubjectDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    private static User editedUser;

    private ComboBox<Field> fieldComboBox = new ComboBox<>();
    private ComboBox<YEAR_STUDY> yearStudyComboBox = new ComboBox<>();


    private ComboBox<INSTITUTE> instituteComboBox = new ComboBox<>();
    private ListView<Subject> subjectListView = new ListView<>();

    public static void setEditedUser(User user) {
        editedUser = user;
    }

    public static User getEditedUser() {
        return editedUser;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();

        if (editedUser instanceof Student) {
            initStudent();
        } else if (editedUser instanceof Teacher) {
            initTeacher();
        } //Init data by edited user
    }

    private void initData() {
        if (editedUser != null) {
            txtFieldFirstName.setText(editedUser.getFirstName());
            txtFieldLastName.setText(editedUser.getLastName());
            txtFieldEmail.setText(editedUser.getEmail());
            txtFieldPassword.setText(editedUser.getPassword());
        }
    }

    private void initTeacher() {
        instituteComboBox.setItems(FXCollections.observableArrayList(INSTITUTE.values()));
        instituteComboBox.getSelectionModel().select(0);

        subjectListView.setPrefHeight(200);
        subjectListView.setPrefWidth(200);
        new Thread(() -> {
            try {
                Collection<Subject> allSubjects = subjectDAO.getAllSubjects();
                Platform.runLater(() -> subjectListView.setItems(FXCollections.observableArrayList(allSubjects)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();

        subjectListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        gridPane.add(instituteComboBox, 2, 6);
        gridPane.add(subjectListView, 5, 0, 1, 7);
    } //Load and init data for teacher

    private void initStudent() {
        new Thread(() -> {
            try {
                Collection<Field> allFields = fieldOfStudyDAO.getAllFields();
                Platform.runLater(() -> {
                    fieldComboBox.setItems(FXCollections.observableArrayList(allFields));
                    fieldComboBox.getSelectionModel().select(0);
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();

        ObservableList<YEAR_STUDY> year_studies = FXCollections.observableArrayList(YEAR_STUDY.values());
        yearStudyComboBox.setItems(year_studies);
        yearStudyComboBox.getSelectionModel().select(0);

        gridPane.add(fieldComboBox, 2, 6);
        gridPane.add(yearStudyComboBox, 0, 6);
    } //Load and init data for student


    public void btnUpdateClicked(ActionEvent actionEvent) {
        User updateUser = null;
        if (editedUser instanceof Student) {
            updateUser = new Student(
                    fieldComboBox.getValue(),
                    yearStudyComboBox.getValue().getValue(),
                    txtFieldFirstName.getText(),
                    txtFieldLastName.getText(),
                    txtFieldEmail.getText(),
                    txtFieldPassword.getText(),
                    editedUser.getUserAvatar()
            );
        } else if (editedUser instanceof Teacher) {
            updateUser = new Teacher(
                    subjectListView.getSelectionModel().getSelectedItems(),
                    instituteComboBox.getValue().getValue(),
                    txtFieldFirstName.getText(),
                    txtFieldLastName.getText(),
                    txtFieldEmail.getText(),
                    txtFieldPassword.getText(),
                    editedUser.getUserAvatar()
            );
        } else {
            updateUser = new User(
                    txtFieldFirstName.getText(),
                    txtFieldLastName.getText(),
                    txtFieldEmail.getText(),
                    USER_TYPE.ADMIN,
                    txtFieldPassword.getText(),
                    editedUser.getUserAvatar()
            );
        }
        updateUser.setId(editedUser.getId());
        try {
            userDAO.updateUser(updateUser);
            MainDashboardPageController.setLoggedUser(updateUser);
        } catch (SQLException e) {
            lblError.setText("Profil se nepovedlo aktualizovat.\n" + e.getMessage());
        }
    } //Update user

}

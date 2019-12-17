package controller;

import controller.enums.ACTION_TYPE;
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



public class EditUserController implements Initializable {
    public Label lblError;
    public PasswordField txtFieldPasswordConfirm;
    public PasswordField txtFieldPassword;
    public TextField txtFieldEmail;
    public TextField txtFieldLastName;
    public TextField txtFieldFirstName;
    public GridPane gridPane;
    public ComboBox<ACTION_TYPE> actionTypeComboBox;

    private static User editedUser;

    public static void setEditedUser(User user) {
        editedUser = user;
    }
    public static User getEditedUser() {
        return editedUser;
    }

    private ComboBox<Field> fieldComboBox = new ComboBox<>();
    private ComboBox<YEAR_STUDY> yearStudyComboBox = new ComboBox<>();

    private ComboBox<INSTITUTE> instituteComboBox = new ComboBox<>();
    private ListView<Subject> subjectListView = new ListView<>();

    private final FieldOfStudyDAO fieldOfStudyDAO = new FieldOfStudyDAOImpl();
    private final SubjectDAO subjectDAO = new SubjectDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

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
                    editedUser != null ? editedUser.getUserAvatar() : null
            );
        } else if (editedUser instanceof Teacher) {
            updateUser = new Teacher(
                    subjectListView.getSelectionModel().getSelectedItems(),
                    instituteComboBox.getValue().getValue(),
                    txtFieldFirstName.getText(),
                    txtFieldLastName.getText(),
                    txtFieldEmail.getText(),
                    txtFieldPassword.getText(),
                    editedUser != null ? editedUser.getUserAvatar() : null
            );
        } else {
            updateUser = new User(
                    txtFieldFirstName.getText(),
                    txtFieldLastName.getText(),
                    txtFieldEmail.getText(),
                    USER_TYPE.ADMIN,
                    txtFieldPassword.getText(),
                    editedUser != null ? editedUser.getUserAvatar() : null
            );
        }

        try {
            switch (actionTypeComboBox.getValue()) {
                case DELETE:
                    updateUser.setId(editedUser.getId());
                    userDAO.deleteUser(updateUser);
                    break;
                case INSERT:
                    userDAO.insertUser(updateUser);
                    break;
                case UPDATE:
                    updateUser.setId(editedUser.getId());
                    userDAO.updateUser(updateUser);
                    break;
                default:
            }
            lblError.setText("Profil se podařilo aktualizovat");
        } catch (SQLException e) {
            //new Alert(Alert.AlertType.ERROR,"Profil se nepovedlo aktualizovat\n" + e.getMessage(), ButtonType.OK);
            lblError.setText("Profil se nepovedlo aktualizovat.\n" + e.getMessage());
        } //Perform action by type of user
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        try {
            if (editedUser instanceof Student) {
                initStudent();
            } else if (editedUser instanceof Teacher) {
                initTeacher();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        if (editedUser != null) {
            txtFieldFirstName.setText(editedUser.getFirstName());
            txtFieldLastName.setText(editedUser.getLastName());
            txtFieldEmail.setText(editedUser.getEmail());
            txtFieldPassword.setText(editedUser.getPassword());
            actionTypeComboBox.setItems(FXCollections.observableArrayList(ACTION_TYPE.UPDATE, ACTION_TYPE.DELETE));
            actionTypeComboBox.getSelectionModel().select(0);
        } else {
            actionTypeComboBox.setItems(FXCollections.observableArrayList(ACTION_TYPE.INSERT));
            actionTypeComboBox.getSelectionModel().select(0);
        }
    } //Init data by edited user

    private void initTeacher() throws SQLException {
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
        gridPane.add(subjectListView, 5, 0, 1, 8);
    } //Get and init data for teacher

    private void initStudent() throws SQLException {
        new Thread(() -> {
            try {
                Collection<Field> allFields = fieldOfStudyDAO.getAllFields();
                Platform.runLater(() -> fieldComboBox.setItems(FXCollections.observableArrayList(allFields)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        fieldComboBox.getSelectionModel().select(0);
        ObservableList<YEAR_STUDY> year_studies = FXCollections.observableArrayList(YEAR_STUDY.values());
        yearStudyComboBox.setItems(year_studies);
        yearStudyComboBox.getSelectionModel().select(0);

        gridPane.add(fieldComboBox, 2, 6);
        gridPane.add(yearStudyComboBox, 0, 6);
    } //Get and init data for student
}

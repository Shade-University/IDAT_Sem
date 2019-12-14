package controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import data.*;
import gui.AlertDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.*;
import model.*;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ToolboxForTeachersPageController implements Initializable {

    private SubjectDAO subjectDAO = new SubjectDAOImpl();
    private GroupDAO groupDAO = new GroupDAOImpl();
    private MessageDAO messageDAO = new MessageDAOImpl();
    private Tab EditMessageTab = new Tab();
    private MainDashboardPageController mdpc;
    private User loggedUser;

    @FXML
    private ListView<Message> lVGroupMessages;
    @FXML
    public ListView<Group> lVGroups;
    @FXML
    private ListView<Subject> lVMySubjects;
    @FXML
    private HBox hBoxDescription;
    @FXML
    private VBox parentBox;
    @FXML
    private Label lblMessages;
    @FXML
    private Label lblGroups;

    void initData(User loggedUser, MainDashboardPageController mdpc) {
        this.loggedUser = loggedUser;
        this.mdpc = mdpc;
        Thread t = new Thread(() -> {
            try {
                lVMySubjects.setItems(FXCollections.observableArrayList(subjectDAO.getAllSubjectsByTeacher(loggedUser)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {

            });
        });
        t.run();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Předměty
        lVMySubjects.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loading(1, true);
            Thread t = new Thread(() -> {
                try {
                    lVGroups.setItems(FXCollections.observableArrayList(groupDAO.getSubjectGroups(newValue)));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    loading(1, false);
                });
            });
            t.run();
        });
        //Zprávy
        lVGroups.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            reloadMessagesByGroup(newValue);
        });
    }


    public void reloadMessagesByGroup(Group gp) {
        loading(2, true);
        Thread tt = new Thread(() -> {
            try {
                if (gp != null) {
                    lVGroupMessages.setItems(FXCollections.observableArrayList(messageDAO.getMessagesForGroupChat(gp)));
                } else {
                    lVGroupMessages.setItems(null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                loading(2, false);
            });
        });
        tt.run();
    }

    @FXML
    void btnChangeClicked(ActionEvent event) {
        if (lVGroupMessages.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/EditMessagePage.fxml"));
                Parent editMessagePane = loader.load();

                EditMessagePageController toolbox = loader.getController();
                EditMessageTab.setText("Editace zprávy");
                EditMessageTab.setClosable(true);
                toolbox.initDataFromToolBox(lVGroupMessages.getSelectionModel().getSelectedItem(), EditMessageTab, mdpc, this);
                EditMessageTab.setContent(editMessagePane);
                mdpc.selectTab(EditMessageTab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialog.show("Není zvolená žádná zpráva k editaci", Alert.AlertType.ERROR);
        }
    }

    /**
     * Vytvoření upozornění na načítání.
     *
     * @param hodnota -> 1)Skupina, 2)Message
     * @param state   -> True - Načítání
     */
    private void loading(int hodnota, boolean state) {
        switch (hodnota) {
            case 1:
                if (!state) {
                    lblGroups.setText("Skupiny předmětu:");
                } else {
                    lblGroups.setText("Skupiny předmětu: (Načítání.)");
                }
                break;
            case 2:
                if (!state) {
                    lblMessages.setText("Komentáře ve skupině:");
                } else {
                    lblMessages.setText("Komentáře ve skupině: (Načítání.)");
                }


        }
    }
}

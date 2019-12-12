package controller;

import controller.enums.ACTION_TYPE;
import data.FileDAO;
import data.FileDAOImpl;
import gui.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.File;
import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class EditFilePageController implements Initializable {

    private static File editedFile;
    public Button btnAddFile;
    public Label lblError;
    public TextField txtFieldTypeFile;
    private java.io.File selectedFile;

    private FileChooser fileChooser = new FileChooser();
    public ComboBox<ACTION_TYPE> actionTypeComboBox;

    private final FileDAO fileDAO = new FileDAOImpl();

    public static void setEditedFile(File file) {
        editedFile = file;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
    }

    @FXML
    private DatePicker dateFileEditedDate;

    @FXML
    private TextField txtFieldFileName;

    @FXML
    private TextField txtFieldFileSuffix;

    @FXML
    private DatePicker dateFileUploadedDate;

    @FXML
    void btnAddFileClicked(ActionEvent event) {
        selectedFile = fileChooser.showOpenDialog(Main.primaryStage);
        btnAddFile.setText(selectedFile.getName());
    }

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void initData() {
        if (editedFile != null) {
            txtFieldFileName.setText(editedFile.getName());
            txtFieldFileSuffix.setText(editedFile.getExtension());
            txtFieldTypeFile.setText(editedFile.getType());
            dateFileUploadedDate.setValue(convertToLocalDate(editedFile.getDate_created()));
            dateFileEditedDate.setValue(convertToLocalDate(editedFile.getDate_updated()));
            actionTypeComboBox.setItems(FXCollections.observableArrayList(ACTION_TYPE.UPDATE, ACTION_TYPE.DELETE));
            actionTypeComboBox.getSelectionModel().select(0);
        } else {
            dateFileEditedDate.setValue(LocalDate.now());
            dateFileUploadedDate.setValue(LocalDate.now());
            actionTypeComboBox.setItems(FXCollections.observableArrayList(ACTION_TYPE.INSERT));
            actionTypeComboBox.getSelectionModel().select(0);
        }
    }

    public void btnFileUpdateClicked(ActionEvent actionEvent) {
        File newFile = null;
        try {
            newFile = new File(
                    txtFieldFileName.getText(),
                    txtFieldFileSuffix.getText(),
                    txtFieldTypeFile.getText(),
                    selectedFile != null ? Files.readAllBytes(selectedFile.toPath()) : null,
                    Date.valueOf(dateFileUploadedDate.getValue()),
                    Date.valueOf(dateFileEditedDate.getValue())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            switch (actionTypeComboBox.getSelectionModel().getSelectedItem()) {
                case UPDATE:
                    newFile.setId(editedFile.getId());
                    fileDAO.updateFile(newFile);
                    break;
                case INSERT:
                    fileDAO.insertFile(newFile);
                    break;
                case DELETE:
                    newFile.setId(editedFile.getId());
                    fileDAO.deleteFile(newFile);
                    break;
                default:
                    break;
            }
            lblError.setText("Soubor se povedlo aktualizovat.");
        } catch(SQLException ex) {
            lblError.setText("Soubor se nepovedlo aktualizovat.\n" + ex.getMessage());
        }
    }
}

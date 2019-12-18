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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.File;
import model.Group;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ComboBox<ACTION_TYPE> actionTypeComboBox;

    private FileChooser fileChooser = new FileChooser();

    private final FileDAO fileDAO = new FileDAOImpl();

    private java.io.File selectedFile;
    private static AdministrationPageController parent;

    public static void setParams(File file, AdministrationPageController aP) {
        editedFile = file;
        parent = aP;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
    }

    public DatePicker dateFileEditedDate;
    public TextField txtFieldFileName;
    public TextField txtFieldFileSuffix;
    public DatePicker dateFileUploadedDate;

    @FXML
    void btnAddFileClicked(ActionEvent event) {
        selectedFile = fileChooser.showOpenDialog(Main.primaryStage);
        btnAddFile.setText(selectedFile.getName());
    } //Select file

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void reloadData() {
        editedFile = null;
        parent.refreshFiles();
        parent.stackPaneEditFile.getChildren().clear();
    }

    private void initData() {
        if (editedFile != null) {
            txtFieldFileName.setText(editedFile.getName());
            txtFieldFileSuffix.setText(editedFile.getExtension());
            txtFieldTypeFile.setText(editedFile.getType());
            dateFileUploadedDate.setValue(convertToLocalDate(editedFile.getDate_created()));
            dateFileEditedDate.setValue(convertToLocalDate(editedFile.getDate_updated()));
            actionTypeComboBox.setItems(FXCollections.observableArrayList(ACTION_TYPE.UPDATE, ACTION_TYPE.DELETE, ACTION_TYPE.DOWNLOAD));
            actionTypeComboBox.getSelectionModel().select(0);
        } else {
            dateFileEditedDate.setValue(LocalDate.now());
            dateFileUploadedDate.setValue(LocalDate.now());
            actionTypeComboBox.setItems(FXCollections.observableArrayList(ACTION_TYPE.INSERT));
            actionTypeComboBox.getSelectionModel().select(0);
        }
    } //Init values if we are editing file or creating

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
                    lblError.setText("Soubor se povedlo aktualizovat.");
                    break;
                case INSERT:
                    fileDAO.insertFile(newFile);
                    lblError.setText("Soubor vložen.");
                    break;
                case DELETE:
                    newFile.setId(editedFile.getId());
                    fileDAO.deleteFile(newFile);
                    lblError.setText("Soubor smazán");
                    break;
                case DOWNLOAD:
                    downloadFile(editedFile);
                    lblError.setText("Soubor stažen");
                    break;
                default:
                    break;
            }
            reloadData();
        } catch (SQLException ex) {
            lblError.setText("Soubor se nepovedlo aktualizovat.\n" + ex.getMessage());
        } //Perform action in db
    }

    private void downloadFile(File editedFile) {
        try {
            Path directory = Paths.get("files");
            if (Files.notExists(directory))
                Files.createDirectory(directory);

            Path path = Paths.get(directory + "/" + editedFile.getName() + editedFile.getExtension());
            Files.write(path, editedFile.getData());

            Hyperlink linkToFile = new Hyperlink(path.toAbsolutePath().toString());
            linkToFile.setOnAction((e) -> {
                try {
                    Desktop.getDesktop().open(path.toFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setHeaderText("Soubor stažen");
            dialog.getDialogPane().setContent(linkToFile);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Download file to files/
}

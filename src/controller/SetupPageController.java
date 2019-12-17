package controller;

import data.DatabaseInitDAO;
import data.DatabaseInitDAOImpl;
import data.FieldOfStudyDAOImpl;
import data.OracleConnection;
import gui.AlertDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import model.Configuration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SetupPageController implements Initializable {

    private ConfigurationHandler ch = new ConfigurationHandler();

    @FXML
    private TextField tFPort;

    @FXML
    private TextField tFAddress;

    @FXML
    private TextField tFUserName;

    @FXML
    private TextField tFDBMS;

    @FXML
    private PasswordField tFPassword;

    @FXML
    private TextField tFSID;

    @FXML
    private Button btnSave;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Configuration config = ch.getPropValues();
            tFUserName.setText(config.getUSERNAME());
            tFPassword.setText(config.getPASSWORD());
            tFAddress.setText(config.getSERVER_NAME());
            tFDBMS.setText(config.getDBMS());
            tFPort.setText(String.valueOf(config.getPORT()));
            tFSID.setText(config.getSID());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSaveClicked(ActionEvent event) {
        try{
            Configuration config = new Configuration(
                    tFUserName.getText(),
                    tFPassword.getText(),
                    tFAddress.getText(),
                    Integer.valueOf(tFPort.getText()),
                    tFDBMS.getText(),
                    tFSID.getText()
            );
            ch.setPropValues(config);
        } catch (IOException e){
            System.out.println(e);
        }
        try {
            Connection conn = OracleConnection.testConnection();
                    if(conn!=null){
                        AlertDialog.show("Připojení bylo úspěšné. Teď prosím ručně naimportujte SQL scripty pro inicializaci, a poté spuste aplikaci znovu.", Alert.AlertType.INFORMATION);
                        Stage stage = (Stage) btnSave.getScene().getWindow();
                        stage.close();
                    }else{
                        AlertDialog.show("Připojení k databázi se nezdařilo.", Alert.AlertType.ERROR);
                    }
        } catch (SQLException ex) {
            AlertDialog.show("Připojení k databázi se nezdařilo.", Alert.AlertType.ERROR);
        }

    }
}

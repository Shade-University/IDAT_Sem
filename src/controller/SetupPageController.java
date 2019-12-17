package controller;

import data.OracleConnection;
import gui.AlertDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Configuration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class SetupPageController implements Initializable {

    private ConfigurationHandler ch = new ConfigurationHandler();

    public TextField tFPort;
    public TextField tFAddress;
    public TextField tFUserName;
    public TextField tFDBMS;
    public PasswordField tFPassword;
    public TextField tFSID;
    public Button btnSave;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Configuration config = ch.getPropValues();
            tFUserName.setText(config.getUsername());
            tFPassword.setText(config.getPassword());
            tFAddress.setText(config.getServer_name());
            tFDBMS.setText(config.getDbms());
            tFPort.setText(String.valueOf(config.getPort()));
            tFSID.setText(config.getSid());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Default config to Senohrábek db

    @FXML
    void btnSaveClicked(ActionEvent event) {
        try{
            Configuration config = new Configuration(
                    tFUserName.getText(),
                    tFPassword.getText(),
                    tFAddress.getText(),
                    Integer.parseInt(tFPort.getText()),
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
    } //Save config
}

package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Configuration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SetupPageController implements Initializable {

    private ConfigurationHandler ch;

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
    private VBox toolsPane;

    @FXML
    private TextField tFSID;

    @FXML
    private Label lblState;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ch = new ConfigurationHandler();
        Configuration conf = new Configuration("t","t","t",123, "t","t");
        try {
            ch.setPropValues(conf);
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

    }

    @FXML
    void btnCreateDB(ActionEvent event) {

    }

    @FXML
    void btnInsertDB(ActionEvent event) {

    }

    @FXML
    void btnDeleteDB(ActionEvent event) {

    }
}

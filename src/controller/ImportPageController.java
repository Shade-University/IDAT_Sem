package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ImportPageController implements Initializable {
    public TextArea txtAreaLog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public void btnImportClicked(ActionEvent actionEvent) {

        URL oracle = null;
        try {
            oracle = new URL("https://stag-ws.upce.cz/ws/services/rest2/predmety/getPredmetyByKatedraFullInfo?katedra=FEI");
            HttpURLConnection conn = (HttpURLConnection) oracle.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            Reader in = new InputStreamReader(conn.getInputStream());

            JSONObject json = (JSONObject)JSONValue.parse(in);
            JSONArray jArray = (JSONArray)json.get("predmetKatedryFullInfo");
            for (Object o : jArray) {
                JSONObject object = (JSONObject)o;
                txtAreaLog.appendText(object.get("nazev") + ":\t" + object.get("nazevDlouhy") + "\n" );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

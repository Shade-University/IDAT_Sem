package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    public StackPane mainStackPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onHomeClicked(null);
    }

    public void onLoginClicked(MouseEvent mouseEvent) {
        switchMainPaneScene("/gui/LoginPage.fxml");
    } //Show login page

    public void onRegistrationClicked(MouseEvent mouseEvent) {
        switchMainPaneScene("/gui/RegistrationPage.fxml");
    } //Show registration page

    public void onHomeClicked(MouseEvent mouseEvent) {
        switchMainPaneScene("/gui/GuestViewPage.fxml");
    } //Show GuestView

    public void onGithubClicked(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/Shade55448/IDAT_Sem").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    } //Open Github in web browser

    public void onDocumentationClicked(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().open(new File("Dokumentace_IDAS2.docx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Open documentation

    private void switchMainPaneScene(String fxmlLocation) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fxmlLocation));
            mainStackPane.getChildren().clear();
            mainStackPane.getChildren().add(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } //Switch scenes in mainStackPane
}

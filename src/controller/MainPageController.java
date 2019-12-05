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
    public void initialize(URL url, ResourceBundle rb)  {
        onHomeClicked(null);
    }

    public void onLoginClicked(MouseEvent mouseEvent) {
        switchMainPaneScene("/gui/LoginPage.fxml");
    }

    public void onRegistrationClicked(MouseEvent mouseEvent) {
        switchMainPaneScene("/gui/RegistrationPage.fxml");
    }

    public void onHomeClicked(MouseEvent mouseEvent) {
        switchMainPaneScene("/gui/GuestView.fxml");
    }

    public void onGithubClicked(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/Shade55448/IDAT_Sem").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void onDocumentationClicked(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().open(new File("Funkční požadavky.docx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchMainPaneScene(String fxmlLocation) {
        try
        {
            Parent parent = FXMLLoader.load(getClass().getResource(fxmlLocation));
            mainStackPane.getChildren().clear();
            mainStackPane.getChildren().add(parent);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

    }

}

package controller;

import data.LikeDAO;
import data.LikeDAOImpl;
import gui.AlertDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.File;
import model.Like;
import model.Message;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;


public class MessageListCellController extends ListCell<Message> {

    @FXML
    private Text lblName;

    @FXML
    private TextFlow tAMessage;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    Button btnLike;

    @FXML
    Label lblLikeCount;

    private AnchorPane pane;

    private FXMLLoader mLLoader;

    private LikeDAO likeDAO = new LikeDAOImpl();

    private Like like;


    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        if(empty || message == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                try {
                    mLLoader = new FXMLLoader(getClass().getResource("/gui/MessageListCell.fxml"));
                    mLLoader.setController(this);
                    Parent toolBoxPane = mLLoader.load();
                    pane = new AnchorPane();
                    pane.getChildren().add(toolBoxPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                like = likeDAO.getLikeForMessage(MainDashboardPageController.getLoggedUser(), message);
                if (like != null) {
                    btnLike.setText("Unlike");
                } else {
                    btnLike.setText("Like");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                lblLikeCount.setText(String.valueOf(likeDAO.getLikeCount(message)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            btnLike.setOnAction((e) -> {
                try {
                    if (btnLike.getText().equals("Like")) {

                        like = new Like(MainDashboardPageController.getLoggedUser().getId(), message.getId());
                        likeDAO.createLike(like);
                        btnLike.setText("Unlike");
                    } else {
                        likeDAO.deleteLike(like);
                        btnLike.setText("Like");

                    }
                    lblLikeCount.setText(String.valueOf(likeDAO.getLikeCount(message)));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }); //On like click, create/remove like

            try {
                tAMessage.getChildren().clear();
                String space = new String(new char[(message.getLevel() * 10) - 10]).replace('\0', ' ');

                if (message.getSender() != null) {
                    if (message.getSender().equals(MainDashboardPageController.getLoggedUser())) {
                        btnLike.setVisible(false);
                    }
                    lblName.setText(space + message.getSender().getFirstName() + " " + message.getSender().getLastName() + " => ");
                } else {
                    lblName.setText(space + "Neexistující uživatel" + " => ");
                    btnLike.setVisible(false);
                }

                Text text = new Text(message.getContent());
                text.setFill(Color.BLUE);
                //Tabs as level in hierarchic message

                tAMessage.getChildren().addAll(lblName, text);
                if (message.getAttached_file() != null) {
                    File file = message.getAttached_file();

                    Hyperlink link = new Hyperlink((message.getAttached_file().getName() + message.getAttached_file().getExtension()));
                    link.setOnAction(event -> {
                        try {
                            Path directory = Paths.get("files");
                            if (Files.notExists(directory))
                                Files.createDirectory(directory);

                            Path path = Paths.get(directory + "/" + file.getName() + file.getExtension());
                            Files.write(path, file.getData());

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
                    });
                    tAMessage.getChildren().add(link); //Add attached file
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            setText(null);
            setGraphic(pane);
        }
    }
}
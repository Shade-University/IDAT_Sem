package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Message;
import model.User;

import java.io.IOException;


public class MessageListCellController extends ListCell<Message> {

    @FXML
    private Text lblName;

    @FXML
    private TextArea tAMessage;

    @FXML
    private AnchorPane anchorPane;

    private AnchorPane pane;

    private FXMLLoader mLLoader;


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
                String space = new String(new char[message.getLevel()*10]).replace('\0', ' ');
                lblName.setText(space + message.getOdesilatel().getFirstName() + " " + message.getOdesilatel().getLastName());
                tAMessage.setText(message.getObsah());
            } catch (Exception e){

            }

            setText(null);
            setGraphic(pane);
        }
    }
}
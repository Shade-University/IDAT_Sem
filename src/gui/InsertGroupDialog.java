
package gui;

import data.SubjectDAO;
import data.SubjectDAOImpl;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Predmet;
import model.Skupina;

/**
 *
 * @author Tomáš Vondra
 */
public class InsertGroupDialog extends Dialog<Skupina> {
    
    private final SubjectDAO subjectDAO = new SubjectDAOImpl();   
    
    public InsertGroupDialog(){
        
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label lblNazev = new Label("Název:");
        Label lblPopis = new Label("Popis:");
        Label lblPredmet = new Label("Předmět:");

        
        TextField txtFieldNazev = new TextField();       
        TextField txtFieldPopis = new TextField();
        
        ChoiceBox<Predmet> chPredmety = new ChoiceBox<>(FXCollections.observableArrayList(subjectDAO.getAllSubjects()));
        chPredmety.getSelectionModel().clearAndSelect(0);

        dialogPane.setContent(new VBox(
                new HBox(143, lblNazev, lblPopis, lblPredmet),
                new HBox(5, txtFieldNazev, txtFieldPopis, chPredmety)
        ));

        this.setResultConverter(
                (ButtonType button) -> {
                    if (button == ButtonType.OK) {
                        if (!txtFieldNazev.getText().isEmpty()
                        && !txtFieldPopis.getText().isEmpty()){
                         
                            return new Skupina(
                                    txtFieldNazev.getText(),
                                    txtFieldPopis.getText(),
                                    chPredmety.getSelectionModel().getSelectedItem()
                            );
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Chyba validace");
                            alert.setHeaderText("Hodnoty nesmí být prázdné");
                            alert.setContentText("");
                            alert.showAndWait();
                        }
                    }
                    return null;
                }
        );
    }

}

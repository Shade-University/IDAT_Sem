
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
public class EditGroupDialog extends Dialog<Skupina> {
    
    private final SubjectDAO subjectDAO = new SubjectDAOImpl();
    public EditGroupDialog(Skupina skupina){
        
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label lblId = new Label("Id:");
        Label lblNazev = new Label("Název:");
        Label lblPopis = new Label("Popis:");
        Label lblPredmet = new Label("Předmět:");

        TextField txtFieldId = new TextField();
        txtFieldId.setText(String.valueOf(skupina.getId()));      
        txtFieldId.editableProperty().set(false);
        
        TextField txtFieldNazev = new TextField();
        txtFieldNazev.setText(skupina.getNazev());
        
        TextField txtFieldPopis = new TextField();
        txtFieldPopis.setText(skupina.getPopis());
        
        ChoiceBox<Predmet> chPredmety = new ChoiceBox<>(FXCollections.observableArrayList(subjectDAO.getAllSubjects()));
        chPredmety.getSelectionModel().select(skupina.getPredmet());

        dialogPane.setContent(new VBox(
                new HBox(143, lblId, lblNazev, lblPopis, lblPredmet),
                new HBox(5, txtFieldId, txtFieldNazev, txtFieldPopis, chPredmety)
        ));

        this.setResultConverter(
                (ButtonType button) -> {
                    if (button == ButtonType.OK) {
                        if (!txtFieldNazev.getText().isEmpty()
                        && !txtFieldPopis.getText().isEmpty()){
                            skupina.setId(Integer.parseInt(txtFieldId.getText()));
                            skupina.setNazev(txtFieldNazev.getText());
                            skupina.setPopis(txtFieldPopis.getText());
                            skupina.setPredmet(chPredmety.getSelectionModel().getSelectedItem());
                            
                            return skupina;
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

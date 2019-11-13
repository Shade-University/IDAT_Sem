package gui;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import model.Obor;
import model.Predmet;

/**
 *
 * @author Tomáš Vondra
 */
public class InsertFieldDialog extends Dialog<Obor> {

    public InsertFieldDialog() {
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label lblNazev = new Label("Název:");
        Label lblPopis = new Label("Popis:");

        TextField txtFieldNazev = new TextField();
        TextField txtFieldPopis = new TextField();

        dialogPane.setContent(new VBox(
                new HBox(143, lblNazev, lblPopis),
                new HBox(5, txtFieldNazev, txtFieldPopis)
        ));

        this.setResultConverter(
                (ButtonType button) -> {
                    if (button == ButtonType.OK) {
                        if (!txtFieldNazev.getText().isEmpty()
                        && !txtFieldPopis.getText().isEmpty()) {
                            Obor o = new Obor(
                                    txtFieldNazev.getText(),
                                    txtFieldPopis.getText()
                            ); //TODO Opravdu musíme vytvářet novej objet ? Přece sem předáváme odkaz na obor, což je složitý struktura, tudíž by stačilo změnit jeho parametry :O
                            //Když to funguje, nesahej na to


                            return o; //TODO Nakonec je to stejný dialog jako v edit

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

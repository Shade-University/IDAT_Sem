package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Obor;
/**
 *
 * @author Tomáš Vondra
 */
public class EditFieldDialog extends Dialog<Obor> {

    public EditFieldDialog(Obor obor) {
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label lblNazev = new Label("Název:");
        Label lblPopis = new Label("Popis:");

        TextField txtFieldNazev = new TextField();
        txtFieldNazev.setText(obor.getNazev());
        TextField txtFieldPopis = new TextField();
        txtFieldPopis.setText(obor.getPopis());

        dialogPane.setContent(new VBox(
                new HBox(143, lblNazev, lblPopis),
                new HBox(5, txtFieldNazev, txtFieldPopis)
        ));

        this.setResultConverter(
                (ButtonType button) -> {
                    if (button == ButtonType.OK) {
                        if (!txtFieldNazev.getText().isEmpty()
                        && !txtFieldPopis.getText().isEmpty()) {
                            
                            obor.setNazev(txtFieldNazev.getText());
                            obor.setPopis(txtFieldPopis.getText());

                            return obor;
                            
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

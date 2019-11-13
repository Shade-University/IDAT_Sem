package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Uzivatel;

/**
 *
 * @author Tomáš Vondra
 */
public class EditUserDialog extends Dialog<Uzivatel> {

    public EditUserDialog(Uzivatel uzivatel) {
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label lblId = new Label("Id:");
        Label lblJmeno = new Label("Jméno:");
        Label lblPrijmeni = new Label("Přijmení:");
        Label lblEmail = new Label("Email:");

        TextField txtFieldId = new TextField();
        txtFieldId.setText(String.valueOf(uzivatel.getId()));
        txtFieldId.editableProperty().set(false);
        TextField txtFieldJmeno = new TextField();
        txtFieldJmeno.setText(uzivatel.getJmeno());
        TextField txtFieldPrijmeni = new TextField();
        txtFieldPrijmeni.setText(uzivatel.getPrijmeni());
        TextField txtFieldEmail = new TextField(); //Heslo z bezpečnostních důvodů nenačítám a tady také needituju
        txtFieldEmail.setText(uzivatel.getEmail());

        dialogPane.setContent(new VBox(
                new HBox(143, lblId, lblJmeno, lblPrijmeni, lblEmail),
                new HBox(5, txtFieldId, txtFieldJmeno, txtFieldPrijmeni, txtFieldEmail)
        ));

        this.setResultConverter(
                (ButtonType button) -> {
                    if (button == ButtonType.OK) {
                        if (!txtFieldJmeno.getText().isEmpty()
                        && !txtFieldPrijmeni.getText().isEmpty()
                        && !txtFieldEmail.getText().isEmpty()){
                            
                            uzivatel.setJmeno(txtFieldJmeno.getText());
                            uzivatel.setPrijmeni(txtFieldPrijmeni.getText());
                            uzivatel.setEmail(txtFieldEmail.getText());
                            
                            return uzivatel;
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

    private boolean validate(String id) {
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    } //TODO Zbytečné

}

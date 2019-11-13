package gui;

import data.FieldOfStudyDAO;
import data.FieldOfStudyDAOImpl;
import data.SubjectDAO;
import data.SubjectDAOImpl;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Obor;
import model.Predmet;
import model.Student;
import model.Ucitel;
import model.Uzivatel;

/**
 *
 * @author Tomáš Vondra
 */
public class InsertUserDialog extends Dialog<Uzivatel> {

    private final SubjectDAO subjectDAO;
    private final FieldOfStudyDAO fieldDAO;

    private TextField txtFieldJmeno;
    private TextField txtFieldPrijmeni;
    private TextField txtFieldEmail;
    private PasswordField pswField;
    private ChoiceBox<String> chBoxTyp;

    private ChoiceBox<Predmet> chBoxPredmet;
    private TextField txtFieldKatedra;

    private ChoiceBox<Obor> chBoxObor;
    private TextField txtFieldRokStudia;

    private Label lblKatedra;
    private Label lblPredmet;
    private Label lblRokStudia;
    private Label lblObor;

//    private HBox lblHbox;
//    private HBox txtHbox;
    private GridPane grid;

    public InsertUserDialog() {
        subjectDAO = new SubjectDAOImpl();
        fieldDAO = new FieldOfStudyDAOImpl();
        createGUI();

        this.setResultConverter(
                (ButtonType button) -> {
                    if (button == ButtonType.OK) {
                        if (validatePage()) {
                            Uzivatel uzivatel = getUser();

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

    private void createGUI() {
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        lblKatedra = new Label("Katedra:");
        lblPredmet = new Label("Vyučující předmět:");
        txtFieldKatedra = new TextField();
        chBoxPredmet = new ChoiceBox<>();
        chBoxPredmet.setItems(FXCollections.observableArrayList(subjectDAO.getAllSubjects()));
        chBoxPredmet.getSelectionModel().clearAndSelect(0);

        lblRokStudia = new Label("Rok studia:");
        lblObor = new Label("Obor:");
        txtFieldRokStudia = new TextField();
        chBoxObor = new ChoiceBox<>();
        chBoxObor.setItems(FXCollections.observableArrayList(fieldDAO.getAllFields()));
        chBoxObor.getSelectionModel().clearAndSelect(0);

        Label lblJmeno = new Label("Jméno:");
        Label lblPrijmeni = new Label("Přijmení:");
        Label lblEmail = new Label("Email:");
        Label lblHeslo = new Label("Heslo:");
        Label lblTyp = new Label("Typ uživatele:");
        txtFieldJmeno = new TextField();
        txtFieldPrijmeni = new TextField();
        txtFieldEmail = new TextField();
        pswField = new PasswordField();
        chBoxTyp = new ChoiceBox<>(FXCollections.observableArrayList("ucitel", "student", "admin"));

        chBoxTyp.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "ucitel":
                    hideStudentFields();
                    showTeacherFields();
                    break;
                case "student":
                    hideTeacherFields();
                    showStudentFields();
                    break;
                default:
                    hideTeacherFields();
                    hideStudentFields();
                    break;
            }
        });

//        lblHbox = new HBox(100, lblJmeno, lblPrijmeni, lblEmail, lblHeslo, lblTyp, lblKatedra, lblPredmet, lblRokStudia, lblObor);
//        txtHbox = new HBox(5, txtFieldJmeno, txtFieldPrijmeni, txtFieldEmail, pswField, chBoxTyp, txtFieldKatedra, txtFieldRokStudia, chBoxPredmet);
//        lblHbox.setAlignment(Pos.CENTER);
//        dialogPane.setContent(new VBox(lblHbox, txtHbox));
        grid = new GridPane();
        grid.setHgap(5);

        grid.addColumn(0, lblJmeno, txtFieldJmeno);
        grid.addColumn(1, lblPrijmeni, txtFieldPrijmeni);
        grid.addColumn(2, lblEmail, txtFieldEmail);
        grid.addColumn(3, lblHeslo, pswField);
        grid.addColumn(4, lblTyp, chBoxTyp);

        chBoxTyp.getSelectionModel().clearAndSelect(0);
        dialogPane.setContent(grid);
    }

    private boolean validatePage() {
        if (txtFieldJmeno.getText().isEmpty()) {
            return false;
        } else if (txtFieldPrijmeni.getText().isEmpty()) {
            return false;
        } else if (txtFieldEmail.getText().isEmpty()) {
            return false;
        } else if (pswField.getText().isEmpty()) {
            return false;
        }

        if (chBoxTyp.getSelectionModel().getSelectedItem().equals("ucitel")) {
            if (txtFieldKatedra.getText().isEmpty()) {
                return false;
            }
        } else if (chBoxTyp.getSelectionModel().getSelectedItem().equals("student")) {
            if (txtFieldRokStudia.getText().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private Uzivatel getUser() {
        Uzivatel uzivatel;

        switch (chBoxTyp.getSelectionModel().getSelectedItem()) {
            case "admin":
                uzivatel = new Uzivatel(
                        txtFieldJmeno.getText(),
                        txtFieldPrijmeni.getText(),
                        txtFieldEmail.getText(),
                        chBoxTyp.getSelectionModel().getSelectedItem(),
                        pswField.getText()
                );
                break;
            case "ucitel":
                uzivatel = new Ucitel(chBoxPredmet.getSelectionModel().getSelectedItem(),
                        txtFieldKatedra.getText(),
                        txtFieldJmeno.getText(),
                        txtFieldPrijmeni.getText(),
                        txtFieldEmail.getText(),
                        pswField.getText()
                );
                break;
            default:
                uzivatel = new Student(
                        chBoxObor.getSelectionModel().getSelectedItem(),
                        txtFieldRokStudia.getText(),
                        txtFieldJmeno.getText(),
                        txtFieldPrijmeni.getText(),
                        txtFieldEmail.getText(),
                        pswField.getText()
                );
                break;
        }

        return uzivatel;
    }

    private void hideStudentFields() {

        grid.getChildren().remove(lblRokStudia);
        grid.getChildren().remove(lblObor);
        grid.getChildren().remove(chBoxObor);
        grid.getChildren().remove(txtFieldRokStudia);

//        lblHbox.getChildren().remove(lblObor);
//        lblHbox.getChildren().remove(lblRokStudia);
//        txtHbox.getChildren().remove(chBoxObor);
//        txtHbox.getChildren().remove(txtFieldRokStudia);
    }

    private void showStudentFields() {
        grid.addColumn(5, lblRokStudia, txtFieldRokStudia);
        grid.addColumn(6, lblObor, chBoxObor);

//        lblHbox.getChildren().add(lblObor);
//        lblHbox.getChildren().add(lblRokStudia);
//        txtHbox.getChildren().add(chBoxObor);
//        txtHbox.getChildren().add(txtFieldRokStudia);
    }

    private void hideTeacherFields() {
        grid.getChildren().remove(lblPredmet);
        grid.getChildren().remove(lblKatedra);
        grid.getChildren().remove(chBoxPredmet);
        grid.getChildren().remove(txtFieldKatedra);

//        lblPredmet.setVisible(false);
//        lblKatedra.setVisible(false);
//        chBoxPredmet.setVisible(false);
//        txtFieldKatedra.setVisible(false);
    }

    private void showTeacherFields() {

        grid.addColumn(5, lblKatedra, txtFieldKatedra);
        grid.addColumn(6, lblPredmet, chBoxPredmet);
//        lblHbox.getChildren().add(lblPredmet);
//        lblHbox.getChildren().add(lblKatedra);
//        txtHbox.getChildren().add(chBoxPredmet);
//        txtHbox.getChildren().add(txtFieldKatedra);
    }

}

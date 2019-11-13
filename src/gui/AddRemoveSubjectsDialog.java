package gui;

import data.SubjectDAO;
import data.SubjectDAOImpl;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Obor;
import model.Predmet;

/**
 *
 * @author Tomáš Vondra
 */
public class AddRemoveSubjectsDialog extends Dialog {

    private final SubjectDAO subjectDAO = new SubjectDAOImpl();

    public AddRemoveSubjectsDialog(Obor obor) {
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ObservableList<Predmet> predmety = FXCollections.observableArrayList(subjectDAO.getAllSubjects());
        subjectDAO.getSubjectsForField(obor).forEach(e -> predmety.remove(e));

        ListView<Predmet> predmetyAll = new ListView<>(predmety);
        predmetyAll.setEditable(false);
        predmetyAll.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ListView<Predmet> predmetyIn = new ListView<>(FXCollections.observableArrayList(subjectDAO.getSubjectsForField(obor)));
        predmetyIn.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        predmetyIn.setEditable(false);

        Button remove = new Button("Odstranit předměty");
        remove.setOnAction((e) -> {
            List<Predmet> p = predmetyIn.getSelectionModel().getSelectedItems();
            subjectDAO.removeSubjectsFromField(p, obor);
            p.forEach((s) -> { 
                predmetyAll.getItems().add(s);
                predmetyIn.getItems().remove(s);
                    });
        });

        Button add = new Button("Přidat předměty");
        add.setOnAction((e) -> {
            List<Predmet> p = predmetyAll.getSelectionModel().getSelectedItems();
            subjectDAO.insertSubjectsToField(p, obor);
            p.forEach(s -> {
                predmetyAll.getItems().remove(s);
                predmetyIn.getItems().add(s);
            });

        });

        dialogPane.setContent(new HBox(5,
                new VBox(5, predmetyIn, remove),
                new VBox(5, predmetyAll, add)));
    }

}

package controller;

import data.GroupDAOImpl;
import data.MessageDAO;
import data.MessageDAOImpl;
import data.UserDAOImpl;
import gui.Main;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Skupina;
import model.Uzivatel;
import model.Zprava;
import data.UserDAO;
import data.GroupDAO;
import data.RatingDAO;
import data.RatingDAOImpl;
import gui.EditUserDialog;
import java.text.DecimalFormat;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import model.Hodnoceni;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MainDashboardPageController implements Initializable {

    @FXML
    private Label lblCeleJmeno;
    @FXML
    private ListView<Uzivatel> listViewUsers;
    @FXML
    private ListView<Skupina> listViewGroups;
    @FXML
    private TextArea textAreaChat;
    @FXML
    private TextField txtFieldMessage;
    @FXML
    private Label lblHodnoceni;
    @FXML
    private Button btnOdhlasit;
    @FXML
    private Button btnSend;
    @FXML
    private Button btnAdmin;
    @FXML
    private Button btnEdit;

    private final UserDAO userDAO = new UserDAOImpl();
    private final GroupDAO groupDAO = new GroupDAOImpl();
    private final MessageDAO messageDAO = new MessageDAOImpl();
    private final RatingDAO ratingDAO = new RatingDAOImpl();

    private static Uzivatel prihlasenyUzivatel;

    public static void setUzivatel(Uzivatel uzivatel) {
        prihlasenyUzivatel = uzivatel;
    }
    public static Uzivatel getUzivatel(){
        return prihlasenyUzivatel;
    }
    
    private void refreshController(){
        if("admin".equals(prihlasenyUzivatel.getUzivatelskyTyp())){
            btnAdmin.setVisible(true);
        }
        
        lblCeleJmeno.setText("Uživatel: " + prihlasenyUzivatel.getJmeno() + " " + prihlasenyUzivatel.getPrijmeni());
        listViewGroups.setItems(FXCollections.observableArrayList(groupDAO.getUserGroups(prihlasenyUzivatel)));

        listViewUsers.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Uzivatel> observable,
                        Uzivatel oldValue, Uzivatel newValue) -> {
                    if (newValue != null) {
                        listViewGroups.getSelectionModel().clearSelection();

                        updateTextArea(messageDAO.getMessagesForChatBetween(
                                prihlasenyUzivatel, listViewUsers.getSelectionModel().getSelectedItem()));
                    } //Pokud to není deselect, deselectni skupinu a získej zprávy se zadaným uživatelem
                }
        );

        listViewGroups.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Skupina> observable, Skupina oldValue, Skupina newValue) -> {
                    if (newValue != null) {
                        listViewUsers.getSelectionModel().clearSelection();

                        Collection<Uzivatel> uzivatele = userDAO.getAllUsersFromGroup(newValue);
                        uzivatele.remove(prihlasenyUzivatel);
                        listViewUsers.setItems(FXCollections.observableArrayList(
                                uzivatele));

                        double rating = ratingDAO.getAverageRating(newValue);
                        if (rating == 0) {
                            lblHodnoceni.setText("Nehodnoceno");
                        } else {
                            lblHodnoceni.setText("Hodnocení: " + new DecimalFormat("#.#").format(rating) + "/5");
                        }
                        updateTextArea(messageDAO.getMessagesForGroupChat(newValue));
                    } //Pokud to není deselect, získej uživatele zadané skupiny (bez sebe), zprávy skupiny a její hodnocení
                });
    }

    private void updateTextArea(Collection<Zprava> kolekce) {
        textAreaChat.clear();
        kolekce.forEach(zprava -> textAreaChat.appendText(zprava + "\n"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshController(); //Nastav jméno přihlášeného uživatele a získej jeho skupiny
    }

    @FXML
    private void btnOdhlasitClicked(ActionEvent event) {
        try {
            prihlasenyUzivatel = null;
            Main.switchScene("/gui/LoginPage.fxml");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboardPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnSendClicked(ActionEvent event) {
        Zprava zprava;
        String obsah = txtFieldMessage.getText();

        if (!listViewGroups.getSelectionModel().isEmpty()) {
            Skupina skupina = listViewGroups.getSelectionModel().getSelectedItem();
            zprava = new Zprava("Zpráva: " + skupina.getNazev(),
                    obsah, prihlasenyUzivatel, null, skupina);
            messageDAO.createMessage(zprava);
            
            listViewGroups.getSelectionModel().clearSelection();
            listViewGroups.getSelectionModel().select(skupina); //Refresh

        } else {
            Uzivatel uzivatel = listViewUsers.getSelectionModel().getSelectedItem();
            zprava = new Zprava("Zpráva pro uživatele: " + uzivatel.getJmeno(),
                    obsah, prihlasenyUzivatel, uzivatel, null);
            messageDAO.createMessage(zprava);
            
            listViewUsers.getSelectionModel().clearSelection();
            listViewUsers.getSelectionModel().select(uzivatel); //Refresh
        } //Vytvoř zprávu buď pro skupinu nebo uživatele

    }

    @FXML
    private void btnEditClicked(ActionEvent event) {
        EditUserDialog edit = new EditUserDialog(prihlasenyUzivatel);
        edit.showAndWait().ifPresent((u) -> {
            try {
                userDAO.updateUser(u);
            } catch (SQLException e) {
                new Alert(AlertType.ERROR, e.getMessage().split("\\n")[0]).showAndWait();
                return;
            }

            prihlasenyUzivatel = u; //Refresh. Na refresh listů asi prdím
            lblCeleJmeno.setText("Uživatel: " + prihlasenyUzivatel.getJmeno() + " " + prihlasenyUzivatel.getPrijmeni());
        });
    }

    @FXML
    private void onMenuContextRatingAction(ActionEvent event) {
        if (listViewGroups.getSelectionModel().isEmpty()) {
            new Alert(AlertType.ERROR, "Nevybrána skupina").showAndWait();
            return;
        }
        ChoiceDialog<Integer> ratingDialog = new ChoiceDialog<>(1, 1, 2, 3, 4, 5);
        ratingDialog.setContentText("Ohodnoťte skupinu 1-5 (5 nejlepší)");
        ratingDialog.setHeaderText("");

        ratingDialog.showAndWait().ifPresent((r) -> {
            Skupina s = listViewGroups.getSelectionModel().getSelectedItem();
            Hodnoceni h = new Hodnoceni(r, "Uživatelské hodnocení", prihlasenyUzivatel, s);
            ratingDAO.createRating(h);
            listViewGroups.getSelectionModel().clearSelection();
            listViewGroups.getSelectionModel().select(s); //Refresh
        });
    }

    @FXML
    private void btnAdminClicked(ActionEvent event) {
        try {
            Main.switchScene("/gui/AdministrationPage.fxml");
        } catch (IOException ex) {
            Logger.getLogger(MainDashboardPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

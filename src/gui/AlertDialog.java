package gui;

import javafx.scene.control.Alert;

public class AlertDialog {
    /**
     * Vytvoří Alert dialog
     *
     * @param message - Zobrazovaná zpráva
     * @param type    - Typ dialogu information/ERROR
     */
    public static void show(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        if (type == Alert.AlertType.ERROR) {
            alert.setTitle("Chyba při provádění požadované akce");
            alert.setHeaderText("Chyba při provádění požadované akce");
        } else {
            alert.setTitle("Operace byla provedena");
            alert.setHeaderText(null);
        }
        alert.setContentText(message);
        alert.showAndWait();
    }
}

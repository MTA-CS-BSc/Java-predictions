package admin.client.consts;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public abstract class Alerts {
    public static void showAlert(String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

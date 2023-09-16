package fx.modules;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public abstract class Alerts {
    public static void showAlert(String title, String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}

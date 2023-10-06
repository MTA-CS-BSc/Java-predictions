package fx.xml.loader.controllers;

import api.mgmt.xml.loader.HttpXmlLoader;
import consts.Alerts;
import consts.Animations;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modules.Constants;
import okhttp3.Response;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class XmlLoaderController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    @FXML
    private void handleLoadXml(ActionEvent event) {
        Window window = ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a XML file");
        File file = fileChooser.showOpenDialog(window);

        if (file != null)
            handleNotNullXmlFileEntered(file);
    }

    private void handleNotNullXmlFileEntered(File file) {
        try {
            TrayNotification tray;
            String title, body;
            NotificationType type;
            Response response = HttpXmlLoader.uploadXml(file);

            if (response.isSuccessful()) {
                title = "SUCCESS";
                body = "XML was loaded successfully!";
                type = NotificationType.SUCCESS;
            } else {
                response.close();
                title = "FAILURE";
                body = "XML was not loaded!";
                type = NotificationType.ERROR;

                if (!Objects.isNull(response.body()))
                    Alerts.showAlert( "XML was not loaded", response.body().string(), Alert.AlertType.ERROR);
            }

            if (Animations.IS_ANIMATIONS_ON.getValue()) {
                tray = new TrayNotification(title, body, type);
                tray.setAnimationType(AnimationType.FADE);
                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
            }
        } catch (Exception ignored) {
            Alerts.showAlert("XML was not loaded", "File not found, not supported or corrupted", Alert.AlertType.ERROR);
        }
        //#endregion
    }
}

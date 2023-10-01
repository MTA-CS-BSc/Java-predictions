package admin.client.fx.xml.loader.controllers;

import admin.client.api.xml.loader.HttpXmlLoader;
import admin.client.consts.Alerts;
import dtos.ResponseDTO;
import helpers.Constants;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class XmlLoaderController implements Initializable {
    private BooleanProperty isAnimationsOn;

    @FXML private TextArea currentXmlFilePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isAnimationsOn = new SimpleBooleanProperty();
    }

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
            ResponseDTO response = HttpXmlLoader.uploadXml(file);
            String title, body;
            NotificationType type;

            if (response.getStatus() == Constants.API_RESPONSE_OK) {
                title = "SUCCESS";
                body = "XML was loaded successfully!";
                type = NotificationType.SUCCESS;
                currentXmlFilePath.setText(file.getAbsolutePath());
            } else {
                title = "FAILURE";
                body = "XML was not loaded!";
                type = NotificationType.ERROR;
                Alerts.showAlert( "XML was not loaded", response.getErrorDescription().getCause(), Alert.AlertType.ERROR);
            }

            if (isAnimationsOn.getValue()) {
                tray = new TrayNotification(title, body, type);
                tray.setAnimationType(AnimationType.FADE);
                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
            }
        } catch (Exception ignored) {
            Alerts.showAlert("XML was not loaded", "File not found, not supported or corrupted", Alert.AlertType.ERROR);
        }
        //#endregion
    }

    public void setIsAnimationsOn(boolean isAnimationsOn) {
        this.isAnimationsOn.setValue(isAnimationsOn);
    }

    public boolean isAnimationOn() { return isAnimationsOn.getValue(); }
}

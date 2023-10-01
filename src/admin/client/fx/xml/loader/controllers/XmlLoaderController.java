package admin.client.fx.xml.loader.controllers;

import admin.client.api.Configuration;
import dtos.ResponseDTO;
import helpers.Constants;
import helpers.modules.SingletonObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import okhttp3.*;
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
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileField",file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        Request request = new Request.Builder()
                .url(Configuration.SERVER_URL + "/xml/load")
                .post(requestBody)
                .build();

        try {
            Response httpResponse = Configuration.HTTP_CLIENT.newCall(request).execute();
            ResponseDTO response = SingletonObjectMapper.objectMapper.readValue(httpResponse.body().string(), ResponseDTO.class);

            TrayNotification tray;

            if (response.getStatus() == Constants.API_RESPONSE_OK) {
                tray = new TrayNotification("SUCCESS", "XML was loaded successfully!", NotificationType.SUCCESS);
                currentXmlFilePath.setText(file.getAbsolutePath());
//                detailsScreenController.handleShowCategoriesData();
            } else {
                tray = new TrayNotification("FAILURE", "XML was not loaded.", NotificationType.ERROR);
//                Alerts.showAlert("XML was not loaded", "Validation error",
//                        response.getErrorDescription().getCause(), Alert.AlertType.ERROR);
            }

            if (isAnimationsOn.getValue()) {
                tray.setAnimationType(AnimationType.FADE);
                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
            }

//            showSimulationDetailsScreen();
        } catch (Exception ignored) {
//            Alerts.showAlert("XML was not loaded", "Validation error",
//                    "File not found, not supported or corrupted", Alert.AlertType.ERROR);
        }
        //#endregion
    }

    public void setIsAnimationsOn(boolean isAnimationsOn) {
        this.isAnimationsOn.setValue(isAnimationsOn);
    }

    public boolean isAnimationOn() { return isAnimationsOn.getValue(); }
}

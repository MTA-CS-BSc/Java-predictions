package admin.client.fx.views.xml.loader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class XmlLoaderController implements Initializable {
    @FXML private TextArea currentXmlFilePath;

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
        //TODO: Rewrite logic

        //#region OldCode
//        try {
//            ResponseDTO response = SingletonEngineAPI.api.loadXml(file.getAbsolutePath());
//            TrayNotification tray;
//
//            if (response.getStatus() == Constants.API_RESPONSE_OK) {
//                tray = new TrayNotification("SUCCESS", "XML was loaded successfully!", NotificationType.SUCCESS);
//                currentXmlFilePath.setText(file.getAbsolutePath());
//                detailsScreenController.handleShowCategoriesData();
//            } else {
//                tray = new TrayNotification("FAILURE", "XML was not loaded.", NotificationType.ERROR);
//                Alerts.showAlert("XML was not loaded", "Validation error",
//                        response.getErrorDescription().getCause(), Alert.AlertType.ERROR);
//            }
//
//            if (isAnimationsOn.getValue()) {
//                tray.setAnimationType(AnimationType.FADE);
//                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
//            }
//
//            showSimulationDetailsScreen();
//        } catch (Exception e) {
//            Alerts.showAlert("XML was not loaded", "Validation error",
//                    "File not found, not supported or corrupted", Alert.AlertType.ERROR);
//        }
        //#endregion
    }
}

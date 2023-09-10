package fx.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.ResponseDTO;
import engine.EngineAPI;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppBarController implements Initializable {
    private EngineAPI engineAPI;
    private Alert xmlErrorsAlert;

    @FXML
    private Button detailsButton;
    @FXML
    private Button newExecutionButton;
    @FXML
    private Button resultsButton;
    @FXML
    private TextArea currentXmlFilePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engineAPI = new EngineAPI();
        initializeXmlErrorsAlert();

        detailsButton.disableProperty().bind(Bindings.isEmpty(currentXmlFilePath.textProperty()));
        newExecutionButton.disableProperty().bind(Bindings.isEmpty(currentXmlFilePath.textProperty()));
        resultsButton.disableProperty().bind(Bindings.createBooleanBinding(this::isHistoryEmpty));
    }

    private void initializeXmlErrorsAlert() {
        xmlErrorsAlert = new Alert(Alert.AlertType.INFORMATION);
        xmlErrorsAlert.setResizable(true);
        xmlErrorsAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        xmlErrorsAlert.setTitle("Latest loaded XML inspection");
        xmlErrorsAlert.setHeaderText("Validation errors");
    }

    private boolean isHistoryEmpty() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return !objectMapper.readValue(engineAPI.isXmlLoaded().getData(), Boolean.class)
                || objectMapper.readValue(engineAPI.isHistoryEmpty().getData(), Boolean.class);
    }

    @FXML
    private void handleLoadXmlButtonClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Window window = scene.getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an XML file");
        File file = fileChooser.showOpenDialog(window);

        if (file != null)
            handleNotNullXmlFileEntered(file);
    }

    private void handleNotNullXmlFileEntered(File file) {
        try {
            ResponseDTO response = engineAPI.loadXml(file.getAbsolutePath());
            TrayNotification tray;

            if (response.getStatus() == 200) {
                tray = new TrayNotification("SUCCESS", "XML was loaded successfully!", NotificationType.SUCCESS);
                currentXmlFilePath.setText(file.getAbsolutePath());
            }

            else {
                tray = new TrayNotification("FAILURE", "XML was not loaded. For details, see the XML log.", NotificationType.ERROR);
                xmlErrorsAlert.setContentText(response.getErrorDescription().getCause());
                xmlErrorsAlert.showAndWait();
            }

            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(new Duration(2000));
        }

        catch (Exception e) {
            xmlErrorsAlert.setContentText("File not found or corrupted!");
            xmlErrorsAlert.showAndWait();
        }
    }

    public void handleShowSimulationDetails() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (objectMapper.readValue(engineAPI.isXmlLoaded().getData(), Boolean.class))
            System.out.println("Not implemented");
//            handleShowCategoriesData();

        else {
            TrayNotification tray = new TrayNotification("FAILURE", "XML was not loaded, nothing to show.", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(new Duration(2000));
        }
    }

    public EngineAPI getEngineAPI() {
        return engineAPI;
    }
}

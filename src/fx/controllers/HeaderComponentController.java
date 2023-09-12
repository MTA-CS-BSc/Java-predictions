package fx.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.ResponseDTO;
import fx.modules.SingletonEngineAPI;
import helpers.modules.SingletonObjectMapper;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
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

public class HeaderComponentController implements Initializable {
    private Alert xmlErrorsAlert;
    @FXML
    private Button detailsButton;
    @FXML
    private Button newExecutionButton;
    @FXML
    private Button resultsButton;
    @FXML
    private TextArea currentXmlFilePath;

    private DetailsScreenController detailsScreenController;
    private NewExecutionController newExecutionController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public void setDetailsScreenController(DetailsScreenController controller) {
        detailsScreenController = controller;
    }

    public void setNewExecutionController(NewExecutionController controller) {
        newExecutionController = controller;
    }

    private boolean isHistoryEmpty() throws JsonProcessingException {
        ObjectMapper objectMapper = SingletonObjectMapper.objectMapper;

        return !objectMapper.readValue(SingletonEngineAPI.api.isXmlLoaded().getData(), Boolean.class)
                || objectMapper.readValue(SingletonEngineAPI.api.isHistoryEmpty().getData(), Boolean.class);
    }

    @FXML
    private void handleLoadXml(ActionEvent event) {
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
            ResponseDTO response = SingletonEngineAPI.api.loadXml(file.getAbsolutePath());
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

    @FXML
    private void handleShowSimulationDetails() throws JsonProcessingException {
        if (SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api.isXmlLoaded().getData(), Boolean.class))
            detailsScreenController.handleShowCategoriesData();

        else {
            TrayNotification tray = new TrayNotification("FAILURE", "XML was not loaded, nothing to show.", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(new Duration(2000));
        }
    }

    @FXML
    private void handleNewExecution() throws Exception {
        if (!newExecutionController.getContainer().isVisible()) {
            fadeOutAnimation(detailsScreenController.getContainer());
            prepareSimulation();
            fadeInAnimation(newExecutionController.getContainer());
        }
    }

    private void prepareSimulation() throws Exception {
        String uuid = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api.createSimulation().getData(),
                String.class);

        newExecutionController.initializeEntitiesTable(uuid);
    }

    private void fadeInAnimation(Pane root) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeIn.setOnFinished(event -> root.setVisible(true));
        fadeIn.play();
    }
    private void fadeOutAnimation(Pane root) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(1200), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> root.setVisible(false));
        fadeOut.play();

        root.setVisible(true);
    }
}

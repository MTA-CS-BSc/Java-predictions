package fx.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.ResponseDTO;
import fx.modules.Alerts;
import fx.modules.GuiUtils;
import fx.modules.SingletonEngineAPI;
import helpers.Constants;
import helpers.modules.SingletonObjectMapper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderComponentController implements Initializable {
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
    private ResultsScreenController resultsScreenController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailsButton.disableProperty().bind(Bindings.isEmpty(currentXmlFilePath.textProperty()));
        newExecutionButton.disableProperty().bind(Bindings.isEmpty(currentXmlFilePath.textProperty()));
        resultsButton.disableProperty().bind(Bindings.isEmpty(currentXmlFilePath.textProperty()));
    }

    public void setDetailsScreenController(DetailsScreenController controller) {
        detailsScreenController = controller;
    }

    public void setNewExecutionController(NewExecutionController controller) {
        newExecutionController = controller;
    }

    public void setResultsScreenController(ResultsScreenController controller) { resultsScreenController = controller; }

    @FXML
    private void handleLoadXml(ActionEvent event) {
        Window window = ((Node)event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a XML file");
        File file = fileChooser.showOpenDialog(window);

        if (file != null)
            handleNotNullXmlFileEntered(file);
    }

    private void handleNotNullXmlFileEntered(File file) {
        try {
            ResponseDTO response = SingletonEngineAPI.api.loadXml(file.getAbsolutePath());
            TrayNotification tray;

            if (response.getStatus() == Constants.API_RESPONSE_OK) {
                tray = new TrayNotification("SUCCESS", "XML was loaded successfully!", NotificationType.SUCCESS);
                currentXmlFilePath.setText(file.getAbsolutePath());
                detailsScreenController.handleShowCategoriesData();
            }

            else {
                tray = new TrayNotification("FAILURE", "XML was not loaded.", NotificationType.ERROR);
                Alerts.showAlert("XML was not loaded", "Validation error",
                        response.getErrorDescription().getCause(), Alert.AlertType.ERROR);
            }

            tray.setAnimationType(AnimationType.FADE);
            Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
        }

        catch (Exception e) {
            Alerts.showAlert("XML was not loaded", "Validation error",
                    "File not found, not supported or corrupted", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void showSimulationDetailsScreen() throws JsonProcessingException {
        if (!detailsScreenController.getContainer().isVisible()) {
            if (SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api.isXmlLoaded().getData(), Boolean.class)) {
                Platform.runLater(() -> {
                    try {
                        hideVisible();
                        GuiUtils.fadeInAnimation(detailsScreenController.getContainer());
                        detailsScreenController.handleShowCategoriesData();
                    }

                    catch (Exception ignored) { }
                });
            }

            else {
                TrayNotification tray = new TrayNotification("FAILURE", "XML was not loaded, nothing to show.", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.FADE);
                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
            }
        }
    }

    @FXML
    private void showNewExecutionScreen() {
        if (!newExecutionController.getContainer().isVisible()) {
            Platform.runLater(() -> {
                try {
                    hideVisible();
                    prepareSimulation();
                    GuiUtils.fadeInAnimation(newExecutionController.getContainer());
                } catch (Exception ignored) { }
            });
        }
    }

    private void hideVisible() {
        Platform.runLater(() -> {
            if (detailsScreenController.getContainer().isVisible())
                GuiUtils.fadeOutAnimation(detailsScreenController.getContainer());

            else if (resultsScreenController.getContainer().isVisible())
                GuiUtils.fadeOutAnimation(resultsScreenController.getContainer());

            else if (newExecutionController.getContainer().isVisible())
                GuiUtils.fadeOutAnimation(newExecutionController.getContainer());
        });
    }

    @FXML
    public void showResultsScreen() {
        if (!resultsScreenController.getContainer().isVisible()) {
            Platform.runLater(() -> {
                hideVisible();
                GuiUtils.fadeInAnimation(resultsScreenController.getContainer());
            });
        }
    }

    private void prepareSimulation() throws Exception {
        String uuid = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api.createSimulation().getData(), String.class);
        newExecutionController.setSimulationUuid(uuid);
    }

}

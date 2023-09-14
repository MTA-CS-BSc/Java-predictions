package fx.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

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

        highlightButtonText(detailsButton);
    }

    private void highlightButtonText(Button button) {
        Stream.of(detailsButton, newExecutionButton, resultsButton)
                .filter(element -> element != button)
                .forEach(this::unHighlightButtonText);

        if (!button.getStyle().contains(" -fx-font-weight: bold"))
            button.setStyle(button.getStyle() + " -fx-font-weight: bold");
    }

    private void unHighlightButtonText(Button button) {
        Font existingFont = button.getFont();
        Font newFont = Font.font(existingFont.getFamily(), FontWeight.NORMAL, existingFont.getSize());
        button.setFont(newFont);

        if (button.getStyle().contains(" -fx-font-weight: bold"))
            button.setStyle(button.getStyle().replace(" -fx-font-weight: bold", ""));
    }

    public void setDetailsScreenController(DetailsScreenController controller) {
        detailsScreenController = controller;
    }

    public void setNewExecutionController(NewExecutionController controller) {
        newExecutionController = controller;
    }

    public void setResultsScreenController(ResultsScreenController controller) { resultsScreenController = controller; }

    @FXML
    private void handleLoadXml(ActionEvent event) throws JsonProcessingException {
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
            showSimulationDetailsScreen();
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
                highlightButtonText(detailsButton);
                hideVisible();
                detailsScreenController.handleShowCategoriesData();
                Platform.runLater(() ->  GuiUtils.fadeInAnimation(detailsScreenController.getContainer()));
            }

            else {
                TrayNotification tray = new TrayNotification("FAILURE", "XML was not loaded, nothing to show.", NotificationType.ERROR);
                tray.setAnimationType(AnimationType.FADE);
                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
            }
        }
    }

    @FXML
    private void showNewExecutionScreen() throws Exception {
        String uuid = SingletonObjectMapper.objectMapper.readValue(
                SingletonEngineAPI.api.createSimulation().getData(),
                String.class);

        showNewExecutionScreenFromUuid(uuid);
    }

    public void showNewExecutionScreenFromUuid(String uuid) throws Exception {
        if (!newExecutionController.getContainer().isVisible()) {
            prepareSimulation(uuid);
            highlightButtonText(newExecutionButton);
            hideVisible();
            Platform.runLater(() -> GuiUtils.fadeInAnimation(newExecutionController.getContainer()));
        }
    }

    private void hideVisible() {
        Platform.runLater(() -> {
            if (detailsScreenController.getContainer().isVisible())
                GuiUtils.fadeOutAnimation(detailsScreenController.getContainer());

            else if (resultsScreenController.getContainer().isVisible()) {
                GuiUtils.fadeOutAnimation(resultsScreenController.getContainer());
                resultsScreenController.setSelectedSimulation(null);
            }

            else if (newExecutionController.getContainer().isVisible())
                GuiUtils.fadeOutAnimation(newExecutionController.getContainer());
        });
    }

    @FXML
    public void showResultsScreen() {
        if (!resultsScreenController.getContainer().isVisible()) {
            Platform.runLater(() -> {
                highlightButtonText(resultsButton);
                hideVisible();

                if (!newExecutionController.isSimulationEmpty())
                    SingletonEngineAPI.api.removeUnusedSimulations();

                Platform.runLater(() -> GuiUtils.fadeInAnimation(resultsScreenController.getContainer()));
            });
        }
    }

    private void prepareSimulation(String uuid) throws Exception {
        SingleSimulationDTO simulation = SingletonObjectMapper.objectMapper.readValue(
                SingletonEngineAPI.api.getPastSimulation(uuid).getData(),
                SingleSimulationDTO.class);

        newExecutionController.setCurrentSimulation(simulation);
    }
}

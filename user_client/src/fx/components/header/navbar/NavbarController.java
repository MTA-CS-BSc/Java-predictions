package fx.components.header.navbar;

import consts.Alerts;
import consts.Animations;
import fx.components.results.ResultsController;
import fx.components.details.DetailsController;
import fx.components.execution.ExecutionController;
import fx.components.requests.RequestsController;
import fx.components.selected.SelectedProps;
import gui.GuiUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class NavbarController implements Initializable {
    private DetailsController detailsController;
    private ResultsController resultsController;
    private RequestsController requestsController;
    private ExecutionController executionController;

    @FXML private Button detailsButton;
    @FXML private Button requestsButton;
    @FXML private Button executionButton;
    @FXML private Button resultsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        highlightButtonText(detailsButton);
    }

    public void setDetailsController(DetailsController controller) {
        detailsController = controller;
    }

    public void setResultsController(ResultsController controller) {
        resultsController = controller;
        resultsController.setNavbarController(this);
    }

    public void setRequestsController(RequestsController controller) {
        requestsController = controller;
        requestsController.setNavbarController(this);
    }

    public void setExecutionController(ExecutionController controller) {
        executionController = controller;
        executionController.setNavbarController(this);
    }

    private void highlightButtonText(Button button) {
        Stream.of(detailsButton, requestsButton, executionButton, resultsButton)
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

    private void hideVisible() {
        Platform.runLater(() -> {
            clearAllTables();
            boolean isAnimationsOn = Animations.IS_ANIMATIONS_ON.getValue();

            if (detailsController.getContainer().isVisible()) {
                if (isAnimationsOn)
                    GuiUtils.fadeOutAnimation(detailsController.getContainer());

                else
                    detailsController.getContainer().setVisible(false);
            }

            if (resultsController.getContainer().isVisible()) {
                if (isAnimationsOn)
                    GuiUtils.fadeOutAnimation(resultsController.getContainer());

                else
                    resultsController.getContainer().setVisible(false);
            }

            if (requestsController.getContainer().isVisible()) {
                if (isAnimationsOn)
                    GuiUtils.fadeOutAnimation(requestsController.getContainer());

                else
                    requestsController.getContainer().setVisible(false);
            }

            if (executionController.getContainer().isVisible()) {
                if (isAnimationsOn)
                    GuiUtils.fadeOutAnimation(executionController.getContainer());

                else
                    executionController.getContainer().setVisible(false);
            }
        });
    }

    @FXML
    public void handleExecutionClicked() {
        if (!executionController.getContainer().isVisible()) {
            if (!Objects.isNull(SelectedProps.CREATING_SIMULATION.getValue())) {
                hideVisible();
                highlightButtonText(executionButton);

                if (Animations.IS_ANIMATIONS_ON.getValue())
                    Platform.runLater(() -> GuiUtils.fadeInAnimation(executionController.getContainer()));

                else
                    Platform.runLater(() -> executionController.getContainer().setVisible(true));
            }

            else {
                Alerts.showAlert("", "Entering this screen requires execution from requests", Alert.AlertType.INFORMATION);
                handleRequestsClicked();;
            }
        }
    }

    @FXML
    public void handleResultsClicked() {
        if (!resultsController.getContainer().isVisible()) {
            hideVisible();
            highlightButtonText(resultsButton);

            if (Animations.IS_ANIMATIONS_ON.getValue())
                Platform.runLater(() -> GuiUtils.fadeInAnimation(resultsController.getContainer()));

            else
                Platform.runLater(() -> resultsController.getContainer().setVisible(true));
        }
    }

    @FXML
    private void handleDetailsClicked() {
        if (!detailsController.getContainer().isVisible()) {
            clearAllTables();
            hideVisible();
            highlightButtonText(detailsButton);

            if (Animations.IS_ANIMATIONS_ON.getValue())
                Platform.runLater(() -> GuiUtils.fadeInAnimation(detailsController.getContainer()));

            else
                Platform.runLater(() -> detailsController.getContainer().setVisible(true));
        }
    }

    @FXML
    public void handleRequestsClicked() {
        if (!requestsController.getContainer().isVisible()) {
            clearAllTables();
            hideVisible();
            highlightButtonText(requestsButton);

            if (Animations.IS_ANIMATIONS_ON.getValue())
                Platform.runLater(() -> GuiUtils.fadeInAnimation(requestsController.getContainer()));

            else
                Platform.runLater(() -> requestsController.getContainer().setVisible(true));
        }
    }

    private void clearAllTables() {
        clearWorldSelection();
    }

    private void clearWorldSelection() {
        detailsController.clearWorldSelection();
    }
}

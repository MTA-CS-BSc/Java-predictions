package fx.header.navbar.controllers;

import consts.Animations;
import fx.allocations.controllers.AllocationsController;
import fx.mgmt.controllers.MgmtController;
import fx.modules.GuiUtils;
import fx.results.controllers.ResultsController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class NavbarController implements Initializable {
    private MgmtController mgmtController;
    private ResultsController resultsController;
    private AllocationsController allocationsController;

    @FXML private Button mgmtButton;
    @FXML private Button allocationsButton;
    @FXML private Button resultsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        highlightButtonText(mgmtButton);
    }

    public void setMgmtController(MgmtController controller) {
        mgmtController = controller;
    }

    public void setResultsController(ResultsController controller) {
        resultsController = controller;
    }

    public void setAllocationsController(AllocationsController controller) {
        allocationsController = controller;
    }

    private void highlightButtonText(Button button) {
        Stream.of(mgmtButton, allocationsButton, resultsButton)
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

//    @FXML
//    private void showSimulationDetailsScreen() throws JsonProcessingException {
//        if (!detailsScreenController.getContainer().isVisible()) {
//            hideVisible();
//
//            if (isAnimationsOn.getValue())
//                Platform.runLater(() -> GuiUtils.fadeInAnimation(detailsScreenController.getContainer()));
//
//            else
//                Platform.runLater(() -> detailsScreenController.getContainer().setVisible(true));
//        }
//
//        if (SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api.anyXmlLoaded().getData(), Boolean.class)) {
//            highlightButtonText(detailsButton);
//            detailsScreenController.handleShowCategoriesData();
//        } else {
//            if (isAnimationsOn.getValue()) {
//                TrayNotification tray = new TrayNotification("FAILURE", "XML was not loaded, nothing to show.", NotificationType.ERROR);
//                tray.setAnimationType(AnimationType.FADE);
//                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
//            }
//        }
//    }
//
//    @FXML
//    public void showResultsScreen() {
//        if (!resultsScreenController.getContainer().isVisible()) {
//            hideVisible();
//
//            if (isAnimationsOn.getValue())
//                Platform.runLater(() -> GuiUtils.fadeInAnimation(resultsScreenController.getContainer()));
//
//            else
//                Platform.runLater(() -> resultsScreenController.getContainer().setVisible(true));
//        }
//
//        SingletonThreadpoolManager.executeTask(() -> {
//            highlightButtonText(resultsButton);
//
//            if (!newExecutionController.isSimulationEmpty())
//                SingletonEngineAPI.api.removeUnusedSimulations();
//        });
//    }

    private void hideVisible() {
        Platform.runLater(() -> {
            boolean isAnimationsOn = Animations.IS_ANIMATIONS_ON.getValue();

            if (mgmtController.getContainer().isVisible()) {
                if (isAnimationsOn)
                    GuiUtils.fadeOutAnimation(mgmtController.getContainer());

                else
                    mgmtController.getContainer().setVisible(false);
            }

            if (resultsController.getContainer().isVisible()) {
                if (isAnimationsOn)
                    GuiUtils.fadeOutAnimation(resultsController.getContainer());

                else
                    resultsController.getContainer().setVisible(false);
            }

            if (allocationsController.getContainer().isVisible()) {
                if (isAnimationsOn)
                    GuiUtils.fadeOutAnimation(allocationsController.getContainer());

                else
                    allocationsController.getContainer().setVisible(false);
            }
        });
    }
}

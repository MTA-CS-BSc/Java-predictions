package fx.components.header.navbar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {
//    private MgmtController mgmtController;
//    private ResultsController resultsController;
//    private AllocationsController allocationsController;

    //TODO: Complete navbar
    @FXML private Button detailsButton;
    @FXML private Button requestsButton;
    @FXML private Button executionButton;
    @FXML private Button resultsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        highlightButtonText(mgmtButton);
    }

//    public void setMgmtController(MgmtController controller) {
//        mgmtController = controller;
//    }
//
//    public void setResultsController(ResultsController controller) {
//        resultsController = controller;
//    }
//
//    public void setAllocationsController(AllocationsController controller) {
//        allocationsController = controller;
//    }

    private void highlightButtonText(Button button) {
//        Stream.of(mgmtButton, allocationsButton, resultsButton)
//                .filter(element -> element != button)
//                .forEach(this::unHighlightButtonText);

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
//
//    private void hideVisible() {
//        Platform.runLater(() -> {
//            boolean isAnimationsOn = Animations.IS_ANIMATIONS_ON.getValue();
//
//            if (mgmtController.getContainer().isVisible()) {
//                if (isAnimationsOn)
//                    GuiUtils.fadeOutAnimation(mgmtController.getContainer());
//
//                else
//                    mgmtController.getContainer().setVisible(false);
//            }
//
//            if (resultsController.getContainer().isVisible()) {
//                if (isAnimationsOn)
//                    GuiUtils.fadeOutAnimation(resultsController.getContainer());
//
//                else
//                    resultsController.getContainer().setVisible(false);
//            }
//
//            if (allocationsController.getContainer().isVisible()) {
//                if (isAnimationsOn)
//                    GuiUtils.fadeOutAnimation(allocationsController.getContainer());
//
//                else
//                    allocationsController.getContainer().setVisible(false);
//            }
//        });
//    }

    @FXML
    private void handleExecutionClicked() {

    }
    @FXML
    private void handleResultsClicked() {
//        if (!resultsController.getContainer().isVisible()) {
//            hideVisible();
//
//            if (Animations.IS_ANIMATIONS_ON.getValue())
//                Platform.runLater(() -> GuiUtils.fadeInAnimation(resultsController.getContainer()));
//
//            else
//                Platform.runLater(() -> resultsController.getContainer().setVisible(true));
//        }
//
//        highlightButtonText(resultsButton);
    }

    @FXML
    private void handleDetailsClicked() {
//        if (!mgmtController.getContainer().isVisible()) {
//            hideVisible();
//            highlightButtonText(mgmtButton);
//            clearThreadpoolTableView();
//            clearWorldsTable();
//
//            if (Animations.IS_ANIMATIONS_ON.getValue())
//                Platform.runLater(() -> GuiUtils.fadeInAnimation(mgmtController.getContainer()));
//
//            else
//                Platform.runLater(() -> resultsController.getContainer().setVisible(true));
//        }
    }

    @FXML
    private void handleRequestsClicked() {
//        if (!allocationsController.getContainer().isVisible()) {
//            hideVisible();
//            highlightButtonText(allocationsButton);
//            clearAllocations();
//
//            if (Animations.IS_ANIMATIONS_ON.getValue())
//                Platform.runLater(() -> GuiUtils.fadeInAnimation(allocationsController.getContainer()));
//
//            else
//                Platform.runLater(() -> allocationsController.getContainer().setVisible(true));
//        }
    }
//
//    private void clearThreadpoolTableView() {
//        mgmtController.clearThreadpoolTableView();
//    }
//
//    private void clearWorldsTable() {
//        mgmtController.clearWorldDetails();
//    }
//
//    private void clearAllocations() {
//        allocationsController.clearAllocations();
//    }
}

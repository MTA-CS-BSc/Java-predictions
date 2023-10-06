package fx.component.orchestrator;

import consts.Animations;
import fx.component.allocations.AllocationsController;
import fx.component.results.ResultsController;
import fx.component.header.HeaderController;
import fx.component.mgmt.MgmtController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class OrchestratorController implements Initializable {
    @FXML private HeaderController headerController;
    @FXML private MgmtController mgmtController;
    @FXML private ResultsController resultsController;
    @FXML private AllocationsController allocationsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Animations.IS_ANIMATIONS_ON.setValue(true);

        headerController.setMgmtController(mgmtController);
        headerController.setResultsController(resultsController);
        headerController.setAllocationsController(allocationsController);

        initializeScreen();
    }

    private void initializeScreen() {
        allocationsController.getContainer().setVisible(false);
        resultsController.getContainer().setVisible(false);
    }
}
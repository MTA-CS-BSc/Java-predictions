package fx.orchestrator.controllers;

import fx.allocations.controllers.AllocationsController;
import fx.header.controllers.HeaderController;
import fx.mgmt.controllers.MgmtController;
import fx.results.controllers.ResultsController;
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
package fx.orchestrator;

import fx.allocations.AllocationsController;
import fx.header.HeaderController;
import fx.mgmt.MgmtController;
import fx.results.ResultsController;
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
package fx.components.orchestrator;

import consts.Animations;
import fx.components.results.ResultsController;
import fx.components.details.DetailsController;
import fx.components.execution.ExecutionController;
import fx.components.header.HeaderController;
import fx.components.requests.RequestsController;
import fx.components.selected.SelectedProps;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class OrchestratorController implements Initializable {
    @FXML private HeaderController headerController;
    @FXML private DetailsController detailsController;
    @FXML private RequestsController requestsController;
    @FXML private ExecutionController executionController;
    @FXML private ResultsController resultsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Animations.IS_ANIMATIONS_ON.setValue(true);
        headerController.setDetailsController(detailsController);
        headerController.setRequestsController(requestsController);
        headerController.setResultsController(resultsController);
        headerController.setExecutionController(executionController);

        SelectedProps.CREATING_SIMULATION.addListener((observableValue, singleSimulationDTO, t1) -> {
            if (Objects.isNull(t1))
                headerController.getNavbarController().handleRequestsClicked();

            else
                headerController.getNavbarController().handleExecutionClicked();
        });

        initializeScreen();
    }

    private void initializeScreen() {
        detailsController.getContainer().setVisible(true);
        executionController.getContainer().setVisible(false);
        requestsController.getContainer().setVisible(false);
        resultsController.getContainer().setVisible(false);
    }
}

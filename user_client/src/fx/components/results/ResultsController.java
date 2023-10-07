package fx.components.results;

import fx.components.population.table.PopulationTableController;
import fx.components.results.stats.StatsController;
import fx.components.selected.SelectedProps;
import fx.components.simulations.table.SimulationsTableController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    @FXML
    private VBox container;

    @FXML
    private PopulationTableController populationTableController;

    @FXML
    private SimulationsTableController simulationsTableController;

    @FXML
    private StatsController statsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SelectedProps.RESULTS_SIMULATION.addListener((observableValue, singleSimulationDTO, t1) -> {
            populationTableController.setSelectedSimulation(t1);
        });

        simulationsTableController.setIsParentVisibleProperty(container.visibleProperty());
    }

    public VBox getContainer() {
        return container;
    }
}

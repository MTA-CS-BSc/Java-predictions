package fx.component.results;

import fx.component.results.population.table.PopulationTableController;
import fx.component.results.simulations.table.SimulationsTableController;
import fx.component.results.stats.StatsController;
import fx.component.selected.SelectedProps;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    @FXML private VBox container;

    @FXML private PopulationTableController populationTableController;

    @FXML private SimulationsTableController simulationsTableController;

    @FXML private StatsController statsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        simulationsTableController.setIsParentVisibleProperty(container.visibleProperty());
    }

    public VBox getContainer() {
        return container;
    }

    public void clearSimulationSelection() {
        SelectedProps.SELECTED_SIMULATION.setValue(null);
    }
}

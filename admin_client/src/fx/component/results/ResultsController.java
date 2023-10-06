package fx.component.results;

import fx.component.results.population.table.PopulationTableController;
import fx.component.results.stats.StatsController;
import fx.component.results.simulations.table.SimulationsTableController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import other.SingleSimulationDTO;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    @FXML private VBox container;

    @FXML private PopulationTableController populationTableController;

    @FXML private SimulationsTableController simulationsTableController;

    @FXML private StatsController statsController;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();

        simulationsTableController.setIsParentVisibleProperty(container.visibleProperty());

        simulationsTableController.selectedSimulationProperty().addListener((observableValue, singleSimulationDTO, t1) -> {
            populationTableController.setSelectedSimulation(t1);
            statsController.setSelectedSimulation(t1);
        });
    }

    public VBox getContainer() {
        return container;
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }
}

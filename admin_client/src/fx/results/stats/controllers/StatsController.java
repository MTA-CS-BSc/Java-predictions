package fx.results.stats.controllers;


import fx.results.grid.controllers.WorldGridPaneController;
import fx.results.stats.finished.controllers.FinishedStatsController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import other.SingleSimulationDTO;
import types.SimulationState;

import java.net.URL;
import java.util.ResourceBundle;

public class StatsController implements Initializable {

    @FXML private FinishedStatsController finishedStatsController;

    @FXML private WorldGridPaneController worldGridPaneController;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();

        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            finishedStatsController.setSelectedSimulation(t1.getSimulationState() == SimulationState.FINISHED ? t1 : null);
            worldGridPaneController.setSelectedSimulation(t1.getSimulationState() == SimulationState.PAUSED ? t1 : null);
        });
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }
}


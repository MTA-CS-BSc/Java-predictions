package fx.controllers;

import dtos.SingleSimulationDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsScreenController implements Initializable {
    @FXML
    private VBox container;

    @FXML
    private PopulationTableController populationTableController;

    @FXML
    private SimulationsTableController simulationsTableController;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();

        simulationsTableController.selectedSimulationProperty().addListener((observableValue, singleSimulationDTO, t1) -> populationTableController.setSelectedSimulation(t1));
    }

    public VBox getContainer() {
        return container;
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) { selectedSimulation.setValue(simulation); }
}

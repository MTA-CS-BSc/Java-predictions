package fx.views.Results;

import dtos.SingleSimulationDTO;
import fx.views.PopulationTable.PopulationTableController;
import fx.views.HeaderComponent.HeaderComponentController;
import fx.views.SimulationsTable.SimulationsTableController;
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

    public void setHeaderController(HeaderComponentController controller) {
        simulationsTableController.setHeaderController(controller);
    }

    public VBox getContainer() {
        return container;
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) { selectedSimulation.setValue(simulation); }
}

package fx.components.execution;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import other.SingleSimulationDTO;

import java.net.URL;
import java.util.ResourceBundle;

public class ExecutionController implements Initializable {
    @FXML private VBox container;

    private ObjectProperty<SingleSimulationDTO> creatingSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        creatingSimulation = new SimpleObjectProperty<>();

        creatingSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            //TODO: Implement
        });
    }

    public void setCreatingSimulation(SingleSimulationDTO value) {
        creatingSimulation.setValue(value);
    }

    public VBox getContainer() {
        return container;
    }
}

package fx.views.StatsTable;

import dtos.SingleSimulationDTO;
import helpers.types.SimulationState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StatsTableController implements Initializable {
    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @FXML
    private VBox container;

    @FXML
    private ComboBox<String> filterByComboBox;

    @FXML
    private EntitiesAmountChartController entitiesAmountChartController;

    @FXML
    private PropertyStatsController propertyStatsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        container.setVisible(false);
        selectedSimulation = new SimpleObjectProperty<>();

        filterByComboBox.getSelectionModel().selectedItemProperty()
                        .addListener((observableValue, s, t1) -> {
                            entitiesAmountChartController.toggleVisibility();
                            propertyStatsController.toggleVisibility();
                        });

        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            entitiesAmountChartController.setSelectedSimulation(t1);
            propertyStatsController.setSelectedSimulation(t1);

            container.setVisible(!Objects.isNull(t1) && t1.getSimulationState() == SimulationState.FINISHED);
        });
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }
}

package fx.views.StatsTable;

import dtos.SingleSimulationDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class StatsTableController implements Initializable {
    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @FXML
    private ComboBox<String> filterByComboBox;

    @FXML
    private EntitiesAmountChartController entitiesAmountChartController;

    @FXML
    private PropertyStatsController propertyStatsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();

        filterByComboBox.getSelectionModel().selectedItemProperty()
                        .addListener((observableValue, s, t1) -> {
                            entitiesAmountChartController.toggleVisibility();
                            propertyStatsController.toggleVisibility();
                        });

        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            entitiesAmountChartController.setSelectedSimulation(t1);
        });
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }
}

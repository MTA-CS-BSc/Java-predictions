package fx.results.stats.finished.controllers;


import fx.results.stats.finished.entities.amount.chart.controllers.EntitiesAmountChartController;
import fx.results.stats.finished.properties.controllers.PropertyStatsController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import other.SingleSimulationDTO;
import types.SimulationState;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FinishedStatsController implements Initializable {
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

                    if (!propertyStatsController.getVisible())
                        propertyStatsController.reset();
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


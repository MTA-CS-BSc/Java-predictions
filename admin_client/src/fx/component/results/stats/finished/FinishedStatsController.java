package fx.component.results.stats.finished;


import fx.component.results.stats.finished.entities.amount.chart.EntitiesAmountChartController;
import fx.component.results.stats.finished.properties.PropertyStatsController;
import fx.component.selected.SelectedProps;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import types.SimulationState;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FinishedStatsController implements Initializable {
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

        filterByComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, s, t1) -> {
                    entitiesAmountChartController.toggleVisibility();
                    propertyStatsController.toggleVisibility();

                    if (!propertyStatsController.getVisible())
                        propertyStatsController.reset();
                });

        SelectedProps.SELECTED_SIMULATION.addListener((observableValue, singleSimulationDTO, t1) -> {
            container.setVisible(!Objects.isNull(t1) && t1.getSimulationState() == SimulationState.FINISHED);
        });
    }
}


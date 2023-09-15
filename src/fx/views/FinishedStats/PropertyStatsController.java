package fx.views.FinishedStats;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.EntityDTO;
import dtos.PropertyDTO;
import dtos.SingleSimulationDTO;
import fx.modules.Alerts;
import fx.modules.SingletonEngineAPI;
import helpers.modules.SingletonObjectMapper;
import helpers.types.PropTypes;
import helpers.types.SimulationState;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class PropertyStatsController implements Initializable {
    @FXML
    private VBox container;

    @FXML
    private ComboBox<EntityDTO> entityNamesComboBox;

    @FXML
    private ComboBox<PropertyDTO> propertyNamesComboBox;

    @FXML
    private PieChart histogramChart;

    @FXML
    private Label averageLabel;

    @FXML
    private VBox avgContainer;
    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    private void hideAvgContainer() {
        avgContainer.setVisible(false);
        averageLabel.setText("");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();
        container.setVisible(false);
        propertyNamesComboBox.setDisable(true);
        hideAvgContainer();

        entityNamesComboBox.setCellFactory(getEntityCellFactory());
        propertyNamesComboBox.setCellFactory(getPropertyCellFactory());

        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            if (Objects.isNull(t1))
                entityNamesComboBox.getItems().clear();

            else if (t1.getSimulationState() == SimulationState.FINISHED
                    && (Objects.isNull(singleSimulationDTO) || !singleSimulationDTO.getUuid().equals(t1.getUuid()))) {
                entityNamesComboBox.getItems().clear();
                entityNamesComboBox.getItems().addAll(t1.getWorld().getEntities());
            }
        });

        entityNamesComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, entityDTO, t1) -> {
            if (Objects.isNull(t1))
                propertyNamesComboBox.setDisable(true);

            else {
                propertyNamesComboBox.setDisable(false);
                propertyNamesComboBox.getItems().clear();
                propertyNamesComboBox.getItems().addAll(t1.getProperties());
            }

            hideAvgContainer();
        });

        propertyNamesComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, propertyDTO, t1) -> {
            if (!Objects.isNull(t1)) {
                String entityName = entityNamesComboBox.getSelectionModel().getSelectedItem().getName();
                String propertyName = t1.getName();

                showSelectedPropertyStats(entityName, propertyName);

                if (PropTypes.NUMERIC_PROPS.contains(t1.getType()))
                    showPropertyAverage(entityName, propertyName);

                else
                    hideAvgContainer();
            }
        });
    }

    private void showPropertyAverage(String entityName, String propertyName) {
        try {
            double average = SingletonObjectMapper.objectMapper.readValue(
                    SingletonEngineAPI.api.getPropertyAverage(selectedSimulation.getValue().getUuid(), entityName, propertyName).getData(),
                    Double.class);
            averageLabel.setText(String.valueOf(average));
            avgContainer.setVisible(true);
        } catch (Exception ignored) { }


    }
    private Callback<ListView<EntityDTO>, ListCell<EntityDTO>> getEntityCellFactory() {
        return new Callback<ListView<EntityDTO>, ListCell<EntityDTO>>() {
            @Override
            public ListCell<EntityDTO> call(ListView<EntityDTO> l) {
                return new ListCell<EntityDTO>() {
                    @Override
                    protected void updateItem(EntityDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                            setGraphic(null);

                        else
                            setText(item.getName());
                    }
                };
            }
        };
    }

    private Callback<ListView<PropertyDTO>, ListCell<PropertyDTO>> getPropertyCellFactory() {
        return new Callback<ListView<PropertyDTO>, ListCell<PropertyDTO>>() {
            @Override
            public ListCell<PropertyDTO> call(ListView<PropertyDTO> l) {
                return new ListCell<PropertyDTO>() {
                    @Override
                    protected void updateItem(PropertyDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                            setGraphic(null);

                        else
                            setText(item.getName());
                    }
                };
            }
        };
    }

    public void toggleVisibility() {
        container.setVisible(!container.isVisible());
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }

    public boolean getVisible() {
        return container.isVisible();
    }

    public void reset() {
        entityNamesComboBox.getSelectionModel().select(null);
        propertyNamesComboBox.getSelectionModel().select(null);
    }

    private void showSelectedPropertyStats(String entityName, String propertyName) {
        try {
            Map<String, Long> entitiesCountForProp = SingletonObjectMapper.objectMapper.readValue(
                    SingletonEngineAPI.api.getEntitiesCountForProp(selectedSimulation.getValue().getUuid(), entityName, propertyName).getData(),
                    new TypeReference<Map<String, Long>>() {}
            );

            histogramChart.getData().clear();

            if (entitiesCountForProp.isEmpty())
                Platform.runLater(() -> Alerts.showAlert("", "No histogram",
                        String.format("All instances of %s died during the simulation", entityName),
                        Alert.AlertType.INFORMATION));

            entitiesCountForProp.forEach((propertyValue, amount) -> {
                Platform.runLater(() -> {
                    histogramChart.getData().add(new PieChart.Data(propertyValue, amount));
                    configureChartTooltips();
                });
            });


        } catch (Exception ignored) { }
    }

    private void configureChartTooltips() {
        for (PieChart.Data data : histogramChart.getData()) {
            Tooltip tooltip = new Tooltip(String.valueOf((int)data.getPieValue()));
            Tooltip.install(data.getNode(), tooltip);

            // Add a mouse listener to show tooltips on hover
            data.getNode().setOnMouseEntered(event -> {
                tooltip.show(data.getNode(), event.getScreenX(), event.getScreenY() + 10);
            });

            data.getNode().setOnMouseExited(event -> {
                tooltip.hide();
            });
        }
    }
}

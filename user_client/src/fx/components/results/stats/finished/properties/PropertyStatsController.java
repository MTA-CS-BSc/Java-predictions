package fx.components.results.stats.finished.properties;


import api.results.HttpPropertyStats;
import com.fasterxml.jackson.core.type.TypeReference;
import consts.Alerts;
import fx.components.selected.SelectedProps;
import fx.modules.SingletonThreadpoolManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import json.JsonParser;
import okhttp3.Response;
import other.EntityDTO;
import other.PropertyDTO;
import types.PropTypes;
import types.SimulationState;

import java.net.URL;
import java.util.*;

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

    @FXML
    private HBox avgConsistencyContainer;

    @FXML
    private VBox consistencyContainer;

    @FXML
    private Label consistencyLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        container.setVisible(false);
        propertyNamesComboBox.setDisable(true);
        avgConsistencyContainer.setVisible(false);

        entityNamesComboBox.setCellFactory(getEntityCellFactory());
        propertyNamesComboBox.setCellFactory(getPropertyCellFactory());

        SelectedProps.RESULTS_SIMULATION.addListener((observableValue, singleSimulationDTO, t1) -> {
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

            Platform.runLater(() -> {
                avgConsistencyContainer.setVisible(false);
                histogramChart.getData().clear();
            });
        });

        propertyNamesComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, propertyDTO, t1) -> {
            if (!Objects.isNull(t1)) {
                String entityName = entityNamesComboBox.getSelectionModel().getSelectedItem().getName();
                showSelectedPropertyStats(entityName, t1);
            }
        });
    }

    private void showPropertyAverage(String entityName, String propertyName) {
        try {
            Response response = HttpPropertyStats.getAverage(SelectedProps.RESULTS_SIMULATION.getValue().getUuid(), entityName, propertyName);

            //TODO: Show error details
            if (!response.isSuccessful())
                response.close();

            if (!Objects.isNull(response.body())) {
                averageLabel.setText(String.format("%.5f", JsonParser.objectMapper.readValue(response.body().string(), Double.class)));
                avgContainer.setVisible(true);
            }
        } catch (Exception ignored) {
        }
    }

    private void showPropertyConsistency(String entityName, String propertyName) {
        try {
            Response response = HttpPropertyStats.getConsistency(SelectedProps.RESULTS_SIMULATION.getValue().getUuid(), entityName, propertyName);

            //TODO: Show error details
            if (!response.isSuccessful()) {
                response.close();
                return;
            }

            if (!Objects.isNull(response.body())) {
                consistencyLabel.setText(String.format("%.3f", JsonParser.objectMapper.readValue(response.body().string(), Double.class)));
                consistencyContainer.setVisible(true);
            }
        } catch (Exception ignored) {
        }
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

    private void hideAvgContainer() {
        avgContainer.setVisible(false);
        averageLabel.setText("");
    }

    private void hideConsistencyContainer() {
        consistencyContainer.setVisible(false);
        consistencyLabel.setText("");
    }

    public void toggleVisibility() {
        container.setVisible(!container.isVisible());
    }

    public boolean getVisible() {
        return container.isVisible();
    }

    public void reset() {
        entityNamesComboBox.getSelectionModel().select(null);
        propertyNamesComboBox.getSelectionModel().select(null);
    }

    private void showSelectedPropertyStats(String entityName, PropertyDTO property) {
        histogramChart.getData().clear();
        avgConsistencyContainer.setVisible(true);

        try {
            Response response = HttpPropertyStats.getEntitiesCountForProp(SelectedProps.RESULTS_SIMULATION.getValue().getUuid(), entityName, property.getName());

            //TODO: Show error details
            if (!response.isSuccessful()) {
                response.close();
                return;
            }

            if (!Objects.isNull(response.body())) {
                Map<String, Long> entitiesCountForProp = JsonParser.objectMapper.readValue(
                        response.body().string(),
                        new TypeReference<Map<String, Long>>() {});

                if (entitiesCountForProp.isEmpty())
                    Platform.runLater(() -> Alerts.showAlert("No histogram",
                            String.format("All instances of %s died during the simulation", entityName),
                            Alert.AlertType.INFORMATION));

                SingletonThreadpoolManager.executeTask(() -> {
                    List<PieChart.Data> data = new ArrayList<>();

                    entitiesCountForProp.forEach((propertyValue, amount) -> {
                        data.add(new PieChart.Data(propertyValue, amount));
                    });

                    Platform.runLater(() -> {
                        histogramChart.getData().clear();
                        histogramChart.getData().addAll(data);
                        addChartTooltips();

                        if (!entitiesCountForProp.isEmpty()) {
                            if (PropTypes.NUMERIC_PROPS.contains(property.getType()))
                                showPropertyAverage(entityName, property.getName());

                            else
                                hideAvgContainer();

                            showPropertyConsistency(entityName, property.getName());
                        }

                        else
                            avgConsistencyContainer.setVisible(false);

                    });
                });

            }
        } catch (Exception ignored) {
        }
    }

    private void addChartTooltips() {
        for (PieChart.Data data : histogramChart.getData()) {
            Tooltip tooltip = new Tooltip(String.valueOf((int) data.getPieValue()));
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


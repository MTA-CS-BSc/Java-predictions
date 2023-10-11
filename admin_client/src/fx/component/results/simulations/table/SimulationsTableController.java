package fx.component.results.simulations.table;


import api.ApiConstants;
import api.history.HttpPastSimulations;
import com.fasterxml.jackson.core.type.TypeReference;
import consts.Alerts;
import fx.component.selected.SelectedProps;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import json.JsonParser;
import okhttp3.Response;
import other.SingleSimulationDTO;
import types.SimulationState;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationsTableController implements Initializable {
    //#region Simulations
    @FXML private TableView<SingleSimulationDTO> simulationsTable;

    @FXML private TableColumn<SingleSimulationDTO, String> idColumn;

    @FXML private TableColumn<SingleSimulationDTO, String> createdTimestampColumn;

    @FXML private TableColumn<SingleSimulationDTO, SimulationState> stateColumn;

    @FXML private TableColumn<SingleSimulationDTO, Long> ticksColumn;

    @FXML private TableColumn<SingleSimulationDTO, Long> elapsedTimeColumn;

    @FXML private TableColumn<SingleSimulationDTO, String> createdUserColumn;

    @FXML private TableColumn<SingleSimulationDTO, String> requestUuidColumn;
    //#endregion

    private ReadOnlyBooleanProperty isParentVisible;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isParentVisible = new SimpleBooleanProperty(false);

        initColumns();

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::fetchSimulations, 0,
                        ApiConstants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);

    }

    public void setIsParentVisibleProperty(ReadOnlyBooleanProperty property) {
        isParentVisible = property;
    }

    private void fetchSimulations() {
        if (isParentVisible.getValue()) {
            try {
                Response response = HttpPastSimulations.getPastSimulations();

                //TODO: Show error details
                if (!response.isSuccessful() || Objects.isNull(response.body())) {
                    response.close();
                    return;
                }

                List<SingleSimulationDTO> simulations = JsonParser.objectMapper.readValue(
                        response.body().string(),
                        new TypeReference<List<SingleSimulationDTO>>() {
                        });

                if (simulations.size() < simulationsTable.getItems().size())
                    Platform.runLater(() -> Alerts.showAlert("Simulations were removed",
                            "One or more simulations were removed due to ERROR state reached", Alert.AlertType.INFORMATION));

                Platform.runLater(() -> {
                    simulationsTable.getItems().clear();
                    simulationsTable.getItems().addAll(simulations);
                    simulationsTable.refresh();

                    if (!Objects.isNull(SelectedProps.SELECTED_SIMULATION.getValue()))
                        selectPreviouslySelected();
                });
            } catch (Exception ignored) { }
        }
    }

    private void selectPreviouslySelected() {
        if (Objects.isNull(SelectedProps.SELECTED_SIMULATION.getValue()))
            return;

        SingleSimulationDTO newlySelectedSimulation = simulationsTable.getItems()
                .stream()
                .filter(element -> element.getUuid().equals(SelectedProps.SELECTED_SIMULATION.getValue().getUuid()))
                .findFirst().orElse(null);

        simulationsTable.getSelectionModel().select(newlySelectedSimulation);
        setSelectedSimulation(newlySelectedSimulation);
    }

    @FXML
    private void handleSelectedSimulation(MouseEvent event) {
        if (event.getClickCount() == 1) {
            SingleSimulationDTO selectedSimulation = simulationsTable.getSelectionModel().getSelectedItem();

            if (selectedSimulation != null)
                setSelectedSimulation(selectedSimulation);
        }
    }

    public void setSelectedSimulation(SingleSimulationDTO value) {
        SelectedProps.SELECTED_SIMULATION.setValue(value);
    }

    private void initColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUuid()));
        createdTimestampColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedTimestamp()));
        stateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSimulationState()));
        ticksColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getTicks()).asObject());
        elapsedTimeColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getElapsedTimeMillis()).asObject());
        requestUuidColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRequestUuid()));
        createdUserColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedUser()));
    }

    public void clearSimulations() {
        setSelectedSimulation(null);

        Platform.runLater(() -> {
            simulationsTable.getItems().clear();
            simulationsTable.refresh();
        });
    }
}

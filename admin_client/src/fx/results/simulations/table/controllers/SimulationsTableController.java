package fx.results.simulations.table.controllers;


import consts.Alerts;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import modules.Constants;
import other.SingleSimulationDTO;
import types.SimulationState;

import java.net.URL;
import java.util.Collections;
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

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();

        initColumns();

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::addSimulationsFromAPI, 0,
                        Constants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);

    }

    private void addSimulationsFromAPI() {
        try {
            //TODO: Re-write
//            List<SingleSimulationDTO> simulations = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api
//                            .getPastSimulations().getData(),
//                    new TypeReference<List<SingleSimulationDTO>>() {
//                    });

            List<SingleSimulationDTO> simulations = Collections.emptyList();

            if (simulations.size() < simulationsTable.getItems().size())
                Platform.runLater(() -> Alerts.showAlert("Simulations were removed",
                        "One or more simulations were removed due to ERROR state reached", Alert.AlertType.INFORMATION));

            simulationsTable.getItems().clear();
            simulationsTable.getItems().addAll(simulations);

            if (!Objects.isNull(selectedSimulation.getValue()))
                selectPreviouslySelected();
        } catch (Exception ignored) {
        }
    }

    private void selectPreviouslySelected() {
        SingleSimulationDTO newlySelectedSimulation = simulationsTable.getItems()
                .stream()
                .filter(element -> element.getUuid().equals(selectedSimulation.getValue().getUuid()))
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

    private void initColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUuid()));
        createdTimestampColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedTimestamp()));
        stateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSimulationState()));
        ticksColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getTicks()).asObject());
        elapsedTimeColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getElapsedTimeMillis()).asObject());
        requestUuidColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRequestUuid()));
        createdUserColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedUser()));
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }

    public ObjectProperty<SingleSimulationDTO> selectedSimulationProperty() {
        return selectedSimulation;
    }
}

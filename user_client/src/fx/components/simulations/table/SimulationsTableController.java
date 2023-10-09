package fx.components.simulations.table;


import api.ApiConstants;
import api.history.HttpUserPastSimulations;
import com.fasterxml.jackson.core.type.TypeReference;
import consts.Alerts;
import consts.ConnectedUser;
import fx.components.header.navbar.NavbarController;
import fx.components.selected.SelectedProps;
import fx.components.simulations.table.models.*;
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
import types.ByStep;
import types.SimulationState;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class SimulationsTableController implements Initializable {
    //#region Simulations
    @FXML
    private TableView<SingleSimulationDTO> simulationsTable;

    @FXML
    private TableColumn<SingleSimulationDTO, String> idColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, String> createdTimestampColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, SimulationState> stateColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, Long> ticksColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, Long> elapsedTimeColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, Boolean> pauseButtonColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, Boolean> resumeButtonColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, Boolean> stopButtonColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, Boolean> restartButtonColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, Boolean> pastStepButtonColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, Boolean> futureStepButtonColumn;
    //#endregion

    private ReadOnlyBooleanProperty isParentVisible;

    private NavbarController navbarController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isParentVisible = new SimpleBooleanProperty();

        initColumns();

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::fetchSimulations, 0,
                        ApiConstants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    private void fetchSimulations() {
        if (isParentVisible.getValue()) {
            try {
                Response response = HttpUserPastSimulations.getUserPastSimulations(ConnectedUser.USERNAME_PROPERTY.getValue());

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

                    if (!Objects.isNull(SelectedProps.RESULTS_SIMULATION.getValue()))
                        selectPreviouslySelected();
                });
            } catch (Exception ignored) { }
        }
    }

    private void selectPreviouslySelected() {
        SingleSimulationDTO newlySelectedSimulation = simulationsTable.getItems()
                .stream()
                .filter(element -> element.getUuid().equals(SelectedProps.RESULTS_SIMULATION.getValue().getUuid()))
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
        pauseButtonColumn.setCellFactory(cellData -> new PauseSimulationTableCell(simulationsTable));
        resumeButtonColumn.setCellFactory(cellData -> new ResumeSimulationTableCell(simulationsTable));
        stopButtonColumn.setCellFactory(cellData -> new StopSimulationTableCell(simulationsTable));
        restartButtonColumn.setCellFactory(cellData -> new RestartSimulationTableCell(simulationsTable, this::navigateToExecution));
        pastStepButtonColumn.setCellFactory(cellData -> new TravelSimulationTableCell(simulationsTable, ByStep.PAST));
        futureStepButtonColumn.setCellFactory(cellData -> new TravelSimulationTableCell(simulationsTable, ByStep.FUTURE));

        Stream.of(pauseButtonColumn, resumeButtonColumn, stopButtonColumn,
                        restartButtonColumn, pastStepButtonColumn, futureStepButtonColumn)
                .forEach(column -> column.setSortable(false));
    }

    private void navigateToExecution() {
        navbarController.handleExecutionClicked();
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        SelectedProps.RESULTS_SIMULATION.setValue(simulation);
    }

    public void setIsParentVisibleProperty(ReadOnlyBooleanProperty property) {
        isParentVisible = property;
    }

    public void setNavbarController(NavbarController controller) {
        navbarController = controller;
    }
}

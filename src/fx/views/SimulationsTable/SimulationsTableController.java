package fx.views.SimulationsTable;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.SingleSimulationDTO;
import engine.simulation.ByStep;
import fx.models.Results.*;
import fx.modules.SingletonEngineAPI;
import fx.views.HeaderComponent.HeaderComponentController;
import helpers.Constants;
import helpers.modules.SingletonObjectMapper;
import helpers.types.SimulationState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

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

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    private HeaderComponentController headerController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();

        initColumns();

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::addSimulationsFromAPI, 0,
                        Constants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);

    }

    public HeaderComponentController getHeaderController() {
        return headerController;
    }

    public void setHeaderController(HeaderComponentController headerController) {
        this.headerController = headerController;
    }

    private void addSimulationsFromAPI() {
        try {
            List<SingleSimulationDTO> simulations = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api
                            .getPastSimulations().getData(),
                    new TypeReference<List<SingleSimulationDTO>>() {});

            simulationsTable.getItems().clear();
            simulationsTable.getItems().addAll(simulations);

            if (!Objects.isNull(selectedSimulation.getValue()))
                selectPreviouslySelectedSimulation();
        }

        catch (Exception ignored) {}
    }

    private void selectPreviouslySelectedSimulation() {
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
        pauseButtonColumn.setCellFactory(cellData -> new PauseSimulationTableCell(simulationsTable));
        resumeButtonColumn.setCellFactory(cellData -> new ResumeSimulationTableCell(simulationsTable));
        stopButtonColumn.setCellFactory(cellData -> new StopSimulationTableCell(simulationsTable));
        restartButtonColumn.setCellFactory(cellData -> new RestartSimulationTableCell(simulationsTable, this));
        pastStepButtonColumn.setCellFactory(cellData -> new TravelSimulationTableCell(simulationsTable, ByStep.PAST));
        futureStepButtonColumn.setCellFactory(cellData -> new TravelSimulationTableCell(simulationsTable, ByStep.FUTURE));

        Stream.of(pauseButtonColumn, resumeButtonColumn, stopButtonColumn,
                        restartButtonColumn, pastStepButtonColumn, futureStepButtonColumn)
                .forEach(column -> column.setSortable(false));
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) { selectedSimulation.setValue(simulation); }

    public ObjectProperty<SingleSimulationDTO> selectedSimulationProperty() {
        return selectedSimulation;
    }
}

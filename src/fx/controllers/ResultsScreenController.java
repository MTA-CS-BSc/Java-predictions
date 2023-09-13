package fx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.SingleSimulationDTO;
import fx.modules.SingletonEngineAPI;
import helpers.Constants;
import helpers.modules.SingletonObjectMapper;
import helpers.types.SimulationState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ResultsScreenController implements Initializable {
    @FXML
    private VBox container;

    //#region Simulations
    @FXML
    private TableView<SingleSimulationDTO> simulationsTable;

    @FXML
    private TableColumn<SingleSimulationDTO, String> idColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, String> startTimestampColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, SimulationState> stateColumn;
    //#endregion

    @FXML
    private PopulationTableController populationTableController;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUuid()));
        startTimestampColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTimestamp()));
        stateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSimulationState()));

        //TODO: Add thread manager
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::addSimulationsFromAPI, 0,
                        Constants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);

        selectedSimulation = new SimpleObjectProperty<>();
        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            populationTableController.setSelectedSimulation(t1);
        });
    }

    private void addSimulationsFromAPI() {
        try {
            List<SingleSimulationDTO> simulations = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api
                            .getPastSimulations().getData(),
                    new TypeReference<List<SingleSimulationDTO>>() {});

            simulationsTable.getItems().clear();
            simulationsTable.getItems().addAll(simulations);
        }

        catch (Exception ignored) {}
    }

    @FXML
    private void handleSelectedSimulation(MouseEvent event) {
        if (event.getClickCount() == 1) {
            SingleSimulationDTO selectedSimulation = simulationsTable.getSelectionModel().getSelectedItem();

            if (selectedSimulation != null)
                this.selectedSimulation.set(selectedSimulation);
        }
    }

    public VBox getContainer() {
        return container;
    }
}

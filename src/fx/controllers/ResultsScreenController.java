package fx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.SingleSimulationDTO;
import fx.modules.SingletonEngineAPI;
import helpers.Constants;
import helpers.modules.SingletonObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ResultsScreenController implements Initializable {
    @FXML
    private GridPane container;
    @FXML
    private TableView<SingleSimulationDTO> simulationsTable;

    @FXML
    private TableColumn<SingleSimulationDTO, String> idColumn;

    @FXML
    private TableColumn<SingleSimulationDTO, String> startTimestampColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUuid()));
        startTimestampColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTimestamp()));

        //TODO: Add thread manager
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::addSimulationsFromAPI, 0,
                        Constants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
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

    public GridPane getContainer() {
        return container;
    }
}

package fx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.SingleSimulationDTO;
import fx.modules.SingletonEngineAPI;
import helpers.modules.SingletonObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SimulationIdsController implements Initializable {
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
    }

    private void addSimulationsFromAPI() throws Exception {
        List<SingleSimulationDTO> simulations = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api
                        .getPastSimulations().getData(),
                new TypeReference<List<SingleSimulationDTO>>() {});

        simulationsTable.getItems().clear();
        simulationsTable.getItems().addAll(simulations);
    }
}

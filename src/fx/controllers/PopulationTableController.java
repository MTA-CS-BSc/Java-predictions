package fx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.EntityDTO;
import dtos.ResponseDTO;
import fx.modules.Alerts;
import fx.modules.SingletonEngineAPI;
import helpers.Constants;
import helpers.modules.SingletonObjectMapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PopulationTableController implements Initializable {
    private boolean isInitial;

    @FXML
    private TableView<EntityDTO> populationTable;

    @FXML
    private TableColumn<EntityDTO, String> entityNameColumn;

    @FXML
    private TableColumn<EntityDTO, Integer> populationColumn;

    private String simulationUuid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        simulationUuid = "";
        entityNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        populationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPopulation()).asObject());
    }

    public void setIsInitial(boolean value) { isInitial = value; }

    public void setSimulationUuid(String value) { simulationUuid = value; }

    public void addPopulationsFromEntitiesList(List<EntityDTO> entities) {
        populationTable.getItems().clear();
        populationTable.getItems().addAll(entities);
    }

    public void addPopulationEditCommit() {
        if (simulationUuid.isEmpty())
            return;

        populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        populationColumn.setOnEditCommit(event -> {
            EntityDTO editedEntity = event.getRowValue();

            try {
                ResponseDTO response = SingletonEngineAPI.api
                        .setEntityInitialPopulation(simulationUuid, editedEntity.getName(), event.getNewValue());

                if (response.getStatus() != Constants.API_RESPONSE_OK) {
                    Alerts.showAlert("Validation failed", "Population is invalid",
                            response.getErrorDescription().getCause(), Alert.AlertType.ERROR);

                    editedEntity.setPopulation(event.getOldValue());
                    populationTable.refresh();
                }

                else
                    editedEntity.setPopulation(event.getNewValue());

            } catch (Exception ignored) { }
        });
    }

    public boolean validateAllInitialized() {
        return populationTable.getItems()
                .stream()
                .anyMatch(entity -> entity.getPopulation() > 0);
    }

    public void clearPopulationTable() throws Exception {
        if (simulationUuid.isEmpty())
            return;

        for (EntityDTO entity : populationTable.getItems())
            SingletonEngineAPI.api.setEntityInitialPopulation(simulationUuid, entity.getName(), 0);

        List<EntityDTO> entities = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api
                        .getEntities(simulationUuid, isInitial).getData(),
                new TypeReference<List<EntityDTO>>() {});

        addPopulationsFromEntitiesList(entities);
    }
}

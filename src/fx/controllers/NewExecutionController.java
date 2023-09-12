package fx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.EntityDTO;
import dtos.PropertyDTO;
import fx.modules.Alerts;
import fx.modules.SingletonEngineAPI;
import helpers.modules.SingletonObjectMapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewExecutionController implements Initializable {
    @FXML
    private VBox container;

    //#region Population Table
    @FXML
    private TableView<EntityDTO> populationTable;

    @FXML
    private TableColumn<EntityDTO, String> entityNameColumn;

    @FXML
    private TableColumn<EntityDTO, Integer> populationColumn;
    //#endregion

    @FXML
    private TableView<PropertyDTO> envPropsTable;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPopulationTable();
    }

    private void initPopulationTable() {
        entityNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        populationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPopulation()).asObject());
    }

    private void addPopulationEditCommit(String uuid) {
        populationColumn.setOnEditCommit(event -> {
            EntityDTO editedEntity = event.getRowValue();

            //TODO: Add ui exception of exceeds maximum space
            if (event.getNewValue() < 0) {
                Alerts.showAlert("Validation failed", "Population is invalid",
                        "Population must be non-negative!", Alert.AlertType.ERROR);

                editedEntity.setPopulation(event.getOldValue());
                populationTable.refresh();
                return;
            }

            //TODO: Do that in the end to enable clearing.
            SingletonEngineAPI.api.setEntityInitialPopulation(uuid, editedEntity.getName(), editedEntity.getPopulation());
        });
    }

    protected void addInitEntitiesDataToTable(String uuid) throws Exception {
        //TODO: Add UI exception
        if (uuid.isEmpty() || SingletonEngineAPI.api.getEntities(uuid).getStatus() != 200)
            return;

        List<EntityDTO> entities = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api.getEntities(uuid).getData(),
                    new TypeReference<List<EntityDTO>>() {});

        populationTable.getItems().addAll(entities);
        addPopulationEditCommit(uuid);
    }

    public VBox getContainer() {
        return container;
    }
}

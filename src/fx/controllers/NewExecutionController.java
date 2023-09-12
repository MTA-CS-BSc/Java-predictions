package fx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.EntityDTO;
import dtos.PropertyDTO;
import dtos.RangeDTO;
import dtos.ResponseDTO;
import fx.modules.Alerts;
import fx.modules.SingletonEngineAPI;
import helpers.modules.SingletonObjectMapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    //#region Environment Variables Table
    @FXML
    private TableView<PropertyDTO> envPropsTable;

    @FXML
    private TableColumn<PropertyDTO, String> propertyNameColumn;

    @FXML
    private TableColumn<PropertyDTO, String> propertyTypeColumn;

    @FXML
    private TableColumn<PropertyDTO, RangeDTO> propertyRangeColumn;

    @FXML
    private TableColumn<PropertyDTO, String> propertyValueColumn;
    //#endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPopulationTable();
        initEnvPropsTable();
    }

    private void initEnvPropsTable() {
        propertyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        propertyTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        propertyRangeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRange()));
        propertyValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));
    }

    private void initPopulationTable() {
        entityNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        populationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPopulation()).asObject());
    }

    private void addValueEditCommit(String uuid) {
        propertyValueColumn.setOnEditCommit(event -> {
            PropertyDTO editedProperty = event.getRowValue();
            ResponseDTO response = SingletonEngineAPI.api
                    .setEnvironmentVariable(uuid, editedProperty, event.getNewValue());

            if (response.getStatus() != 200) {
                Alerts.showAlert("Validation failed", "Value is invalid",
                        response.getErrorDescription().getCause(), Alert.AlertType.ERROR);

                editedProperty.setValue(event.getOldValue());
                envPropsTable.refresh();
            }
        });
    }

    private void addPopulationEditCommit(String uuid) {
        populationColumn.setOnEditCommit(event -> {
            EntityDTO editedEntity = event.getRowValue();

            ResponseDTO response = SingletonEngineAPI.api
                    .setEntityInitialPopulation(uuid, editedEntity.getName(), event.getNewValue());

            if (response.getStatus() != 200) {
                Alerts.showAlert("Validation failed", "Population is invalid",
                        response.getErrorDescription().getCause(), Alert.AlertType.ERROR);

                editedEntity.setPopulation(event.getOldValue());
                populationTable.refresh();
            }
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

    protected void addInitEnvPropsDataToTable(String uuid) throws Exception {
        if (uuid.isEmpty() || SingletonEngineAPI.api.getEntities(uuid).getStatus() != 200)
            return;

        List<PropertyDTO> envProps = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api.getEnvironmentProperties(uuid).getData(),
                new TypeReference<List<PropertyDTO>>() {});

        envPropsTable.getItems().addAll(envProps);
        addValueEditCommit(uuid);
    }

    public VBox getContainer() {
        return container;
    }
}

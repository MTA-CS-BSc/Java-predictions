package fx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import dtos.EntityDTO;
import dtos.PropertyDTO;
import dtos.RangeDTO;
import dtos.ResponseDTO;
import fx.modules.Alerts;
import fx.modules.SingletonEngineAPI;
import helpers.Constants;
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
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

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

    private String simulationUuid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPopulationTable();
        initEnvPropsTable();
    }

    public void setSimulationUuid(String value) { simulationUuid = value; }

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

            else
                editedProperty.setValue(event.getNewValue());
        });
    }

    private void addPopulationEditCommit() {
        populationColumn.setOnEditCommit(event -> {
            EntityDTO editedEntity = event.getRowValue();

            try {
                ResponseDTO response = SingletonEngineAPI.api
                        .setEntityInitialPopulation(simulationUuid, editedEntity.getName(), event.getNewValue());

                if (response.getStatus() != 200) {
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

    protected void addInitEntitiesDataToTable() throws Exception {
        //TODO: Add alert
        if (isUuidEmpty())
            return;

        addPopulationsFromAPI();
        addPopulationEditCommit();
    }

    protected void addInitEnvPropsDataToTable() throws Exception {
        //TODO: Add alert
        if (isUuidEmpty())
            return;

        addEnvPropsFromAPI();
        addValueEditCommit(simulationUuid);
    }

    private void addPopulationsFromAPI() throws Exception {
        List<EntityDTO> entities = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api
                        .getEntities(simulationUuid).getData(),
                new TypeReference<List<EntityDTO>>() {});

        populationTable.getItems().clear();
        populationTable.getItems().addAll(entities);
    }

    private void addEnvPropsFromAPI() throws Exception {
        List<PropertyDTO> envProps = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api
                        .getEnvironmentProperties(simulationUuid).getData(),
                new TypeReference<List<PropertyDTO>>() {});

        envPropsTable.getItems().clear();
        envPropsTable.getItems().addAll(envProps);
    }

    public VBox getContainer() {
        return container;
    }

    private boolean isUuidEmpty() throws Exception {
        return simulationUuid.isEmpty() || SingletonEngineAPI.api.getEntities(simulationUuid).getStatus() != 200;
    }

    private void clearPopulationTable() throws Exception {
        for (EntityDTO entity : populationTable.getItems())
            SingletonEngineAPI.api.setEntityInitialPopulation(simulationUuid, entity.getName(), 0);

        addPopulationsFromAPI();
    }

    private void clearEnvPropsTable() throws Exception {
        envPropsTable.getItems().forEach(property -> {
            SingletonEngineAPI.api.setEnvironmentVariable(simulationUuid, property, null);
        });

        addEnvPropsFromAPI();
    }

    @FXML
    private void handleClear() throws Exception {
        //TODO: Add alert
        if (isUuidEmpty())
            return;

        clearPopulationTable();
        clearEnvPropsTable();
    }

    private boolean validateAllInitialized() {
        return populationTable.getItems()
                .stream()
                .anyMatch(entity -> entity.getPopulation() > 0);
    }

    @FXML
    private void handleRun() throws Exception {
        //TODO: Add alert
        if (isUuidEmpty())
            return;

        if (validateAllInitialized()) {
            ResponseDTO response = SingletonEngineAPI.api.runSimulation(simulationUuid);

            if (response.getStatus() == 200) {
                TrayNotification tray = new TrayNotification("SUCCESS", String.format("Simulation [%s] was added to queue manager", simulationUuid), NotificationType.SUCCESS);
                tray.showAndDismiss(Constants.ANIMATION_DURATION);
            }

            else
                Alerts.showAlert("FAILURE", String.format("Simulation [%s] was not added to queue", simulationUuid),
                        String.format("Cause: %s", response.getErrorDescription().getCause()), Alert.AlertType.ERROR);
        }

        else
            Alerts.showAlert("FAILURE", String.format("Simulation [%s] was not added to queue", simulationUuid),
                    "Cause: Please initialize at least one of the entities population to be positive", Alert.AlertType.ERROR);
    }
}

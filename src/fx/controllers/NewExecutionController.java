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
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewExecutionController implements Initializable {
    @FXML
    private VBox container;

    @FXML
    private PopulationTableController populationTableController;

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
    private HeaderComponentController headerComponentController;
    private boolean isInitial;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isInitial = true;
        simulationUuid = "";
        initEnvPropsTable();
    }

    public void setSimulationUuid(String value) throws Exception {
        simulationUuid = value;
        populationTableController.setSimulationUuid(simulationUuid);
        populationTableController.setIsInitial(isInitial);

        if (!value.isEmpty()) {
            addInitEntitiesDataToTable();
            addInitEnvPropsDataToTable();
        }
    }

    public String getSimulationUuid() { return simulationUuid; }

    public void setHeaderComponentController(HeaderComponentController controller) {
        headerComponentController = controller;
    }

    private void initEnvPropsTable() {
        propertyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        propertyTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        propertyRangeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRange()));
        propertyValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));
    }

    private void addValueEditCommit(String uuid) {
        propertyValueColumn.setOnEditCommit(event -> {
            PropertyDTO editedProperty = event.getRowValue();
            ResponseDTO response = SingletonEngineAPI.api
                    .setEnvironmentVariable(uuid, editedProperty, event.getNewValue());

            if (response.getStatus() != Constants.API_RESPONSE_OK) {
                Alerts.showAlert("Validation failed", "Value is invalid",
                        response.getErrorDescription().getCause(), Alert.AlertType.ERROR);

                editedProperty.setValue(event.getOldValue());
                envPropsTable.refresh();
            }

            else
                editedProperty.setValue(event.getNewValue());
        });
    }

    protected void addInitEntitiesDataToTable() throws Exception {
        if (isUuidEmpty())
            return;

        List<EntityDTO> entities = SingletonObjectMapper.objectMapper.readValue(SingletonEngineAPI.api
                        .getEntities(simulationUuid, isInitial).getData(),
                new TypeReference<List<EntityDTO>>() {});

        populationTableController.addPopulationsFromEntitiesList(entities);
        populationTableController.addPopulationEditCommit();
    }

    protected void addInitEnvPropsDataToTable() throws Exception {
        if (isUuidEmpty())
            return;

        addEnvPropsFromAPI();
        addValueEditCommit(simulationUuid);
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
        return simulationUuid.isEmpty() ||
                SingletonEngineAPI.api.getEntities(simulationUuid, isInitial).getStatus() != Constants.API_RESPONSE_OK;
    }

    private void clearEnvPropsTable() throws Exception {
        envPropsTable.getItems().forEach(property -> {
            SingletonEngineAPI.api.setEnvironmentVariable(simulationUuid, property, null);
        });

        addEnvPropsFromAPI();
    }

    @FXML
    private void handleClear() throws Exception {
        if (isUuidEmpty())
            return;

        populationTableController.clearPopulationTable();
        clearEnvPropsTable();
    }

    @FXML
    private void handleRun() throws Exception {
        if (isUuidEmpty())
            return;

        if (populationTableController.validateAllInitialized()) {
            ResponseDTO response = SingletonEngineAPI.api.runSimulation(simulationUuid);

            if (response.getStatus() == Constants.API_RESPONSE_OK) {
                TrayNotification tray = new TrayNotification("SUCCESS", String.format("Simulation [%s] was added to queue manager", simulationUuid), NotificationType.SUCCESS);
                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
                headerComponentController.showResultsScreen();
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

package fx.views.NewExecution;

import dtos.PropertyDTO;
import dtos.RangeDTO;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
import fx.views.PopulationTable.PopulationTableController;
import fx.modules.Alerts;
import fx.modules.SingletonEngineAPI;
import fx.views.HeaderComponent.HeaderComponentController;
import helpers.Constants;
import helpers.types.SimulationState;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
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
import java.util.Objects;
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

    private HeaderComponentController headerComponentController;
    private ObjectProperty<SingleSimulationDTO> currentSimulation;
    private boolean isInitial;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isInitial = true;
        currentSimulation = new SimpleObjectProperty<>();
        initEnvPropsTable();

        currentSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            populationTableController.setSelectedSimulation(t1);

            if (Objects.isNull(t1))
                envPropsTable.getItems().clear();

            else {
                if (t1.getSimulationState() == SimulationState.CREATED)
                    addValueEditCommit();

                envPropsTable.getItems().clear();
                envPropsTable.getItems().addAll(t1.getWorld().getEnvironment());
            }
        });
    }

    public void setCurrentSimulation(SingleSimulationDTO simulation) { currentSimulation.setValue(simulation); }

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

    private void addValueEditCommit() {
        propertyValueColumn.setOnEditCommit(event -> {
            PropertyDTO editedProperty = event.getRowValue();
            ResponseDTO response = SingletonEngineAPI.api
                    .setEnvironmentVariable(currentSimulation.getValue().getUuid(),
                            editedProperty, event.getNewValue());

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

    public VBox getContainer() {
        return container;
    }

    public SingleSimulationDTO getCurrentSimulation() {
        return currentSimulation.getValue();
    }

    public boolean isSimulationEmpty() {
        return Objects.isNull(currentSimulation.getValue()) ||
                SingletonEngineAPI.api.getEntities(currentSimulation.getValue().getUuid(), isInitial).getStatus() != Constants.API_RESPONSE_OK;
    }

    private void clearEnvPropsTable() {
        envPropsTable.getItems().forEach(property -> {
            SingletonEngineAPI.api.setEnvironmentVariable(currentSimulation.getValue().getUuid(), property, null);
        });

        envPropsTable.refresh();
    }

    @FXML
    private void handleClear() {
        if (isSimulationEmpty())
            return;

        populationTableController.clearPopulationTable();
        clearEnvPropsTable();
    }

    @FXML
    private void handleRun() {
        if (isSimulationEmpty())
            return;

        String simulationUuid = currentSimulation.getValue().getUuid();

        if (populationTableController.validateAllInitialized()) {
            ResponseDTO response = SingletonEngineAPI.api.enqueueSimulation(simulationUuid);

            if (response.getStatus() == Constants.API_RESPONSE_OK) {
                TrayNotification tray = new TrayNotification("SUCCESS", "Simulation was added to queue manager", NotificationType.SUCCESS);
                Platform.runLater(() -> tray.showAndDismiss(Constants.ANIMATION_DURATION));
                headerComponentController.showResultsScreen();
            }

            else
                Alerts.showAlert("FAILURE", "Simulation was not added to queue",
                        String.format("Cause: %s", response.getErrorDescription().getCause()), Alert.AlertType.ERROR);
        }

        else
            Alerts.showAlert("FAILURE", "Simulation was not added to queue",
                    "Cause: Please initialize at least one of the entities population to be positive", Alert.AlertType.ERROR);
    }
}

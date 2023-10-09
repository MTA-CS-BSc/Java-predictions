package fx.components.execution;

import api.simulation.HttpSimulation;
import api.simulation.set.HttpSimulationSetters;
import consts.Alerts;
import consts.Animations;
import fx.components.header.navbar.NavbarController;
import fx.components.population.table.PopulationTableController;
import fx.components.selected.SelectedProps;
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
import modules.Restrictions;
import okhttp3.Response;
import other.PropertyDTO;
import other.RangeDTO;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import types.SimulationState;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ExecutionController implements Initializable {
    @FXML private VBox container;

    @FXML
    private PopulationTableController populationTableController;

//    #region Environment Variables Table
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

    private NavbarController navbarController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initEnvPropsTable();

        SelectedProps.CREATING_SIMULATION.addListener((observableValue, singleSimulationDTO, t1) -> {
            populationTableController.setSelectedSimulation(t1);

            if (Objects.isNull(t1))
                envPropsTable.getItems().clear();

            else {
                if (t1.getSimulationState() == SimulationState.CREATED)
                    addValueEditCommit();

                envPropsTable.getItems().clear();
                envPropsTable.getItems().addAll(t1.getWorld().getEnvironment());
                envPropsTable.refresh();
            }
        });
    }

    public void setNavbarController(NavbarController controller) {
        navbarController = controller;
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

            try {
                Response response = HttpSimulationSetters.setEnvironmentProperty(SelectedProps.CREATING_SIMULATION.getValue().getUuid(),
                        editedProperty.getName(), Integer.parseInt(event.getNewValue()));

                //TODO: Show error details
                if (!response.isSuccessful()) {
                    if (!Objects.isNull(response.body()))
                        Alerts.showAlert("Validation failed", response.body().string(), Alert.AlertType.ERROR);

                    response.close();

                    editedProperty.setValue(event.getOldValue());
                    envPropsTable.refresh();
                }

                else
                    editedProperty.setValue(event.getNewValue());
            } catch (Exception ignored) { }
        });
    }

    public VBox getContainer() {
        return container;
    }

    public boolean isSimulationEmpty() {
        return Objects.isNull(SelectedProps.CREATING_SIMULATION.getValue());
    }

    private void clearEnvPropsTable() {
        envPropsTable.getItems().forEach(property -> {
            try {
                Response response = HttpSimulationSetters.setEnvironmentProperty(SelectedProps.CREATING_SIMULATION.getValue().getUuid(), property.getName(), null);

                //TODO: Show error details
                if (!response.isSuccessful())
                    response.close();

            } catch (Exception ignored) { }
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

        if (populationTableController.validateAllInitialized()) {
            try {
                String simulationUuid = SelectedProps.CREATING_SIMULATION.getValue().getUuid();
                Response response = HttpSimulation.enqueueSimulation(simulationUuid);

                //TODO: Show error details
                if (!response.isSuccessful()) {
                    response.close();
                    return;
                }

                if (Animations.IS_ANIMATIONS_ON.getValue()) {
                    TrayNotification tray = new TrayNotification("SUCCESS", "Simulation was added to queue manager", NotificationType.SUCCESS);
                    Platform.runLater(() -> tray.showAndDismiss(Restrictions.ANIMATION_DURATION));
                }

                navbarController.handleResultsClicked();
            } catch (Exception ignored) { }
        } else
            Alerts.showAlert("FAILURE",  "Please initialize at least one of the entities population to be positive", Alert.AlertType.ERROR);
    }
}

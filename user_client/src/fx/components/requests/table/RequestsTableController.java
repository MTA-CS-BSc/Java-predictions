package fx.components.requests.table;

import api.ApiConstants;
import api.requests.HttpAllocations;
import api.simulation.HttpSimulation;
import com.fasterxml.jackson.core.type.TypeReference;
import consts.Alerts;
import consts.ConnectedUser;
import fx.components.header.navbar.NavbarController;
import fx.components.requests.table.models.ExecuteTableCell;
import fx.components.selected.SelectedProps;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import json.JsonParser;
import okhttp3.Response;
import other.AllocationRequestDTO;
import other.SingleSimulationDTO;
import types.RequestState;
import types.SimulationState;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RequestsTableController implements Initializable {
    @FXML private TableView<AllocationRequestDTO> requestsTableView;
    @FXML private TableColumn<AllocationRequestDTO, String> requestUuidColumn;
    @FXML private TableColumn<AllocationRequestDTO, String> worldNameColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> executionsAmountColumn;
    @FXML private TableColumn<AllocationRequestDTO, RequestState> stateColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> runningSimulationsInRequestColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> finishedSimulationsInRequestColumn;
    @FXML private TableColumn<AllocationRequestDTO, Boolean> executeColumn;

    private ReadOnlyBooleanProperty isParentVisible;
    private NavbarController navbarController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO: Change
        ConnectedUser.USERNAME_PROPERTY.setValue("maya");

        isParentVisible = new SimpleBooleanProperty();
        initColumns();

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::fetchAllocationRequests, 0,
                        ApiConstants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    public void setNavbarController(NavbarController controller) {
        navbarController = controller;
    }

    private void initColumns() {
        requestUuidColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUuid()));
        worldNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInitialWorldName()));
        stateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getState()));
        executionsAmountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRequestedExecutions()).asObject());
        runningSimulationsInRequestColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getRequestSimulations().stream().filter(element -> element.getSimulationState() == SimulationState.RUNNING).count()).asObject());
        finishedSimulationsInRequestColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getRequestSimulations().stream().filter(element -> element.getSimulationState() == SimulationState.FINISHED).count()).asObject());
        executeColumn.setCellFactory(cellData -> new ExecuteTableCell(requestsTableView, this::navigateToExecution));
    }

    public void setCreatingSimulation(SingleSimulationDTO value) {
        SelectedProps.CREATING_SIMULATION.setValue(value);
    }

    private void navigateToExecution() {
        ObjectProperty<AllocationRequestDTO> selectedRequest = SelectedProps.SELECTED_REQUEST;

        if (!Objects.isNull(selectedRequest.getValue())) {
            try {
                Response createSimulationResponse = HttpSimulation.createSimulation(selectedRequest.getValue().getUuid());

                if (!createSimulationResponse.isSuccessful()) {
                    createSimulationResponse.close();
                    return;
                }

                if (!Objects.isNull(createSimulationResponse.body())) {
                    String createdSimulationUuid = JsonParser.objectMapper.readValue(createSimulationResponse.body().string(), String.class);
                    Response getCreatingSimulationResponse = HttpSimulation.getCreatingSimulation(createdSimulationUuid);

                    if (!getCreatingSimulationResponse.isSuccessful()) {
                        getCreatingSimulationResponse.close();
                        return;
                    }

                    if (!Objects.isNull(getCreatingSimulationResponse.body())) {
                        SingleSimulationDTO creatingSimulation = JsonParser.objectMapper.readValue(
                                getCreatingSimulationResponse.body().string(),
                                SingleSimulationDTO.class
                        );

                        setCreatingSimulation(creatingSimulation);
                        navbarController.handleExecutionClicked();
                    }
                }

            } catch (Exception e) {
                Alerts.showAlert("ERROR", "Error executing simulation", Alert.AlertType.ERROR);
            }

        }
    }

    private void fetchAllocationRequests() {
        if (isParentVisible.getValue()) {
            try {
                Response response = HttpAllocations.getAllocationsRequests(ConnectedUser.USERNAME_PROPERTY.getValue());

                //TODO: Show error details
                if (!response.isSuccessful()) {
                    response.close();
                    return;
                }

                if (!Objects.isNull(response.body())) {
                    List<AllocationRequestDTO> requests = JsonParser.objectMapper.readValue(
                            response.body().string(),
                            new TypeReference<List<AllocationRequestDTO>>() {
                            });

                    Platform.runLater(() -> {
                        requestsTableView.getItems().clear();
                        requestsTableView.getItems().addAll(requests);
                        requestsTableView.refresh();

                        if (!Objects.isNull(SelectedProps.SELECTED_REQUEST.getValue()))
                            selectPreviouslySelected();
                    });
                }
            } catch (Exception ignored) { }
        }
    }

    private void selectPreviouslySelected() {
        ObjectProperty<AllocationRequestDTO> selectedRequest = SelectedProps.SELECTED_REQUEST;

        if (Objects.isNull(selectedRequest.getValue()))
            return;

        AllocationRequestDTO newlySelectedRequest = requestsTableView.getItems()
                .stream()
                .filter(element -> element.getUuid().equals(selectedRequest.getValue().getUuid()))
                .findFirst().orElse(null);

        requestsTableView.getSelectionModel().select(newlySelectedRequest);
        setSelectedRequest(newlySelectedRequest);
    }

    public void setIsParentVisibleProperty(ReadOnlyBooleanProperty property) {
        isParentVisible = property;
    }

    public void setSelectedRequest(AllocationRequestDTO value) {
        SelectedProps.SELECTED_REQUEST.setValue(value);
    }

    @FXML
    private void handleRequestClicked(MouseEvent event) {
        if (event.getClickCount() == 1) {
            AllocationRequestDTO selected = requestsTableView.getSelectionModel().getSelectedItem();

            if (selected != null)
                setSelectedRequest(selected);
        }
    }

    public void clearRequests() {
        setSelectedRequest(null);

        Platform.runLater(() -> {
            requestsTableView.getItems().clear();
            requestsTableView.refresh();
        });
    }
}

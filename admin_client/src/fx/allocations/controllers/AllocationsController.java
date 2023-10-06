package fx.allocations.controllers;

import api.history.allocations.HttpAllocations;
import com.fasterxml.jackson.core.type.TypeReference;
import fx.allocations.models.ApproveTableCell;
import fx.allocations.models.DeclineTableCell;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import json.JsonMapper;
import modules.Constants;
import okhttp3.Response;
import other.AllocationRequestDTO;
import other.TerminationDTO;
import types.RequestState;
import types.SimulationState;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class AllocationsController implements Initializable {
    @FXML private VBox container;
    @FXML private TableView<AllocationRequestDTO> allocationsTableView;
    @FXML private TableColumn<AllocationRequestDTO, String> requestUuidColumn;
    @FXML private TableColumn<AllocationRequestDTO, String> createdUserColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> executionsAmountColumn;
    @FXML private TableColumn<AllocationRequestDTO, RequestState> stateColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> runningSimulationsInRequestColumn;
    @FXML private TableColumn<AllocationRequestDTO, Boolean> approveColumn;
    @FXML private TableColumn<AllocationRequestDTO, Boolean> declineColumn;
    @FXML private TableColumn<AllocationRequestDTO, TerminationDTO> terminationColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> userTotalFinishedSimulations;

    private ObjectProperty<AllocationRequestDTO> selectedRequest;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedRequest = new SimpleObjectProperty<>();
        initColumns();

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::fetchAllocationRequests, 0,
                        Constants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    private void fetchAllocationRequests() {
        if (container.isVisible()) {
            try {
                Response response = HttpAllocations.getAllocationsRequests();

                if (!response.isSuccessful()) {
                    response.close();
                    return;
                }

                if (!Objects.isNull(response.body())) {
                    List<AllocationRequestDTO> requests = JsonMapper.objectMapper.readValue(
                            response.body().string(),
                            new TypeReference<List<AllocationRequestDTO>>() {
                            });

                    Platform.runLater(() -> {
                        allocationsTableView.getItems().clear();
                        allocationsTableView.getItems().addAll(requests);
                        allocationsTableView.refresh();

                        if (!Objects.isNull(selectedRequest.getValue()))
                            selectPreviouslySelected();
                    });
                }
            } catch (Exception ignored) { }
        }
    }

    private void selectPreviouslySelected() {
        if (Objects.isNull(selectedRequest.getValue()))
            return;

        AllocationRequestDTO newlySelectedRequest = allocationsTableView.getItems()
                .stream()
                .filter(element -> element.getUuid().equals(selectedRequest.getValue().getUuid()))
                .findFirst().orElse(null);

        allocationsTableView.getSelectionModel().select(newlySelectedRequest);
        setSelectedRequest(newlySelectedRequest);
    }

    public void setSelectedRequest(AllocationRequestDTO value) {
        selectedRequest.setValue(value);
    }

    public VBox getContainer() {
        return container;
    }

    public void clearAllocations() {
        Platform.runLater(() -> {
            allocationsTableView.getItems().clear();
            allocationsTableView.refresh();
        });
    }

    private void initColumns() {
        requestUuidColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUuid()));
        createdUserColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedUser()));
        executionsAmountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRequestedExecutions()).asObject());
        stateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getState()));
        runningSimulationsInRequestColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getRequestSimulations().stream().filter(element -> element.getSimulationState() == SimulationState.RUNNING).count()).asObject());
        terminationColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTermination()));

        userTotalFinishedSimulations.setCellValueFactory(cellData -> {
            int amount = allocationsTableView.getItems().stream()
                    .filter(element -> element.getCreatedUser().equals(cellData.getValue().getCreatedUser()))
                    .mapToInt(element -> (int)element.getRequestSimulations().stream().filter(simulation -> simulation.getSimulationState() == SimulationState.FINISHED).count())
                    .sum();

            return new SimpleIntegerProperty(amount).asObject();
        });
        approveColumn.setCellFactory(cellData -> new ApproveTableCell(allocationsTableView));
        declineColumn.setCellFactory(cellData -> new DeclineTableCell(allocationsTableView));

        Stream.of(terminationColumn, approveColumn, declineColumn)
                .forEach(column -> column.setSortable(false));
    }

    @FXML
    private void handleRequestClicked(MouseEvent event) {
        if (event.getClickCount() == 1) {
            AllocationRequestDTO selectedWorld = allocationsTableView.getSelectionModel().getSelectedItem();

            if (selectedWorld != null)
                setSelectedRequest(selectedWorld);
        }
    }
}

package fx.allocations.controllers;

import fx.allocations.models.ApproveTableCell;
import fx.allocations.models.DeclineTableCell;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import other.AllocationRequestDTO;
import other.TerminationDTO;
import types.RequestState;
import types.SimulationState;

import java.net.URL;
import java.util.ResourceBundle;
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

    //TODO: Add parsing
    @FXML private TableColumn<AllocationRequestDTO, Integer> userTotalFinishedSimulations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

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

        approveColumn.setCellFactory(cellData -> new ApproveTableCell(allocationsTableView));
        declineColumn.setCellFactory(cellData -> new DeclineTableCell(allocationsTableView));

        Stream.of(terminationColumn, approveColumn, declineColumn)
                .forEach(column -> column.setSortable(false));
    }
}

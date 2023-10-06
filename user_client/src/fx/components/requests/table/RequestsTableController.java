package fx.components.requests.table;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import other.AllocationRequestDTO;
import types.RequestState;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestsTableController implements Initializable {
    @FXML private TableView<AllocationRequestDTO> requestsTableView;
    @FXML private TableColumn<AllocationRequestDTO, String> requestUuidColumn;
    @FXML private TableColumn<AllocationRequestDTO, String> worldNameColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> executionsAmountColumn;
    @FXML private TableColumn<AllocationRequestDTO, RequestState> stateColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> runningSimulationsColumn;
    @FXML private TableColumn<AllocationRequestDTO, Integer> finishedSimulationsColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

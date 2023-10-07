package fx.components.requests;

import fx.components.requests.table.RequestsTableController;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import other.AllocationRequestDTO;
import other.SingleSimulationDTO;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestsController implements Initializable {
    @FXML private VBox container;
    @FXML private RequestsTableController requestsTableController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestsTableController.setIsParentVisibleProperty(container.visibleProperty());
    }

    public VBox getContainer() {
        return container;
    }

    public void clearRequests() {
        requestsTableController.clearRequests();
    }

    public ObjectProperty<SingleSimulationDTO> creatingSimulationProperty() {
        return requestsTableController.creatingSimulationProperty();
    }

    public ObjectProperty<AllocationRequestDTO> selectedRequestProperty() {
        return requestsTableController.selectedRequestProperty();
    }
}

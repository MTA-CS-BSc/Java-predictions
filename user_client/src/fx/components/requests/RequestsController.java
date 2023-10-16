package fx.components.requests;

import fx.components.header.navbar.NavbarController;
import fx.components.requests.create.CreateRequestController;
import fx.components.requests.table.RequestsTableController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestsController implements Initializable {
    @FXML private VBox container;
    @FXML private RequestsTableController requestsTableController;
    @FXML private CreateRequestController createRequestController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestsTableController.setIsParentVisibleProperty(container.visibleProperty());
        createRequestController.setIsParentVisibleProperty(container.visibleProperty());
    }

    public void setNavbarController(NavbarController controller) {
        requestsTableController.setNavbarController(controller);
    }

    public VBox getContainer() {
        return container;
    }

    public void clearRequests() {
        requestsTableController.clearRequests();
    }
}

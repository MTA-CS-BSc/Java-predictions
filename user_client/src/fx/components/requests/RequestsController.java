package fx.components.requests;

import fx.components.requests.table.RequestsTableController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

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
}

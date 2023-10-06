package fx.components.header;

import fx.components.details.DetailsController;
import fx.components.execution.ExecutionController;
import fx.components.header.menu.MenuController;
import fx.components.header.navbar.NavbarController;
import fx.components.requests.RequestsController;
import fx.components.results.ResultsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    @FXML private MenuController menuController;
    @FXML private NavbarController navbarController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setRequestsController(RequestsController controller) {
        navbarController.setRequestsController(controller);
    }

    public void setResultsController(ResultsController controller) {
        navbarController.setResultsController(controller);
    }

    public void setDetailsController(DetailsController controller) {
        navbarController.setDetailsController(controller);
    }

    public void setExecutionController(ExecutionController controller) {
        navbarController.setExecutionController(controller);
    }
}

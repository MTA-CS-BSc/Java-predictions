package fx.component.header;

import fx.component.allocations.AllocationsController;
import fx.component.header.menu.MenuController;
import fx.component.header.navbar.NavbarController;
import fx.component.results.ResultsController;
import fx.component.mgmt.MgmtController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    @FXML private NavbarController navbarController;
    @FXML private MenuController menuController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void setMgmtController(MgmtController controller) {
        navbarController.setMgmtController(controller);
    }

    public void setResultsController(ResultsController controller) {
        navbarController.setResultsController(controller);
    }

    public void setAllocationsController(AllocationsController controller) {
        navbarController.setAllocationsController(controller);
    }
}

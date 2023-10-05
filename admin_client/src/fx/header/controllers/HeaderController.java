package fx.header.controllers;

import fx.allocations.controllers.AllocationsController;
import fx.header.menu.controllers.MenuController;
import fx.header.navbar.controllers.NavbarController;
import fx.mgmt.controllers.MgmtController;
import fx.results.controllers.ResultsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    @FXML private NavbarController navbarController;
    @FXML private MenuController menuController;
    @FXML private AllocationsController allocationsController;

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

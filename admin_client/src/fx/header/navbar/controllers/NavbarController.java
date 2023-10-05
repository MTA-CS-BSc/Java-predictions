package fx.header.navbar.controllers;

import fx.allocations.controllers.AllocationsController;
import fx.mgmt.controllers.MgmtController;
import fx.results.controllers.ResultsController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {
    private MgmtController mgmtController;
    private ResultsController resultsController;
    private AllocationsController allocationsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void setMgmtController(MgmtController controller) {
        mgmtController = controller;
    }

    public void setResultsController(ResultsController controller) {
        resultsController = controller;
    }

    public void setAllocationsController(AllocationsController controller) {
     allocationsController = controller;
    }
}

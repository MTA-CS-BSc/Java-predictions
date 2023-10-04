package fx.orchestrator.controllers;

import fx.header.controllers.HeaderController;
import fx.mgmt.controllers.MgmtController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class OrchestratorController implements Initializable {
    @FXML private HeaderController headerController;
    @FXML private MgmtController mgmtController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }
}
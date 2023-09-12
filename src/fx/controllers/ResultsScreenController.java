package fx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsScreenController implements Initializable {
    @FXML
    private GridPane container;
    @FXML
    private SimulationIdsController simulationIdsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public GridPane getContainer() {
        return container;
    }
}
